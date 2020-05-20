package sbs.controller.qsurveys;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.controller.upload.UploadController;
import sbs.helpers.TextHelper;
import sbs.model.hr.HrUserInfo;
import sbs.model.qsurveys.QSurvey;
import sbs.model.qsurveys.QSurveyAnswer;
import sbs.model.qsurveys.QSurveyBomAnswer;
import sbs.model.qsurveys.QSurveyQuestion;
import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.model.qsurveys.QSurveyTemplate;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.qsurveys.QSurveyAnswersService;
import sbs.service.qsurveys.QSurveyBomAnswersService;
import sbs.service.qsurveys.QSurveyQuestionTypesService;
import sbs.service.qsurveys.QSurveyQuestionsService;
import sbs.service.qsurveys.QSurveyTemplatesService;
import sbs.service.qsurveys.QSurveysService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("qsurveys")
public class QSurveysController {

	@Autowired
	UserService userService;
	@Autowired
	QSurveySessionInfo sessionInfo;
	@Autowired
	QSurveysService surveysService;
	@Autowired
	QSurveyTemplatesService templatesService;
	@Autowired
	QSurveyQuestionTypesService questionTypesService;
	@Autowired
	QSurveyQuestionsService questionsService;
	@Autowired
	QSurveyBomAnswersService bomAnswersService;
	@Autowired
	QSurveyAnswersService answersService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcAdrOptimaService hrService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;
	@Autowired 
	UploadController uploadController;
 
	@ModelAttribute("sessionInfo")
	public QSurveySessionInfo sessionInfo() {
		return sessionInfo;
	}

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		return "qsurveys/dispatch";
	}
	
	@RequestMapping(value = "/list")
	public String list(Model model) {
		model.addAttribute("surveys", surveysService.findAllSortByDateDesc());
		return "qsurveys/list";
	}
	
	@RequestMapping("/show/{id}")
	@Transactional
	public String showSurvey(@PathVariable("id") int id, Model model, Locale locale) throws NoSuchMessageException, NotFoundException {
		boolean noBomDate = false;
		boolean noTemplateDate = false;
		
		QSurvey survey = surveysService.findById(id);
		if(survey == null){
			throw new NotFoundException(messageSource.getMessage("qsurveys.error.survey.not.found", null, locale));
		}
		
		// bom answers
		if(survey.getBomSurveyDate()==null){
			noBomDate = true;
			model.addAttribute("noBomDate", noBomDate);
		}else{
			model.addAttribute("bomAnswers", bomAnswersService.findBySurveyId(survey.getId()));
		}
		
		// template answers
		if(survey.getTemplateSurveyDate()==null){
			noTemplateDate = true;
			model.addAttribute("noTemplateDate", noTemplateDate);
		}
		else{
			model.addAttribute("answers", answersService.findBySurveyId(survey.getId()));
		}
		
		model.addAttribute("photos", uploadController.listFiles(uploadController.getqSurveysPhotoPath()+"/"+survey.getId()));
		model.addAttribute("surveyInfo",survey);
		return "qsurveys/show";
	}
	
	@RequestMapping(value = "/make", params = { "bom" }, method = RequestMethod.POST)
	@Transactional
	public String showMakeBomSurvey(@RequestParam String bom, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		QSurvey survey = surveysService.findById(Integer.parseInt(bom));
		if (survey == null) {
			throw new NotFoundException("Survey not found");
		}

		List<X3BomItem> items = x3Service.findProductionPartsByProductionOrderAndOperation("ATW", survey.getOrderNumber(), Integer.parseInt(survey.getOrderOperation()));

		BomItem item;
		FormBom form = new FormBom();

		for (int i = 0; i < items.size(); i++) {
			item = new BomItem(items.get(i), survey.getOrderQuantityChecked());
			form.getItems().add(item);
		}
		
		model.addAttribute("formBom", form);
		model.addAttribute("surveyInfo", survey);

		return "qsurveys/bomsurvey";

	}
	
	@RequestMapping(value = "/savebom", params = { "bom" }, method = RequestMethod.POST)
	@Transactional
	public String finishBomSurvey(@RequestParam String bom, FormBom formBom, BindingResult bindingResult, Locale locale,
			RedirectAttributes redirectAttrs, Model model) throws NotFoundException {

		QSurvey survey = surveysService.findById(Integer.parseInt(bom));
		if (survey == null) {
			throw new NotFoundException("Survey not found");
		}
		
		QSurveyBomAnswer bsa;
		BomItem bsi;
		ArrayList<QSurveyBomAnswer> answers = new ArrayList<>();

		Double answer;
		String stringAnswer;

		for (int i = 0; i < formBom.items.size(); i++) {
			bsi = formBom.items.get(i);
			bsa = new QSurveyBomAnswer();

			// if empty
			if (bsi.getAnswerQuantity().trim().length() == 0) {
				bindingResult.rejectValue("items[" + i + "].answerQuantity", "qsurveys.error.missing.value",
						"ERROR");
				continue;
			}			
			
			try {
				stringAnswer = bsi.getAnswerQuantity().trim().replace(',', '.');
				answer = Double.valueOf(stringAnswer);
				
				// for INT only				
				// if(bsi.getModelUnit().equals("UN")){
				// if ((bsi.getModelQuantity() == Math.floor(bsi.getModelQuantity())) && !Double.isInfinite(bsi.getModelQuantity())) {
				if(bsi.getModelQuantity() % 1 == 0) {
					// check quantity 
					if(Double.compare(answer, bsi.getModelQuantity())!=0){
						bindingResult.rejectValue("items[" + i + "].answerQuantity", "qsurveys.error.value.not.match.model",
								"ERROR");
						continue;
					}
				}
				
				bsa.setBomSeq(bsi.getSequence());
				bsa.setPartCode(bsi.getPartCode());
				bsa.setPartDescription(bsi.getPartDescription());
				bsa.setModelUnit(bsi.getModelUnit());
				bsa.setModelQuantity(bsi.getModelQuantity());
				bsa.setAnswerQuantity(answer);
				bsa.setComment(bsi.getComment().trim());
				
				// survey one-to-many
				bsa.setSurvey(survey);
				survey.getBomAnswers().add(bsa);
				// to save
				answers.add(bsa);

			} catch (NumberFormatException ex) {
				// if not number
				bindingResult.rejectValue("items[" + i + "].answerQuantity", "qsurveys.error.number.value",
						"ERROR");
				continue;
			}
			
			if(bsi.getComment().trim().length()>100) {
				bindingResult.rejectValue("items[" + i + "].comment", "error.bad.length",
						"ERROR");
				continue;
			}
		}

		// check errors before save
		if (survey.getBomAnswers().size() != formBom.getItems().size()) {
			bindingResult.reject("qsurveys.error.not.enough.answers");
		}
		if (bindingResult.hasErrors()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			model.addAttribute("surveyInfo", survey);
			return "qsurveys/bomsurvey";
		}

		// save
		survey.setBomSurveyDate(new Timestamp(new java.util.Date().getTime()));
		surveysService.save(survey);
		for (QSurveyBomAnswer psts : answers) {
			bomAnswersService.save(psts);
		}

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/show/"+survey.getId();
	}
	
	
	@RequestMapping(value = "/make", params = { "template" }, method = RequestMethod.POST)
	@Transactional
	public String showMakeTemplateSurvey(@RequestParam String template, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		
		QSurvey survey = surveysService.findById(Integer.parseInt(template));
		if (survey == null) {
			throw new NotFoundException("Survey not found");
		}
		Hibernate.initialize(survey.getTemplate().getQuestions());

		List<QSurveyQuestion> questions = questionsService.findListByTemplateIdAsc(survey.getTemplate().getId());
		FormTemplate formTemplate = new FormTemplate();
		QuestionItem qi;
		for (QSurveyQuestion question : questions) {
			qi = new QuestionItem(question);
			formTemplate.getItems().add(qi);
		}

		model.addAttribute("formTemplate", formTemplate);
		model.addAttribute("surveyInfo", survey);

		return "qsurveys/templatesurvey";
		
	}
	
	@RequestMapping(value = "/savetemplate", params = { "template" }, method = RequestMethod.POST)
	@Transactional
	public String finishTemplateSurvey(@RequestParam String template, FormTemplate formTemplate, BindingResult bindingResult, Locale locale,
			RedirectAttributes redirectAttrs, Model model) throws NotFoundException {

		QSurvey survey = surveysService.findById(Integer.parseInt(template));
		if (survey == null) {
			throw new NotFoundException("Survey not found");
		}
		  
		QSurveyQuestionType type;
		QSurveyAnswer sa;
		QuestionItem qi;
		ArrayList<QSurveyAnswer> answers = new ArrayList<>();
		boolean answerPresent;

		for (int i = 0; i < formTemplate.items.size(); i++) {
			answerPresent= false;
			qi = formTemplate.getItems().get(i);
			sa = new QSurveyAnswer();

			type =  questionTypesService.findById(qi.getTypeId());
			if (type == null) {
				throw new NotFoundException("No question type for id = " + qi.getTypeId());
			}
			
			// save survive data
			sa.setSurvey(survey);
			sa.setType(type);
			sa.setQuestionId(qi.getQuestionId());
			sa.setOrder(qi.getOrder());
			sa.setShortQuestionText(qi.getShortText());
			sa.setLongQuestionText(qi.getLongText());
			if(qi.getComment().trim().length()<=100){
				sa.setComment(qi.getComment().trim());
			}
			else{
				bindingResult.rejectValue("items[" + i + "].comment", "error.bad.length",
						"ERROR");
			}
			
			// save answers
			if(type.getCode().equals("qsurveys.type.parameter")){
				if (qi.getReferenceValue().trim().length() > 0 || qi.getCurrentValue().trim().length() > 0) {
					if (qi.getReferenceValue().trim().length() > 0 && qi.getCurrentValue().trim().length() > 0) {
						answerPresent = true;
						sa.setReferenceValue(qi.getReferenceValue().trim());
						sa.setValue(qi.getCurrentValue().trim());
						sa.setBooleanValue(false);
					} else {
						bindingResult.rejectValue("items[" + i + "].referenceValue", "qsurveys.error.both.values", "ERROR");
						bindingResult.rejectValue("items[" + i + "].currentValue", "qsurveys.error.both.values", "ERROR");
					}
				}
			}
			else if(type.getCode().equals("qsurveys.type.boolean")){
				if(qi.isBooleanValue()){
					answerPresent = true;
					sa.setReferenceValue("");
					sa.setValue("");
					sa.setBooleanValue(qi.isBooleanValue());
				}
			}
			else{
				throw new NotFoundException("No validator for type = " + qi.getTypeCode());
			}
			
			if(answerPresent){
				survey.getAnswers().add(sa);
				answers.add(sa);
			}

		}

		if (answers.size() < 2) {
			bindingResult.reject("qsurveys.error.not.enough.answers");
		}

		if (bindingResult.hasErrors()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			model.addAttribute("surveyInfo", survey);
			return "qsurveys/templatesurvey";
		}

		// save
		survey.setTemplateSurveyDate(new Timestamp(new java.util.Date().getTime()));
		surveysService.save(survey);
		for (QSurveyAnswer ans : answers) {
			answersService.save(ans);
		}

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/show/"+survey.getId();
	}
	

	@RequestMapping(value = "/templates")
	public String templates(Model model) {
		model.addAttribute("templates", templatesService.findAll());
		return "qsurveys/templates";
	}

	@RequestMapping(value = "/templates/create")
	public String createTemplate(Model model) {
		model.addAttribute("formTemplateCreate", new FormTemplateCreate());
		return "qsurveys/createtemplate";
	}

	@RequestMapping(value = "/templates/create", method = RequestMethod.POST)
	@Transactional
	public String createTemplate(@Valid FormTemplateCreate formTemplateCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			return "qsurveys/createtemplate";
		}

		// create project object
		QSurveyTemplate template = new QSurveyTemplate();

		template.setActive(false);

		// form fields
		template.setTitle(formTemplateCreate.getTitle());
		template.setDescription(formTemplateCreate.getDescription());
		template.setComment(formTemplateCreate.getComment());

		// save
		templatesService.save(template);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/questions/" + template.getId();

	}

	@RequestMapping("/templates/edit/{id}")
	public String editTemplateView(@PathVariable("id") int id, Model model) throws NotFoundException {
		QSurveyTemplate template = templatesService.findById(id);
		if (template == null) {
			throw new NotFoundException("Template not found");
		}
		FormTemplateEdit formTemplateEdit = new FormTemplateEdit();
		formTemplateEdit.setId(template.getId());
		formTemplateEdit.setActive(template.getActive());
		formTemplateEdit.setTitle(template.getTitle());
		formTemplateEdit.setDescription(template.getDescription());
		formTemplateEdit.setComment(template.getComment());

		model.addAttribute("formTemplateEdit", formTemplateEdit);

		return "qsurveys/edittemplate";
	}

	@RequestMapping(value = "/templates/edit", method = RequestMethod.POST)
	@Transactional
	public String editTemplate(@Valid FormTemplateEdit formTemplateEdit, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		QSurveyTemplate template = templatesService.findById(formTemplateEdit.getId());
		if (template == null) {
			throw new NotFoundException("Template not found");
		}

		// validate
		if (bindingResult.hasErrors()) {
			return "qsurveys/edittemplate";
		}

		// form fields
		template.setTitle(formTemplateEdit.getTitle());
		template.setDescription(formTemplateEdit.getDescription());
		template.setComment(formTemplateEdit.getComment());
		template.setActive(formTemplateEdit.isActive());

		// save
		templatesService.save(template);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/templates/edit/" + template.getId();

	}

	@RequestMapping("/questions/{id}")
	@Transactional
	public String editQuestionsView(@PathVariable("id") int id, Model model) throws NotFoundException {
		QSurveyTemplate template = templatesService.findById(id);
		if (template == null) {
			throw new NotFoundException("Template not found");
		}

		model.addAttribute("template", template);
		model.addAttribute("questions", questionsService.findListByTemplateIdAsc(template.getId()));

		return "qsurveys/questions";
	}

	@RequestMapping("/questions/add/{id}")
	@Transactional
	public String addQuestionsView(@PathVariable("id") int id, Model model) throws NotFoundException {
		QSurveyTemplate template = templatesService.findById(id);
		if (template == null) {
			throw new NotFoundException("Template not found");
		}
		FormQuestionCreate formQuestionCreate = new FormQuestionCreate();
		formQuestionCreate.setTemplateId(template.getId());

		model.addAttribute("formQuestionCreate", formQuestionCreate);
		sessionInfo.setTypes(questionTypesService.findAll());

		return "qsurveys/createquestion";
	}

	@RequestMapping(value = "/questions/add", method = RequestMethod.POST)
	@Transactional
	public String addQuestion(@Valid FormQuestionCreate formQuestionCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		QSurveyTemplate template = templatesService.findById(formQuestionCreate.getTemplateId());
		if (template == null) {
			throw new NotFoundException("Template not found");
		}

		// validate
		if (bindingResult.hasErrors()) {
			return "qsurveys/createquestion";
		}

		QSurveyQuestion question = new QSurveyQuestion();
		question.setOrder(formQuestionCreate.getOrder());
		question.setShortText(formQuestionCreate.getShortText());
		question.setLongText(formQuestionCreate.getLongText());

		QSurveyQuestionType type = questionTypesService.findById(formQuestionCreate.getTypeId());
		if (type == null) {
			throw new NotFoundException("Unknown question type");
		}

		question.setType(type);
		question.setTemplate(template);

		questionsService.save(question);
		template.getQuestions().add(question);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/questions/" + formQuestionCreate.getTemplateId();
	}

	@RequestMapping("/questions/delete/{templateId}/{questionId}")
	@Transactional
	public String deleteQuestion(@PathVariable("templateId") int templateId, @PathVariable("questionId") int questionId,
			RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException {
		QSurveyQuestion question = questionsService.findById(questionId);
		if (question != null) {
			questionsService.remove(question);
			redirectAttrs.addFlashAttribute("msg",
					messageSource.getMessage("action.removed", null, locale) + ": " + question.getShortText());
			return "redirect:/qsurveys/questions/" + templateId;
		} else {
			throw new NotFoundException("Question not found");
		}
	}

	@RequestMapping(value = "/create")
	public String createSurvey(Model model) {
		sessionInfo.setTemplates(templatesService.findAllActiveAscByTitle());
		sessionInfo.setWorkers(hrService.findAllCurrentlyEmployed());
		model.addAttribute("formSurveyCreate", new FormSurveyCreate());
		return "qsurveys/create";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional
	public String createSurvey(@Valid FormSurveyCreate formSurveyCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// INITIAL VALIDATION
		if (bindingResult.hasErrors()) {
			return "qsurveys/create";
		}

		// VALIDATE TEMPLATE
		QSurveyTemplate template = templatesService.findById(formSurveyCreate.getTemplateId());
		if (template == null) {
			bindingResult.rejectValue("templateId", "error.qsurveys.template.notfound", "ERROR");
		}

		// VALIDATE USER
		HrUserInfo hrUser = new HrUserInfo();
		
		if (!formSurveyCreate.isOperatorManual()) {
			// USER CHOOSE
			hrUser = hrService.findCurrentlyEmployedById(formSurveyCreate.getOperatorId());
			if (hrUser == null) {
				bindingResult.rejectValue("operatorId", "error.user.not.found", "ERROR");
			}
		} else {
			int size;
			// USER MANUAL
			// id
			size = formSurveyCreate.getManualOperatorId().length();
			if(size > 10 || size < 1){
				bindingResult.rejectValue("manualOperatorId", "error.bad.length", "ERROR");
			}
			else{
				formSurveyCreate.setManualOperatorId(formSurveyCreate.getManualOperatorId().toLowerCase());
			}
			
			// first name
			size = formSurveyCreate.getManualOperatorFirstName().length();
			if(size > 45 || size < 1){
				bindingResult.rejectValue("manualOperatorFirstName", "error.bad.length", "ERROR");
			}
			else{
				formSurveyCreate.setManualOperatorFirstName(textHelper.capitalizeFull(formSurveyCreate.getManualOperatorFirstName()));
			}
			// last name
			size = formSurveyCreate.getManualOperatorLastName().length();
			if(size > 65 || size < 1){
				bindingResult.rejectValue("manualOperatorLastName", "error.bad.length", "ERROR");
			}
			else{
				formSurveyCreate.setManualOperatorLastName(textHelper.capitalizeFull(formSurveyCreate.getManualOperatorLastName()));
			}
			
		}

		// RETURN BEFORE ORDER IF NEEDED
		if (bindingResult.hasErrors()) {
			return "qsurveys/create";
		}

		// VALIDATE ORDER
		String order, rawLine, operationDescription;
		int operation;

		rawLine = formSurveyCreate.getOrderNumberAndOperation().trim();

		if (rawLine.length() <= 15) {
			bindingResult.rejectValue("orderNumberAndOperation", "qsurveys.combine.number.and.operation", "ERROR");
			return "qsurveys/create";
		}

		order = rawLine.substring(0, 15);
		operation = Integer.parseInt((rawLine.substring(15, rawLine.length())));
		X3ProductionOrderDetails prodDetails = x3Service.getProductionOrderInfoByNumber("ATW", order);

		if (prodDetails == null) {
			bindingResult.rejectValue("orderNumberAndOperation", "production.order.info.not.found", "ERROR");
			return "qsurveys/create";
		}

		operationDescription = x3Service.findOperationDescriptionByProductionOrder("ATW", order, operation);
		if (operationDescription == null) {
			bindingResult.rejectValue("orderNumberAndOperation", "production.operation.not.found",
					new Object[] { operation }, "ERROR");
			return "qsurveys/create";
		}

		// CREATE NEW SURVEY OBJECT
		QSurvey survey = new QSurvey();
		survey.setUser(userService.getAuthenticatedUser());
		survey.setTemplate(template);

		Timestamp credat = new Timestamp(new java.util.Date().getTime());
		survey.setCreationDate(credat);
		survey.setTemplateSurveyDate(null);
		survey.setBomSurveyDate(null);

		survey.setComment(formSurveyCreate.getComment());
		survey.setOrderQuantityChecked(formSurveyCreate.getQuantityChecked());

		survey.setOperatorManual(formSurveyCreate.isOperatorManual());
		if (!formSurveyCreate.isOperatorManual()) {
			survey.setOperatorNumber(hrUser.getId());
			survey.setOperatorFirstName(hrUser.getFirstName());
			survey.setOperatorLastName(hrUser.getLastName());
			survey.setOperatorDepartment(hrUser.getDepartment());
			survey.setOperatorPosition(hrUser.getPosition());
		} else {
			survey.setOperatorNumber(formSurveyCreate.getManualOperatorId());
			survey.setOperatorFirstName(formSurveyCreate.getManualOperatorFirstName());
			survey.setOperatorLastName(formSurveyCreate.getManualOperatorLastName());
			survey.setOperatorDepartment("-");
			survey.setOperatorPosition("-");
		}

		survey.setOrderNumber(prodDetails.getProductionOrderNumber());
		survey.setOrderOperation("" + operation);
		survey.setOrderOperationDescription(operationDescription);
		survey.setOrderProduct(prodDetails.getProductCode());
		survey.setOrderProductDescription(prodDetails.getProductDescription());
		survey.setOrderClientCode(prodDetails.getClientCode());
		survey.setOrderClientName(prodDetails.getClientName());
		survey.setOrderQuantityProduced(prodDetails.getProducedQuantity());
		survey.setOrderSalesNumber(prodDetails.getSalesOrderNumber());

		// save
		surveysService.save(survey);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/show/" + survey.getId();

	}
	
	@RequestMapping("/photos/{id}")
	@Transactional
	public String showPhotosForm(@PathVariable("id") int id, Model model, Locale locale) throws NotFoundException {
		QSurvey survey = surveysService.findById(id);
		if(survey == null){
			throw new NotFoundException(messageSource.getMessage("qsurveys.error.survey.not.found", null, locale));
		}
		model.addAttribute("survey", survey);
		return "qsurveys/photos";
	}

}

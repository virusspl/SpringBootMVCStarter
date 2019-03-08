package sbs.controller.qsurveys;

import java.sql.Timestamp;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.helpers.TextHelper;
import sbs.model.hr.HrUserInfo;
import sbs.model.qsurveys.QSurvey;
import sbs.model.qsurveys.QSurveyQuestion;
import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.model.qsurveys.QSurveyTemplate;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.service.optima.JdbcAdrOptimaService;
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
	QSurveyQuestionTypesService qSurveyQuestionTypesService;
	@Autowired
	QSurveyQuestionsService qSurveyQuestionsService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcAdrOptimaService hrService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;

	@ModelAttribute("sessionInfo")
	public QSurveySessionInfo sessionInfo() {
		return sessionInfo;
	}

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		return "qsurveys/dispatch";
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
		model.addAttribute("questions", qSurveyQuestionsService.findListByTemplateIdAsc(template.getId()));

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
		sessionInfo.setTypes(qSurveyQuestionTypesService.findAll());

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

		QSurveyQuestionType type = qSurveyQuestionTypesService.findById(formQuestionCreate.getTypeId());
		if (type == null) {
			throw new NotFoundException("Unknown question type");
		}

		question.setType(type);
		question.setTemplate(template);

		qSurveyQuestionsService.save(question);
		template.getQuestions().add(question);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/questions/" + formQuestionCreate.getTemplateId();
	}

	@RequestMapping("/questions/delete/{templateId}/{questionId}")
	@Transactional
	public String deleteQuestion(@PathVariable("templateId") int templateId, @PathVariable("questionId") int questionId,
			RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException {
		QSurveyQuestion question = qSurveyQuestionsService.findById(questionId);
		if (question != null) {
			qSurveyQuestionsService.remove(question);
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
		survey.setOrderQuantityOrdered(prodDetails.getProducedQuantity());

		// save
		surveysService.save(survey);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/view/" + survey.getId();

	}

}

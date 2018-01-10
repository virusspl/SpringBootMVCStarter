package sbs.controller.qualitysurveys;

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

import javassist.NotFoundException;
import sbs.helpers.SortHelper;
import sbs.helpers.TextHelper;
import sbs.model.hr.HrUserInfo;
import sbs.model.qualitysurveys.ParameterSurveyItem;
import sbs.model.qualitysurveys.QualitySurvey;
import sbs.model.qualitysurveys.QualitySurveyBomAnswer;
import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.model.qualitysurveys.QualitySurveyParameterAnswer;
import sbs.model.x3.X3BomItem;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.qualitysurveys.QualitySurveyBomAnswersService;
import sbs.service.qualitysurveys.QualitySurveyParameterAnswersService;
import sbs.service.qualitysurveys.QualitySurveyParametersService;
import sbs.service.qualitysurveys.QualitySurveysService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("qualitysurveys")
public class QualitySurveysController {
	@Autowired
	QualitySurveySessionForm sessionForm;
	@Autowired
	JdbcAdrOptimaService hrService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	QualitySurveyParametersService parametersService;
	@Autowired
	QualitySurveysService surveysService;
	@Autowired
	QualitySurveyParameterAnswersService answersService;
	@Autowired
	QualitySurveyBomAnswersService bomAnswersService;
	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;
	@Autowired
	SortHelper sortHelper;
	
	@ModelAttribute("surveyInfo")
	public QualitySurveySessionForm surveyInfo() {
		return sessionForm;
	}

	 @RequestMapping(value = "/list")
	 @Transactional
	 public String listAll(Model model){
		 model.addAttribute("surveys", surveysService.findAllDesc());
		 return "qualitysurveys/list";
		}
	 
	
	@RequestMapping(value = "/dispatch")
	public String dispatch(){
		return "qualitysurveys/dispatch";
	}
	
	@RequestMapping("/show/{id}")
	@Transactional
	public String showSurvey(@PathVariable("id") int id, Model model, Locale locale) throws NoSuchMessageException, NotFoundException {
		
		QualitySurvey survey = surveysService.findById(id);
		if(survey == null){
			throw new NotFoundException(messageSource.getMessage("quality.surveys.error.survey.not.found", null, locale));
		}

		model.addAttribute("surveyInfo",survey);
		
		if(survey.getType().equals(QualitySurvey.QUALITY_SURVEY_TYPE_BOM)){
			Hibernate.initialize(survey.getUser());
			model.addAttribute("bomAnswers", sortHelper.sortSet(survey.getBomAnswers()));
			
			return "qualitysurveys/surveyview";
		}
		else if(survey.getType().equals(QualitySurvey.QUALITY_SURVEY_TYPE_PARAM)){
			Hibernate.initialize(survey.getUser());
			model.addAttribute("parameterAnswers", sortHelper.sortSet(survey.getParameterAnswers()));
			model.addAttribute("textAnswerType", QualitySurveyParameter.PARAMETER_TEXT);
			model.addAttribute("booleanAnswerType", QualitySurveyParameter.PARAMETER_BOOLEAN);
			return "qualitysurveys/surveyview";
		}
		else{			
			model.addAttribute("error", messageSource.getMessage("quality.surveys.error.unknown.survey.type", null, locale));
			model.addAttribute("surveys", surveysService.findAllDesc());
			return "qualitysurveys/list";
		}
	}
	
	@RequestMapping(value = "/create")
	public String selectUser(Model model, Locale locale) {
		sessionForm = new QualitySurveySessionForm();
		model.addAttribute("operatorForm", new OperatorForm());
		model.addAttribute("hrUsers", hrService.findAllCurrentlyEmployed());
		return "qualitysurveys/selectuser";
	}

	@RequestMapping(value = "/create", params = { "surveyDetails" }, method = RequestMethod.POST)
	public String selectDetails(@Valid OperatorForm operatorForm, BindingResult bindingResult, Model model,
			Locale locale) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("hrUsers", hrService.findAllCurrentlyEmployed());
			return "qualitysurveys/selectuser";
		}

		// get user
		HrUserInfo hrUser = hrService.findCurrentlyEmployedById(operatorForm.getId());

		if (hrUser == null) {
			bindingResult.rejectValue("id", "error.user.not.found", "ERROR");
			model.addAttribute("hrUsers", hrService.findAllCurrentlyEmployed());
			return "qualitysurveys/selectuser";
		}

		// save in session
		sessionForm.setOperatorId(hrUser.getId());
		sessionForm.setOperatorFirstName(hrUser.getFirstName());
		sessionForm.setOperatorLastName(hrUser.getLastName());
		sessionForm.setOperatorRcpNo(hrUser.getRcpNumber());
		sessionForm.setOperatorDepartment(hrUser.getDepartment());
		sessionForm.setOperatorPosition(hrUser.getPosition());

		// prepare model data for survey
		model.addAttribute("productionOrderForm", new ProductionOrderForm());

		return "qualitysurveys/surveydetails";
	}

	@RequestMapping(value = "/create", params = { "summaryBeforeStart" }, method = RequestMethod.POST)
	public String showSummary(@Valid ProductionOrderForm productionOrderForm, BindingResult bindingResult, Model model,
			Locale locale) {
		
		String order, rawLine, operationDescription;
		int operation;
		

		if (bindingResult.hasErrors()) {
			return "qualitysurveys/surveydetails";
		}
		
		rawLine = productionOrderForm.getNumber();
		
		if(rawLine.length()<=15){
			bindingResult.rejectValue("number", "quality.surveys.combine.number.and.operation", "ERROR");
			return "qualitysurveys/surveydetails";
		}
		
		order = rawLine.substring(0, 15);
		operation = Integer.parseInt((rawLine.substring(15, rawLine.length())));
		X3ProductionOrderDetails details = x3Service.getProductionOrderInfoByNumber("ATW",
				order);
		
		if (details == null) {
			bindingResult.rejectValue("number", "production.order.info.not.found", "ERROR");
			return "qualitysurveys/surveydetails";
		}

		operationDescription = x3Service.findOperationDescriptionByProductionOrder("ATW", order, operation);
		if (operationDescription == null) {
			bindingResult.rejectValue("number", "production.operation.not.found",new Object[]{operation}, "ERROR");
			return "qualitysurveys/surveydetails";
		}

		sessionForm.setProductionOrder(details.getProductionOrderNumber());
		sessionForm.setClientCode(details.getClientCode());
		sessionForm.setClientName(details.getClientName());
		sessionForm.setProductCode(details.getProductCode());
		sessionForm.setProductDescription(details.getProductDescription());
		sessionForm.setSalesOrder(details.getSalesOrderNumber());
		sessionForm.setProducedQuantity(details.getProducedQuantity());
		sessionForm.setOperationNumber(operation);
		sessionForm.setOperationDescription(operationDescription);
		return "qualitysurveys/summarybeforestart";
	}

	@RequestMapping(value = "/create", params = { "changeSurveyType" }, method = RequestMethod.POST)
	public String changeSurveyType() {
		return "qualitysurveys/summarybeforestart";
	}

	@RequestMapping(value = "/create", params = { "anotherProductionOrder" }, method = RequestMethod.POST)
	public String changeProductionOrder(ProductionOrderForm productionOrderForm) {

		QualitySurveySessionForm newSessionForm = new QualitySurveySessionForm();
		newSessionForm.copyOperatorInfo(sessionForm);
		sessionForm = newSessionForm;

		return "qualitysurveys/surveydetails";
	}

	@RequestMapping(value = "/create", params = { "beginSuspensionsSurvey" }, method = RequestMethod.POST)
	public String beginSuspensionSurvey(Model model, Locale locale) {

		sessionForm.setType(QualitySurvey.QUALITY_SURVEY_TYPE_BOM);
		List<X3BomItem> items = x3Service.findProductionPartsByProductionOrderAndOperation("ATW", sessionForm.getProductionOrder(), sessionForm.getOperationNumber());
		//List<X3BomItem> items = x3Service.findBomPartsByParent("ATW", sessionForm.getProductCode());

		
		BomSurveyFormItem bsfi;
		BomSurveyForm bsf = new BomSurveyForm();

		for (int i = 0; i < items.size(); i++) {
			bsfi = new BomSurveyFormItem(items.get(i), sessionForm.getProducedQuantity());
			bsf.getItems().add(bsfi);
		}
		model.addAttribute("bomSurveyForm", bsf);

		return "qualitysurveys/bomsurvey";
	}

	@RequestMapping(value = "/create", params = { "beginAxlesSurvey" }, method = RequestMethod.POST)
	public String beginAxlesSurvey(Model model, Locale locale) {

		sessionForm.setType(QualitySurvey.QUALITY_SURVEY_TYPE_PARAM);

		List<QualitySurveyParameter> parameters = parametersService.findAllActive();
		ParameterSurveyForm psf = new ParameterSurveyForm();
		ParameterSurveyItem psi;
		for (QualitySurveyParameter param : parameters) {
			if (!param.isActive()) {
				continue;
			}

			psi = new ParameterSurveyItem();
			psi.setParameterId(param.getId());
			psi.setTitle(param.getTitle());
			psi.setType(param.getType());
			psf.items.add(psi);
		}

		model.addAttribute("parameterSurveyForm", psf);

		return "qualitysurveys/parametersurvey";
	}

	private void prepareSurveyEntityFrom(QualitySurvey survey, QualitySurveySessionForm sessionForm) {
		survey.setUser(userService.getAuthenticatedUser());
		survey.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		survey.setType(sessionForm.getType());
		survey.setClientCode(sessionForm.getClientCode());
		survey.setClientName(sessionForm.getClientName());
		survey.setProductCode(sessionForm.getProductCode());
		survey.setProductDescription(sessionForm.getProductDescription());
		survey.setProductionOrder(sessionForm.getProductionOrder());
		survey.setSalesOrder(sessionForm.getSalesOrder());
		survey.setOperatorDepartment(sessionForm.getOperatorDepartment());
		survey.setOperatorPosition(sessionForm.getOperatorPosition());
		survey.setOperatorFirstName(sessionForm.getOperatorFirstName());
		survey.setOperatorLastName(sessionForm.getOperatorLastName());
		survey.setOperatorId(sessionForm.getOperatorId());
		survey.setOperatorRcpNo(sessionForm.getOperatorRcpNo());
		survey.setProducedQuantity(sessionForm.getProducedQuantity());
		survey.setProductionOperation(sessionForm.getOperationNumber());
	}

	@RequestMapping(value = "/create", params = { "finishParametersSurvey" }, method = RequestMethod.POST)
	@Transactional
	public String finishParametersSurvey(ParameterSurveyForm parameterSurveyForm, BindingResult bindingResult,
			Locale locale, Model model) {

		ParameterSurveyItem psi;
		QualitySurvey survey;
		QualitySurveyParameter psp;
		QualitySurveyParameterAnswer psa;

		survey = new QualitySurvey();
		prepareSurveyEntityFrom(survey, sessionForm);

		ArrayList<QualitySurveyParameterAnswer> answers = new ArrayList<>();

		for (int i = 0; i < parameterSurveyForm.items.size(); i++) {
			psi = parameterSurveyForm.items.get(i);
			psa = new QualitySurveyParameterAnswer();

			switch (psi.getType()) {
			case QualitySurveyParameter.PARAMETER_BOOLEAN:
				if (psi.isYesnoAnswer()) {

					// store data answer
					psa.setYesnoAnswer(psi.isYesnoAnswer());
					psa.setComment(psi.getComment());
					// clear unused
					psa.setModelAnswer("");
					psa.setValueAnswer("");

					// survey one-to-many
					psa.setSurvey(survey);
					survey.getParameterAnswers().add(psa);

					// parameter one-to-many
					psp = parametersService.findById(psi.getParameterId());
					psa.setParameter(psp);
					psp.getAnswers().add(psa);

					// to save
					answers.add(psa);

				}
				break;
			case QualitySurveyParameter.PARAMETER_TEXT:

				psi.setModelAnswer(psi.getModelAnswer().trim());
				psi.setAnswer(psi.getAnswer().trim());

				if (psi.getModelAnswer().length() > 0 || psi.getAnswer().length() > 0) {
					if (psi.getModelAnswer().length() > 0 && psi.getAnswer().length() > 0) {

						// store data answer
						psa.setYesnoAnswer(false);
						// clear unused
						psa.setComment(psi.getComment());
						psa.setModelAnswer(psi.getModelAnswer());
						psa.setValueAnswer(psi.getAnswer());
						// survey one-to-many
						psa.setSurvey(survey);
						survey.getParameterAnswers().add(psa);

						// parameter one-to-many
						psp = parametersService.findById(psi.getParameterId());
						psa.setParameter(psp);
						psp.getAnswers().add(psa);

						// to save
						answers.add(psa);

					} else {
						bindingResult.rejectValue("items[" + i + "].modelAnswer", "quality.surveys.error.both.values",
								"ERROR");
						bindingResult.rejectValue("items[" + i + "].answer", "quality.surveys.error.both.values",
								"ERROR");
					}
				}
				break;
			}
		}

		if (survey.getParameterAnswers().size() < 2) {
			bindingResult.reject("quality.surveys.error.not.enough.answers");
		}

		if (bindingResult.hasErrors()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "qualitysurveys/parametersurvey";
		}

		surveysService.save(survey);
		for (QualitySurveyParameterAnswer psts : answers) {
			answersService.save(psts);
		}

		QualitySurveySessionForm newSessionForm = new QualitySurveySessionForm();
		newSessionForm.copyOperatorInfo(sessionForm);
		sessionForm = newSessionForm;
		model.addAttribute("productionOrderForm", new ProductionOrderForm());

		model.addAttribute("msg", messageSource.getMessage("action.saved", null, locale));

		return "qualitysurveys/surveydetails";
	}

	@RequestMapping(value = "/create", params = { "finishBomSurvey" }, method = RequestMethod.POST)
	@Transactional
	public String finishBomSurvey(BomSurveyForm bomSurveyForm, BindingResult bindingResult, Locale locale,
			Model model) {

		QualitySurvey survey;
		QualitySurveyBomAnswer bsa;
		BomSurveyFormItem bsi;

		survey = new QualitySurvey();
		prepareSurveyEntityFrom(survey, sessionForm);

		ArrayList<QualitySurveyBomAnswer> answers = new ArrayList<>();

		Double answer;
		String stringAnswer;

		for (int i = 0; i < bomSurveyForm.items.size(); i++) {
			bsi = bomSurveyForm.items.get(i);
			bsa = new QualitySurveyBomAnswer();

			// if empty
			if (bsi.getAnswerQuantity().trim().length() == 0) {
				bindingResult.rejectValue("items[" + i + "].answerQuantity", "quality.surveys.error.missing.value",
						"ERROR");
				continue;
			}			
			
			try {
				stringAnswer = bsi.getAnswerQuantity().trim().replace(',', '.');
				answer = Double.valueOf(stringAnswer);
				
				// if wrong quantiy
				if(Double.compare(answer, bsi.getModelQuantity())!=0){
					bindingResult.rejectValue("items[" + i + "].answerQuantity", "quality.surveys.error.value.not.match.model",
							"ERROR");
					continue;
				}
				
				bsa.setSequence(bsi.getSequence());
				bsa.setPartCode(bsi.getPartCode());
				bsa.setPartDescription(bsi.getPartDescription());
				bsa.setModelUnit(bsi.getModelUnit());
				bsa.setComment(bsi.getComment());
				bsa.setAnswerQuantity(answer);
				// survey one-to-many
				bsa.setSurvey(survey);
				survey.getBomAnswers().add(bsa);
				// to save
				answers.add(bsa);

			} catch (NumberFormatException ex) {
				// if not number
				bindingResult.rejectValue("items[" + i + "].answerQuantity", "quality.surveys.error.number.value",
						"ERROR");
				continue;
			}

		}

		// check errors before save
		if (survey.getBomAnswers().size() != bomSurveyForm.getItems().size()) {
			bindingResult.reject("quality.surveys.error.not.enough.answers");
		}
		if (bindingResult.hasErrors()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "qualitysurveys/bomsurvey";
		}

		// save
		surveysService.save(survey);
		for (QualitySurveyBomAnswer psts : answers) {
			bomAnswersService.save(psts);
		}		 

		// prepare to next one
		QualitySurveySessionForm newSessionForm = new QualitySurveySessionForm();
		newSessionForm.copyOperatorInfo(sessionForm);
		sessionForm = newSessionForm;
		model.addAttribute("productionOrderForm", new ProductionOrderForm());

		// say ok
		model.addAttribute("msg", messageSource.getMessage("action.saved", null, locale));

		return "qualitysurveys/surveydetails";
	}

}

package sbs.controller.qualitysurveys;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sbs.model.hr.HrUserInfo;
import sbs.model.qualitysurveys.QualitySurvey;
import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.model.qualitysurveys.QualitySurveyParameterAnswer;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.service.optima.JdbcAdrOptimaService;
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
	UserService userService;
	@Autowired
	MessageSource messageSource;

	@ModelAttribute("surveyInfo")
	public QualitySurveySessionForm surveyInfo() {
		return sessionForm;
	}

	/*
	 TODO
	 	@RequestMapping(value = "/list")
	 	@RequestMapping(value = "/view")
	 */
	
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

		if (bindingResult.hasErrors()) {
			return "qualitysurveys/surveydetails";
		}

		X3ProductionOrderDetails details = x3Service.getProductionOrderInfoByNumber("ATW",
				productionOrderForm.getNumber());
		if (details == null) {
			bindingResult.rejectValue("number", "production.order.info.not.found", "ERROR");
			return "qualitysurveys/surveydetails";
		}

		sessionForm.setProductionOrder(details.getProductionOrderNumber());
		sessionForm.setClientCode(details.getClientCode());
		sessionForm.setClientName(details.getClientName());
		sessionForm.setProductCode(details.getProductCode());
		sessionForm.setProductDescription(details.getProductDescription());
		sessionForm.setSalesOrder(details.getSalesOrderNumber());

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

	@RequestMapping(value = "/create", params = { "finishParametersSurvey" }, method = RequestMethod.POST)
	@Transactional
	public String finishParametersSurvey(ParameterSurveyForm parameterSurveyForm, BindingResult bindingResult,
			Locale locale, Model model) {

		System.out.println(sessionForm);
		System.out.println(parameterSurveyForm);

		ParameterSurveyItem psi;
		QualitySurvey survey;
		QualitySurveyParameter psp;
		QualitySurveyParameterAnswer psa;
		
		survey = new QualitySurvey();
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
		
		ArrayList<QualitySurveyParameterAnswer> answers = new ArrayList<>();
		
		for (int i = 0; i < parameterSurveyForm.items.size(); i++) {
			psi = parameterSurveyForm.items.get(i);
			
			switch (psi.getType()) {
			case QualitySurveyParameter.PARAMETER_BOOLEAN:
				if (psi.isYesnoAnswer()) {
					psa = new QualitySurveyParameterAnswer();
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
				
				if (psi.getModelAnswer().length() > 0 || psi.getAnswer().length() > 0){
					if(psi.getModelAnswer().length() > 0 && psi.getAnswer().length() > 0){
						
						psa = new QualitySurveyParameterAnswer();
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
						
					}
					else{
						bindingResult.rejectValue("items[" + i + "].modelAnswer", "quality.surveys.error.both.values", "ERROR");
						bindingResult.rejectValue("items[" + i + "].answer", "quality.surveys.error.both.values", "ERROR");
					}
				}
				break;
			}
		}

		if(survey.getParameterAnswers().size()<2){
			bindingResult.reject("quality.surveys.error.not.enough.answers");
		}
		
		if (bindingResult.hasErrors()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "qualitysurveys/parametersurvey";
		}
		
		surveysService.save(survey);
		for(QualitySurveyParameterAnswer psts: answers){
			answersService.save(psts);
		}
		
		QualitySurveySessionForm newSessionForm = new QualitySurveySessionForm();
		newSessionForm.copyOperatorInfo(sessionForm);
		sessionForm = newSessionForm;
		model.addAttribute("productionOrderForm", new ProductionOrderForm());
		
		model.addAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		
		return "qualitysurveys/surveydetails";
	}

}

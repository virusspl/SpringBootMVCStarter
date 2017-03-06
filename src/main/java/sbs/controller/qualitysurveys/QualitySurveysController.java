package sbs.controller.qualitysurveys;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sbs.model.hr.HrUserInfo;
import sbs.model.qualitysurveys.QualitySurvey;
import sbs.model.x3.X3ProductionOrderDetails;
import sbs.service.optima.JdbcAdrOptimaService;
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
	
	@ModelAttribute("surveyInfo")
	public QualitySurveySessionForm surveyInfo(){
		return sessionForm;
	}
	
	@RequestMapping(value = "/create")
	public String selectUser(Model model, Locale locale) {
		sessionForm = new QualitySurveySessionForm();
		model.addAttribute("operatorForm", new OperatorForm());
		model.addAttribute("hrUsers", hrService.findAllCurrentlyEmployed());
		return "qualitysurveys/selectuser";
	}
	
	@RequestMapping(value = "/create", params = {"surveyDetails"}, method = RequestMethod.POST)
	public String selectDetails(@Valid OperatorForm operatorForm, BindingResult bindingResult, Model model, Locale locale) {
		
		if(bindingResult.hasErrors()){			
			model.addAttribute("hrUsers", hrService.findAllCurrentlyEmployed());
			return "qualitysurveys/selectuser";
		}

		// get user
		HrUserInfo hrUser =  hrService.findCurrentlyEmployedById(operatorForm.getId());
		
		if(hrUser == null){
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
	
	@RequestMapping(value = "/create", params = {"summaryBeforeStart"}, method = RequestMethod.POST)
	public String showSummary(@Valid ProductionOrderForm productionOrderForm, BindingResult bindingResult, Model model, Locale locale) {
		
		if(bindingResult.hasErrors()){
			return "qualitysurveys/surveydetails";
		}
		
		X3ProductionOrderDetails details = x3Service.getProductionOrderInfoByNumber("ATW", productionOrderForm.getNumber());
		if (details == null){
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
	
	 // NEXT TIME TODO
	// find a way to validate etc.
	 
	@RequestMapping(value = "/create", params = {"beginSuspensionsSurvey"}, method = RequestMethod.POST)
	public String beginSuspensionSurvey(Model model, Locale locale) {
		model.addAttribute("msg", "START SUSPENSIONS SURVEY");
		System.out.println(QualitySurvey.QUALITY_SURVEY_TYPE_BOM);
		return "qualitysurveys/summarybeforestart";
	}
	@RequestMapping(value = "/create", params = {"beginAxlesSurvey"}, method = RequestMethod.POST)
	public String beginAxlesSurvey(Model model, Locale locale) {
		model.addAttribute("msg", "START AXLES SURVEY");
		System.out.println(QualitySurvey.QUALITY_SURVEY_TYPE_PARAM);
		return "qualitysurveys/summarybeforestart";
	}
	
	@RequestMapping(value = "/create", params = {"anotherProductionOrder"}, method = RequestMethod.POST)
	public String changeProductionOrder(ProductionOrderForm productionOrderForm) {
		
		QualitySurveySessionForm newSessionForm = new QualitySurveySessionForm();
		newSessionForm.copyOperatorInfo(sessionForm);
		sessionForm = newSessionForm;
		
		return "qualitysurveys/surveydetails";
	}
}












package sbs.controller.dirrcpship;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
import sbs.model.x3.X3ProductSellDemand;
import sbs.service.dictionary.X3HistoryPriceService;
import sbs.service.x3.JdbcOracleX3Service;


@Controller
@RequestMapping("dirrcpship")
public class DirectReceptionsShipmentController {
	
	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service x3Service;
	@Autowired
	X3HistoryPriceService x3HistoryPriceService;  
	@Autowired
	TextHelper textHelper;
	
	List<String> alwaysShow;
	List<String> alwaysHide;
	
    @Autowired
    public DirectReceptionsShipmentController(Environment env) {
    	alwaysShow = Arrays.asList(env.getRequiredProperty("dirrcpship.codes.alwaysshow").split(";"));
    	alwaysHide = Arrays.asList(env.getRequiredProperty("dirrcpship.codes.alwayshide").split(";"));
    }
    
    private boolean isAlwaysShow(String code){
    	for(String prefix: alwaysShow){
    		if(code.startsWith(prefix)){
    			return true;
    		}
    	}
    	return false;
    }
    
    private boolean isAlwaysHide(String code){
    	for(String prefix: alwaysHide){
    		if(code.startsWith(prefix)){
    			return true;
    		}
    	}
    	return false;
    }

	
    @RequestMapping("/main")
    public String view(Model model, Locale locale){
    	DirectReceptionsShipmentForm directReceptionsShipmentForm = new DirectReceptionsShipmentForm();
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	cal.add(Calendar.DAY_OF_MONTH, -1);
    	directReceptionsShipmentForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
    	cal.add(Calendar.DAY_OF_MONTH, -6);
    	directReceptionsShipmentForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
    	model.addAttribute("directReceptionsShipmentForm", directReceptionsShipmentForm);
    	return "dirrcpship/main";
    }
    
    
	@RequestMapping(value = "/makelist", params = { "viewlist" }, method = RequestMethod.POST)
	public String performSearch(@Valid DirectReceptionsShipmentForm directReceptionsShipmentForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs, Locale locale){
		
		if(bindingResult.hasErrors()){
			return "dirrcpship/main";
		}

		// average 1-month usage
		String startPeriod, endPeriod;
		Calendar calendar = Calendar.getInstance();
		endPeriod = calendar.get(Calendar.YEAR) + "" + textHelper.fillWithLeadingZero(""+calendar.get(Calendar.MONTH), 2);
		calendar.add(Calendar.MONTH, -7);
		startPeriod = calendar.get(Calendar.YEAR) + "" + textHelper.fillWithLeadingZero(""+calendar.get(Calendar.MONTH), 2);
		Map<String, Integer> averageUsage = x3Service.findAcvAverageUsageInPeriod(startPeriod, endPeriod,"ATW");
		
		
		// MAG stock
		Map<String, Integer> magStock = x3Service.findAcvMagStock("ATW");
		// sell demand in period
		Map<String, X3ProductSellDemand> sellDemands = x3Service.findAcvProductSellDemand(directReceptionsShipmentForm.getStartDate(), directReceptionsShipmentForm.getEndDate(), "ATW");
		// non production codes
		List<String> nonProductionCodes = x3Service.findAcvNonProductionCodes("ATW");		
		
		
		
		

		
		
		
		model.addAttribute("startDate", directReceptionsShipmentForm.getStartDate());
		model.addAttribute("endDate", directReceptionsShipmentForm.getEndDate());
		model.addAttribute("list", sellDemands);
		
		
		return "dirrcpship/main";
	}
	
	
	
    
    
}

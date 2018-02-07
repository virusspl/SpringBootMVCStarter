package sbs.controller.utr;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import sbs.controller.geolook.GeodeSearchForm;
import sbs.model.x3.X3UtrMachine;
import sbs.model.x3.X3UtrWorker;
import sbs.service.x3.JdbcOracleX3Service;


@Controller
@RequestMapping("utr")
public class UtrController {
	
	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service x3Service;
	
	List<String> excludedMachines;
	
    @Autowired
    public UtrController(Environment env) {
    	excludedMachines = Arrays.asList(env.getRequiredProperty("utr.machines.mtbfexcluded").split(";"));
    }
	
    @RequestMapping("/stats")
    public String view(Model model, Locale locale){
    	UtrDispatchForm utrDispatchForm = new UtrDispatchForm();
    	Calendar cal = Calendar.getInstance();
    	utrDispatchForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
    	cal.add(Calendar.MONTH, -1);
    	utrDispatchForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
    	utrDispatchForm.setCritical(0);
    	utrDispatchForm.setStop(0);
    	model.addAttribute("utrDispatchForm", utrDispatchForm);
    	Map<String, X3UtrMachine> machines = x3Service.findAllUtrMachines("ATW");
    	Map<String, X3UtrWorker> workers = x3Service.findAllUtrWorkers("ATW");
    	for (Map.Entry<String, X3UtrMachine> entry : machines.entrySet()) {
    	  //System.out.println("key: " + entry.getKey() + " object: " + entry.getValue());
    	}
    	for (Map.Entry<String, X3UtrWorker> entry : workers.entrySet()) {
      	  System.out.println("key: " + entry.getKey() + " object: " + entry.getValue());
      	}
    	return "utr/stats";
    }
    
    
	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	public String performSearch(@Valid UtrDispatchForm utrDispatchForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Locale locale){
		if(bindingResult.hasErrors()){
			return "utr/stats";
		}
		Date date = null;
		if(date == null){
			bindingResult.rejectValue("startDate", "error.date", "ERROR");
			return "geolook/search";
		}
		//redirectAttrs.addFlashAttribute("geodeList", jdbcOracleGeodeService.findLocationsOfProduct(geodeSearchForm.getProduct()));
		return "redirect:/utr/stats";
	}
    
    
    
}

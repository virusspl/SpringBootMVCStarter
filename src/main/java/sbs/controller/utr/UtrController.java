package sbs.controller.utr;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("utr")
public class UtrController {
	
	@Autowired
	MessageSource messageSource;
	
	List<String> excludedMachines;
	
    @Autowired
    public UtrController(Environment env) {
    	excludedMachines = Arrays.asList(env.getRequiredProperty("utr.machines.mtbfexcluded").split(";"));
    }
	
    @RequestMapping("/dispatch")
    public String view(Model model, Locale locale){
    	UtrDispatchForm utrDispatchForm = new UtrDispatchForm();
    	Calendar cal = Calendar.getInstance();
    	utrDispatchForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
    	cal.add(Calendar.MONTH, -1);
    	utrDispatchForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
    	utrDispatchForm.setCritical(0);
    	utrDispatchForm.setStop(0);
    	model.addAttribute("utrDispatchForm", utrDispatchForm);
    	return "utr/dispatch";
    }
    
    @RequestMapping("/crashes")
    public String viewActualCrashes(Model model, Locale locale){
    	UtrDispatchForm utrDispatchForm = new UtrDispatchForm();
    	Calendar cal = Calendar.getInstance();
    	utrDispatchForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
    	cal.add(Calendar.MONTH, -1);
    	utrDispatchForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
    	utrDispatchForm.setCritical(0);
    	utrDispatchForm.setStop(0);
    	model.addAttribute("utrDispatchForm", utrDispatchForm);
    	return "utr/crashes";
    }
    
    
}

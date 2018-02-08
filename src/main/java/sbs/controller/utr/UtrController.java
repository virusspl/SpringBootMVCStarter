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
import sbs.helpers.X3OrmHelper;
import sbs.model.x3.X3UtrFault;
import sbs.model.x3.X3UtrFaultLine;
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
	@Autowired
	X3OrmHelper x3OrmHelper;
	
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
    	return "utr/stats";
    }
    
    
	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	public String performSearch(@Valid UtrDispatchForm utrDispatchForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Locale locale){
		
		if(bindingResult.hasErrors()){
			return "utr/stats";
		}
		
		Date startDate = utrDispatchForm.getStartDate();
		Date endDate = utrDispatchForm.getEndDate();
		
		Map<String, X3UtrMachine> machines = x3Service.findAllUtrMachines("ATW");
    	Map<String, X3UtrWorker> workers = x3Service.findAllUtrWorkers("ATW");
    	Map<String, X3UtrFault> faults = x3Service.findUtrFaultsInPeriod(startDate, endDate);
    	List<X3UtrFaultLine> lines = x3Service.findUtrFaultLinesAfterDate(startDate);
    	x3OrmHelper.fillUtrFaultsLinks(faults, lines, workers, machines);
    	
    	int machinesCnt = 0;
    	int workersCnt = 0; 
    	int faultsCnt = 0;
    	
    	for (Map.Entry<String, X3UtrMachine> entry : machines.entrySet()) {
    		machinesCnt++;
    		//System.out.println("key: " + entry.getKey() + " object: " + entry.getValue());
    	}
    	for (Map.Entry<String, X3UtrWorker> entry : workers.entrySet()) {
    		workersCnt++;
    		//System.out.println("key: " + entry.getKey() + " object: " + entry.getValue());
      	}
    	for (Map.Entry<String, X3UtrFault> entry : faults.entrySet()) {
    		faultsCnt++;
    		//System.out.println("key: " + entry.getKey() + " object: " + entry.getValue());
    		System.out.println(entry.getValue());
      	}
    	
    	
    	
    	System.out.println("period: " + startDate + " - " + endDate);
    	System.out.println("workers: " + workersCnt + "; machines: " + machinesCnt +"; faults: " + faultsCnt);
		
    	return "utr/stats";
	}
    
    
    
}

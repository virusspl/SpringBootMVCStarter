package sbs.controller.utr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
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
import sbs.helpers.DateHelper;
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
	@Autowired
	DateHelper dateHelper;
	
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
	public String performSearch(@Valid UtrDispatchForm utrDispatchForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Model model, Locale locale){
		
		if(bindingResult.hasErrors()){
			return "utr/stats";
		}
		
		Date startDate = utrDispatchForm.getStartDate();
		Date endDate = utrDispatchForm.getEndDate();

		// get dictionaries
		Map<String, X3UtrMachine> machines = x3Service.findAllUtrMachines("ATW");
    	Map<String, X3UtrWorker> workers = x3Service.findAllUtrWorkers("ATW");
    	Map<String, X3UtrFault> faults = x3Service.findUtrFaultsInPeriod(startDate, endDate);
    	List<X3UtrFaultLine> lines = x3Service.findUtrFaultLinesAfterDate(startDate);
    	// link
    	x3OrmHelper.fillUtrFaultsLinks(faults, lines, workers, machines);
    	// remove excluded machines
    	for(String machine: excludedMachines){
    		machines.remove(machine);
    	}
    	// go through faults and get indicators
    
    	/*
    	int machinesCnt = 0;
    	int workersCnt = 0; 
    	for (Map.Entry<String, X3UtrMachine> entry : machines.entrySet()) {
    		machinesCnt++;
    		//System.out.println("key: " + entry.getKey() + " object: " + entry.getValue());
    	}
    	for (Map.Entry<String, X3UtrWorker> entry : workers.entrySet()) {
    		workersCnt++;
    		//System.out.println("key: " + entry.getKey() + " object: " + entry.getValue());
      	}
      	System.out.println("workers: " + workersCnt + "; machines: " + machinesCnt +"; faults: " + faultsCnt);
      	*/
    	
    	int minutesDurationTotal = 0;
    	int minutesReactionTotal = 0;
    	int minutesOfWorkTotal = 0;
    	
    	// remove faults by criteria
    	removeFaultsByCriteria(utrDispatchForm.getCritical(), utrDispatchForm.getStop(), faults);

    	// count
    	for (X3UtrFault fault : faults.values()) {
    		// MTTR
			minutesDurationTotal += fault.getFaultDurationInMinutes();
			// MRT
			minutesReactionTotal += fault.getFirstReactionTimeInMinutes();
			// MWT
			minutesOfWorkTotal += fault.getTotalWorkTimeInMinutes();
    		/*System.out.println(fault.getFaultNumber());
    		System.out.println(fault.getLines());
    		System.out.println(fault.getInputDateTime() + " - " + fault.getCloseDateTime());
    		System.out.println(fault.getFaultDurationInMinutes());
    		System.out.println("minutes total: " + minutesTotal);
    		System.out.println("hours: " + fault.getFaultDurationInMinutes()/60 + " minutes: " + fault.getFaultDurationInMinutes()%60);
    		System.out.println("=======================");
    		*/
      	}
    	
    	if(faults.size()==0){
    		return "utr/stats";
    	}
    	
    	// calculate
    	int mttr = minutesDurationTotal/faults.size();
    	int mrt = minutesReactionTotal/faults.size();
    	int mwt = minutesOfWorkTotal/faults.size();
    	
    	// pass values to view
    	model.addAttribute("faultsCounter", faults.size());
    	model.addAttribute("mttr", dateHelper.convertMinutesToHours(mttr));
    	model.addAttribute("mrt", dateHelper.convertMinutesToHours(mrt));
    	model.addAttribute("mwt", dateHelper.convertMinutesToHours(mwt));

    	return "utr/stats";
	}
	
	private void removeFaultsByCriteria(int critical, int stop, Map<String, X3UtrFault> faults){
		ArrayList<String> toDelete = new ArrayList<>();
		for (Map.Entry<String, X3UtrFault> entry : faults.entrySet()) {
			try {
				// normal
				if (critical == 1) {
					if (entry.getValue().getMachine().isCritical()) {
						toDelete.add(entry.getKey());
					}
				}
				// critical
				if (critical == 2) {
					if (!entry.getValue().getMachine().isCritical()) {
						toDelete.add(entry.getKey());
					}
				}
				// no stop
				if (stop == 1) {
					if (entry.getValue().getFaultType() != 1) {
						toDelete.add(entry.getKey());
					}
				}				
				// stop
				if (stop == 2) {
					if (entry.getValue().getFaultType() != 2) {
						toDelete.add(entry.getKey());
					}
				}
			} catch (Exception ex) {
				toDelete.add(entry.getKey());
			}
		}
		for(String key: toDelete){
			faults.remove(key);
		}
	}

}

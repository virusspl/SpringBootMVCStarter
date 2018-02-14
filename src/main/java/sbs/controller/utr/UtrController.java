package sbs.controller.utr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

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
	int criticalMachinesCnt;
	int nonCriticalMachinesCnt;

	@Autowired
	public UtrController(Environment env) {
		excludedMachines = Arrays.asList(env.getRequiredProperty("utr.machines.mtbfexcluded").split(";"));
		criticalMachinesCnt = Integer.parseInt(env.getRequiredProperty("utr.machines.criticalCnt"));
		nonCriticalMachinesCnt = Integer.parseInt(env.getRequiredProperty("utr.machines.nonCriticalCnt"));
	}

	@RequestMapping("/stats")
	public String view(Model model, Locale locale) {
		UtrDispatchForm utrDispatchForm = new UtrDispatchForm();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		utrDispatchForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
		cal.add(Calendar.MONTH, -1);
		utrDispatchForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
		utrDispatchForm.setCritical(0);
		utrDispatchForm.setStop(0);
		model.addAttribute("utrDispatchForm", utrDispatchForm);

		return "utr/stats";
	}

	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	public String performSearch(@Valid UtrDispatchForm utrDispatchForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Model model, Locale locale) {

		if (bindingResult.hasErrors()) {
			return "utr/stats";
		}

		// dates range
		Date startDate = utrDispatchForm.getStartDate();
		Date endDate = utrDispatchForm.getEndDate();

		// get dictionaries
		Map<String, X3UtrMachine> machines = x3Service.findAllUtrMachines("ATW");
		Map<String, X3UtrWorker> workersUnsorted = x3Service.findAllUtrWorkers("ATW");
		Map<String, X3UtrWorker> workers = new TreeMap<String, X3UtrWorker>(workersUnsorted);
		Map<String, X3UtrFault> faults = x3Service.findUtrFaultsInPeriod(startDate, endDate);
		List<X3UtrFaultLine> lines = x3Service.findUtrFaultLinesAfterDate(startDate);
		Map<String, Integer> workersTimeInMinutes = new HashMap<>();

		// initialize workers time table
		for (X3UtrWorker worker : workers.values()) {
			workersTimeInMinutes.put(worker.getCode(), 0);
		}

		// link maps
		x3OrmHelper.fillUtrFaultsLinks(faults, lines, workers, machines);

		// remove excluded machines
		for (String machine : excludedMachines) {
			machines.remove(machine);
		}

		// initialize counters
		int minutesDurationTotal = 0;
		int minutesReactionTotal = 0;
		int minutesOfWorkTotal = 0;
		int validFaultsCnt = 0;

		// remove faults by criteria
		removeFaultsByCriteria(utrDispatchForm.getCritical(), utrDispatchForm.getStop(), faults);

		// count
		for (X3UtrFault fault : faults.values()) {
			if (machines.get(fault.getMachineCode()) == null) {
				continue;
			}
			// count faults
			validFaultsCnt++;
			// MTTR
			minutesDurationTotal += fault.getFaultDurationInMinutes();
			// MRT
			minutesReactionTotal += fault.getFirstReactionTimeInMinutes();
			// MWT
			minutesOfWorkTotal += fault.getTotalWorkTimeInMinutes();
			// workers time
			countWorkersTime(workersTimeInMinutes, fault.getLines());
		}
		
		// if no faults - exit
		if (faults.size() == 0) {
			return "utr/stats";
		}
		
		// fill workers map with work time
		for (Map.Entry<String, Integer> entry : workersTimeInMinutes.entrySet()) {
			if(!workers.containsKey(entry.getKey())){
				continue;
			}
			workers.get(entry.getKey()).setWorkTimeInHours(dateHelper.convertMinutesToHours(entry.getValue()));
		}

		// calculate indicators
		int mttr = minutesDurationTotal / validFaultsCnt;
		int mrt = minutesReactionTotal / validFaultsCnt;
		int mwt = minutesOfWorkTotal / validFaultsCnt;
		int daysInPeriod = countDaysInPeriod(startDate, endDate);
		int mtbf = calculateMtbf(daysInPeriod, validFaultsCnt, utrDispatchForm.getCritical(), criticalMachinesCnt,
				nonCriticalMachinesCnt);

		// pass values to view
		model.addAttribute("faultsCounter", validFaultsCnt);
		model.addAttribute("daysInPeriod", daysInPeriod);
		model.addAttribute("mttr", dateHelper.convertMinutesToHours(mttr));
		model.addAttribute("mrt", dateHelper.convertMinutesToHours(mrt));
		model.addAttribute("mwt", dateHelper.convertMinutesToHours(mwt));
		model.addAttribute("criticalCnt", criticalMachinesCnt);
		model.addAttribute("nonCriticalCnt", nonCriticalMachinesCnt);
		model.addAttribute("mtbf", mtbf);
		model.addAttribute("workers", workers.values());
		
		return "utr/stats";
	}

	/**
	 * count workers time into a map, based on fault lines
	 * @param workersTimeInMinutes
	 * @param lines
	 */
	private void countWorkersTime(Map<String, Integer> workersTimeInMinutes, ArrayList<X3UtrFaultLine> lines) {
		int value;
		String code;
		for (X3UtrFaultLine line : lines) {
			if (line.getUtrWorkerCode() != null) {
				value = 0;
				code = line.getUtrWorkerCode();
				if (workersTimeInMinutes.containsKey(code)) {
					value = workersTimeInMinutes.get(code);
					value += line.getClosedLineDurationInMinutes();
				} 
				workersTimeInMinutes.put(line.getUtrWorkerCode(), value);
			}
		}
	}

	/**
	 * calculate MTBF for parameters
	 * @param daysInPeriod
	 * @param faultsCnt
	 * @param criticalCriteria
	 * @param criticalMachinesCnt
	 * @param nonCriticalMachinesCnt
	 * @return integer, MTBF in hours
	 */
	private int calculateMtbf(int daysInPeriod, int faultsCnt, int criticalCriteria, int criticalMachinesCnt,
			int nonCriticalMachinesCnt) {
		int machinesCnt;
		switch (criticalCriteria) {
		case 1:
			machinesCnt = nonCriticalMachinesCnt;
			break;
		case 2:
			machinesCnt = criticalMachinesCnt;
			break;
		default:
			machinesCnt = criticalMachinesCnt + nonCriticalMachinesCnt;
			break;
		}

		return (int) ((daysInPeriod * 1.0 / faultsCnt) * 12 * machinesCnt);
	}

	/**
	 * count number of days in period
	 * @param startDate
	 * @param endDate
	 * @return difference in days
	 */
	private int countDaysInPeriod(Date startDate, Date endDate) {
		return ((int) TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS)) + 1;
	}

	/**
	 * remove faults in given map by criteria
	 * @param critical based on maintenance form, 0 all, 1 normal, 2 critical
	 * @param stop base on maintenance form, 0 all, 1 faults, 2 crashes
	 * @param faults map
	 */
	private void removeFaultsByCriteria(int critical, int stop, Map<String, X3UtrFault> faults) {
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
		for (String key : toDelete) {
			faults.remove(key);
		}
	}

	/**
	 * count machines by type
	 * @param machines
	 * @param type
	 * @return integer quantity
	 */
	@SuppressWarnings("unused")
	private int countMachinesByType(Collection<X3UtrMachine> machines, boolean type) {
		int counter = 0;
		for (X3UtrMachine machine : machines) {
			if (type == machine.isCritical()) {
				counter++;
			}
		}
		return counter;
	}

}

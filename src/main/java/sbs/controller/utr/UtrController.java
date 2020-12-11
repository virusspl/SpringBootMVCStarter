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
import org.springframework.web.bind.annotation.RequestParam;
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

	public static final int MACHINE_BODIES = 1;
	public static final int MACHINE_ARMS = 2;
	public static final int MACHINE_EXPANDERS = 3;

	List<String> excludedMachines;
	List<String> bodiesMachines;
	List<String> armsMachines;

	int hoursInShift;

	@Autowired
	public UtrController(Environment env) {
		excludedMachines = Arrays.asList(env.getRequiredProperty("utr.machines.mtbfexcluded").split(";"));
		bodiesMachines = Arrays.asList(env.getRequiredProperty("utr.machineschart.bodies").split(";"));
		armsMachines = Arrays.asList(env.getRequiredProperty("utr.machineschart.arms").split(";"));
		hoursInShift = Integer.parseInt(env.getRequiredProperty("utr.hoursInShift"));
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

	@RequestMapping(value = "/stats", params = { "stats" }, method = RequestMethod.POST)
	public String performSearch(@Valid UtrDispatchForm utrDispatchForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Model model, Locale locale, @RequestParam(value = "stats") int company) {

		if (bindingResult.hasErrors()) {
			return "utr/stats";
		}

		// dates range
		Date startDate = utrDispatchForm.getStartDate();
		Date endDate = utrDispatchForm.getEndDate();

		// get dictionaries
		Map<String, X3UtrMachine> machines = x3Service.findAllUtrMachines("ATW", company);
		Map<String, X3UtrWorker> workersUnsorted = x3Service.findAllUtrWorkers("ATW");
		Map<String, X3UtrWorker> workers = new TreeMap<String, X3UtrWorker>(workersUnsorted);
		Map<String, X3UtrFault> faults = x3Service.findUtrFaultsInPeriod(startDate, endDate);
		List<X3UtrFaultLine> lines = x3Service.findUtrFaultLinesAfterDate(startDate);
		Map<String, Integer> workersTimeInMinutes = new HashMap<>();
		ArrayList<X3UtrMachine> criticalMachines = new ArrayList<>();

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

		// make critical machines list
		// and count critical + non-critical
		int nonCriticalMachinesCnt = 0;
		int criticalMachinesCnt = 0;
		for (X3UtrMachine machine : machines.values()) {
			if (machine.isCritical()) {
				criticalMachinesCnt++;
				criticalMachines.add(machine);
			} else {
				nonCriticalMachinesCnt++;
			}
		}

		// initialize counters
		int minutesDurationTotal = 0;
		int minutesReactionTotal = 0;
		int minutesOfWorkTotal = 0;
		int validFaultsCnt = 0;

		// remove faults by criteria
		removeFaultsByCriteria(utrDispatchForm.getCritical(), utrDispatchForm.getStop(), faults);

		// count
		String tmpMachineCode;
		for (X3UtrFault fault : faults.values()) {
			tmpMachineCode = fault.getMachineCode();
			if (machines.get(tmpMachineCode) == null) {
				continue;
			}
			// 1 = ADRP; 2 = WPS - in view
			if (machines.get(tmpMachineCode).getCompany() != company) {
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
			if (!workers.containsKey(entry.getKey())) {
				continue;
			}
			workers.get(entry.getKey()).setWorkTimeInHours(dateHelper.convertMinutesToHours(entry.getValue()));
		}

		// calculate indicators
		int mttr = minutesDurationTotal / validFaultsCnt;
		int mrt = minutesReactionTotal / validFaultsCnt;
		int mwt = minutesOfWorkTotal / validFaultsCnt;
		int daysInPeriod = countDaysInPeriod(startDate, endDate);
		int mtbf = 0;
		mtbf = calculateMtbf(daysInPeriod, validFaultsCnt, utrDispatchForm.getCritical(), criticalMachinesCnt,
				nonCriticalMachinesCnt);

		// pass values to view
		model.addAttribute("faultsCounter", validFaultsCnt);
		model.addAttribute("daysInPeriod", daysInPeriod);
		model.addAttribute("mttr", dateHelper.convertMinutesToHours(mttr));
		model.addAttribute("mrt", dateHelper.convertMinutesToHours(mrt));
		model.addAttribute("mwt", dateHelper.convertMinutesToHours(mwt));
		model.addAttribute("criticalMachinesCnt", criticalMachinesCnt);
		model.addAttribute("nonCriticalMachinesCnt", nonCriticalMachinesCnt);
		model.addAttribute("mtbf", mtbf);
		model.addAttribute("workers", workers.values());
		model.addAttribute("criticalMachines", criticalMachines);
		model.addAttribute("company", company == 1 ? "ADRP" : "WPS");

		return "utr/stats";
	}

	@RequestMapping(value = "/stats", params = { "machines" }, method = RequestMethod.POST)
	public String machinesState(@Valid UtrDispatchForm utrDispatchForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Model model, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "utr/stats";
		}

		final String NORMAL_COLOR = "#f5f5f5";
		final String SATURDAY_COLOR = "#ffffcc";
		final String SUNDAY_COLOR = "#ffdddd";

		// get machines ADR + WPS
		Map<String, X3UtrMachine> machines = x3Service.findAllUtrMachines("ATW", 1);
		Map<String, X3UtrMachine> machinesWPS = x3Service.findAllUtrMachines("ATW", 2);
		for (Map.Entry<String, X3UtrMachine> machine : machinesWPS.entrySet()) {
			machines.put(machine.getKey(), machine.getValue());
		}

		Map<String, X3UtrFault> faults = x3Service.findAllUtrFaults();
		List<X3UtrFaultLine> lines = x3Service.findAllUtrFaultLines();
		// link maps
		x3OrmHelper.assignUtrFaultsLines(faults, lines, machines);

		// dates range
		Calendar start = Calendar.getInstance();
		start.setTime(utrDispatchForm.getStartDate());
		Calendar end = Calendar.getInstance();
		end.setTime(utrDispatchForm.getEndDate());

		// count period
		int periodLength = countDaysInPeriod(utrDispatchForm.getStartDate(), utrDispatchForm.getEndDate());

		// create titles
		String[] datesInPeriod = new String[periodLength];
		String[] colorsInPeriod = new String[periodLength];
		Calendar cal = Calendar.getInstance();
		cal.setTime(utrDispatchForm.getStartDate());
		for (int i = 0; i < periodLength; i++) {
			datesInPeriod[i] = dateHelper.formatDdMmYy(cal.getTime());
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				colorsInPeriod[i] = SATURDAY_COLOR;
			} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				colorsInPeriod[i] = SUNDAY_COLOR;
			} else {
				colorsInPeriod[i] = NORMAL_COLOR;
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}

		// create faults list in period
		ArrayList<X3UtrFault> periodFaults = getFaultsInPeriod(faults, start, end);

		// create empty lines
		int linesCount = 0;
		ArrayList<ChartLine> bodiesLines = new ArrayList<>();
		ArrayList<ChartLine> armsLines = new ArrayList<>();
		ArrayList<ChartLine> expandersLines = new ArrayList<>();
		ArrayList<ChartLine> wpsLines = new ArrayList<>();
		ChartLine line;

		for (X3UtrMachine m : machines.values()) {
			if (m.getCompany() == X3UtrMachine.ADRP) {

				if (m.getCodeNicim() == null) {
					// if NICIM code empty - skip
					continue;
				}
				switch (getMachineTypeByNicimCode(m.getCodeNicim())) {
				case MACHINE_BODIES:
					line = new ChartLine(m, periodLength);
					calculateTimeAvailable(line, periodFaults, start, end);
					linesCount++;
					bodiesLines.add(line);
					break;
				case MACHINE_ARMS:
					line = new ChartLine(m, periodLength);
					calculateTimeAvailable(line, periodFaults, start, end);
					linesCount++;
					armsLines.add(line);
					break;
				case MACHINE_EXPANDERS:
					line = new ChartLine(m, periodLength);
					calculateTimeAvailable(line, periodFaults, start, end);
					linesCount++;
					expandersLines.add(line);
					break;
				default:
					break;
				}

			} else if (m.getCompany() == X3UtrMachine.WPS) {
				line = new ChartLine(m, periodLength);
				calculateTimeAvailable(line, periodFaults, start, end);
				linesCount++;
				wpsLines.add(line);
			}

		}

		model.addAttribute("titleDates", datesInPeriod);
		model.addAttribute("titleColors", colorsInPeriod);
		model.addAttribute("periodLength", periodLength);
		model.addAttribute("linesCount", linesCount);
		model.addAttribute("bodiesLines", bodiesLines);
		model.addAttribute("armsLines", armsLines);
		model.addAttribute("expandersLines", expandersLines);
		model.addAttribute("wpsLines", wpsLines);

		return "utr/stats";
	}

	private int getMachineTypeByNicimCode(String machineCode) {
		if (machineCode.startsWith("ZGT")) {
			return MACHINE_EXPANDERS;
		}
		if (bodiesMachines.contains(machineCode)) {
			return MACHINE_BODIES;
		}
		if (armsMachines.contains(machineCode)) {
			return MACHINE_ARMS;
		}

		return 0;
	}

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public String currentFaultsMonitor(Model model, Locale locale) {

		// dates
		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		cal.add(Calendar.YEAR, -1);
		Date startDate = cal.getTime();

		// get dictionaries
		// machines ADR + WPS
		Map<String, X3UtrMachine> machines = x3Service.findAllUtrMachines("ATW", 1);
		Map<String, X3UtrMachine> machinesWPS = x3Service.findAllUtrMachines("ATW", 2);
		for (Map.Entry<String, X3UtrMachine> machine : machinesWPS.entrySet()) {
			machines.put(machine.getKey(), machine.getValue());
		}
		Map<String, X3UtrFault> faults = x3Service.findUtrFaultsInPeriod(startDate, endDate);
		List<X3UtrFaultLine> lines = x3Service.findUtrFaultLinesAfterDate(startDate);

		// link maps
		x3OrmHelper.assignUtrFaultsLines(faults, lines, machines);

		// date
		Calendar today = Calendar.getInstance();
		Map<String, CurrentFaultsLine> list = new TreeMap<>();

		CurrentFaultsLine cfl;
		for (X3UtrFault fault : faults.values()) {
			// only critical machines
			if (fault.getMachine() == null || !fault.getMachine().isCritical()) {
				continue;
			}
			if (fault.getInputDateTime() == null) {
				continue;
			}
			if (fault.getFaultType() == X3UtrFault.NOSTOP_TYPE) {
				continue;
			}
			if (dateHelper.isDateInRange(new Timestamp(today.getTime().getTime()), fault.getInputDateTime(),
					fault.getCloseDateTime())) {
				if (!list.containsKey(fault.getMachineCode())) {
					cfl = new CurrentFaultsLine();
					cfl.setInputDate(fault.getInputDateTime());
					cfl.setMachine(fault.getMachine());
					cfl.setFaults(fault.getFaultNumber());
					list.put(fault.getMachineCode(), cfl);
				} else {
					list.get(fault.getMachineCode()).setFaults(fault.getFaultNumber());
				}
			}
		}

		model.addAttribute("list", list.values());
		model.addAttribute("refreshTime", dateHelper.formatDdMmYyyyHhMm(dateHelper.getCurrentTime()));

		return "utr/current";
	}

	private ArrayList<X3UtrFault> getFaultsInPeriod(Map<String, X3UtrFault> list, Calendar start, Calendar end) {
		ArrayList<X3UtrFault> faults = new ArrayList<>();
		Calendar inputDate = Calendar.getInstance();
		Calendar closeDate = Calendar.getInstance();
		for (X3UtrFault ft : list.values()) {
			if (ft.getInputDateTime() == null) {
				// no start date - skip
				continue;
			}
			// get start date
			inputDate.setTime(new Date(ft.getInputDateTime().getTime()));
			if (ft.getCloseDateTime() == null) {
				// no close date - set future +100 years
				closeDate = Calendar.getInstance();
				closeDate.add(Calendar.YEAR, 100);
			}
			// get close date
			closeDate.setTime(new Date(ft.getCloseDateTime().getTime()));

			// if fault is in range:
			if (dateHelper.dateInRange(inputDate, start, end) || dateHelper.dateInRange(closeDate, start, end)) {
				faults.add(ft);
			}
			// is range inside faults period:
			if (dateHelper.dateBeforeOrEqual(inputDate, start) && dateHelper.dateAfterOrEqual(closeDate, end)) {
				faults.add(ft);
			}
		}
		return faults;

	}

	private void calculateTimeAvailable(ChartLine line, ArrayList<X3UtrFault> faults, Calendar start, Calendar end) {

		Calendar inputDate = Calendar.getInstance();
		Calendar closeDate = Calendar.getInstance();
		int periodLength = countDaysInPeriod(start.getTime(), end.getTime());
		ArrayList<Integer> minutesInDays = new ArrayList<>();
		for (int i = 0; i < periodLength; i++) {
			minutesInDays.add(0);
		}

		// find current machine's faults
		for (X3UtrFault ft : faults) {
			// no machine / wrong machine - skip
			if (ft.getMachineCode().equals(null) || !ft.getMachineCode().equals(line.getMachine().getCode())) {
				continue;
			}
			// no input date - skip
			if (ft.getInputDateTime() == null) {
				continue;
			}
			// INPUT AND CLOSE DATE
			inputDate.setTime(new Date(ft.getInputDateTime().getTime()));
			if (ft.getCloseDateTime() == null) {
				// no close date - set future +100 years
				closeDate = Calendar.getInstance();
				closeDate.add(Calendar.YEAR, 100);
			}
			// get close date
			closeDate.setTime(new Date(ft.getCloseDateTime().getTime()));

			// for each day decrease quantity
			Calendar currentDay = Calendar.getInstance();
			currentDay.setTime(start.getTime());
			for (int i = 0; i < periodLength; i++) {
				if (calculateStopMinutesInDay(currentDay, inputDate, closeDate) > 0) {
					if (line.getFaults()[i] != null) {
						line.setFaultAt(i, line.getFaults()[i] + "; " + ft.getFaultNumber());
					} else {
						line.setFaultAt(i, ft.getFaultNumber());
					}
				}
				minutesInDays.set(i,
						minutesInDays.get(i) + calculateStopMinutesInDay(currentDay, inputDate, closeDate));
				currentDay.add(Calendar.DAY_OF_YEAR, 1);
			}
		}

		// format result
		for (int i = 0; i < line.getValues().length; i++) {
			// 1440 min = 24 hours
			if (minutesInDays.get(i) > 1440) {
				minutesInDays.set(i, 1440);
			}
			line.setValueAt(i, dateHelper.convertMinutesToHours(1440 - minutesInDays.get(i)));
		}

	}

	private int calculateStopMinutesInDay(Calendar currentDay, Calendar inputDate, Calendar closeDate) {
		// day range
		Calendar currStart = Calendar.getInstance();
		currStart.setTime(currentDay.getTime());
		currStart.set(Calendar.HOUR_OF_DAY, 0);
		currStart.set(Calendar.MINUTE, 0);
		Calendar currEnd = Calendar.getInstance();
		currEnd.setTime(currStart.getTime());
		currEnd.set(Calendar.HOUR_OF_DAY, 24);
		currEnd.set(Calendar.MINUTE, 0);

		long diff;

		if (dateHelper.dateBeforeOrEqual(inputDate, currStart) && dateHelper.dateAfterOrEqual(closeDate, currEnd)) {
			return 1440;
		} else if (inputDate.before(currStart) && dateHelper.dateInRange(closeDate, currStart, currEnd)) {
			diff = closeDate.getTime().getTime() - currStart.getTime().getTime();
			return (int) diff / 1000 / 60;
		} else if (closeDate.after(currEnd) && dateHelper.dateInRange(inputDate, currStart, currEnd)) {
			diff = currEnd.getTime().getTime() - inputDate.getTime().getTime();
			return (int) diff / 1000 / 60;
		} else if (dateHelper.dateInRange(inputDate, currStart, currEnd)
				&& dateHelper.dateInRange(closeDate, currStart, currEnd)) {
			diff = closeDate.getTime().getTime() - inputDate.getTime().getTime();
			return (int) diff / 1000 / 60;
		}

		return 0;

	}

	/**
	 * count workers time into a map, based on fault lines
	 * 
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
	 * 
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
		return (int) ((daysInPeriod * 1.0 / faultsCnt) * hoursInShift * machinesCnt);
	}

	/**
	 * count number of days in period
	 * 
	 * @param startDate
	 * @param endDate
	 * @return difference in days
	 */
	private int countDaysInPeriod(Date startDate, Date endDate) {
		return ((int) TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS)) + 1;
	}

	/**
	 * remove faults in given map by criteria
	 * 
	 * @param critical based on maintenance form, 0 all, 1 normal, 2 critical
	 * @param stop     base on maintenance form, 0 all, 1 faults, 2 crashes
	 * @param faults   map
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
	 * 
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

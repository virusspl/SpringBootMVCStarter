package sbs.controller.capacity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.DateHelper;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("/capacity")
public class CapacityController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service jdbcOracleX3Service;
	@Autowired
	DateHelper dateHelper;

	@RequestMapping("/main")
	public String search(Model model) {
		model.addAttribute("date", dateHelper.formatDdMmYyyyDot(new Date()));
		return "capacity/main";
	}

	@RequestMapping("/exec")
	public String doMakePlan(Model model, Locale locale, RedirectAttributes redirectAttrs) throws Exception {

		File file = (File) model.asMap().get("file");
		if (file == null) {
			// no file
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("action.choose.file", null, locale));
			return "redirect:/capacity/main";
		}

		Date date = (Date) model.asMap().get("date");
		if (date == null) {
			// no date
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("general.date", null, locale) + "!");
			return "redirect:/capacity/main";
		}

		List<CapacityLine> input = new ArrayList<>();

		// file exist
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			String[] split;

			int lineNo = 0;
			CapacityLine capacityLine;

			while ((line = br.readLine()) != null) {
				lineNo++;
				split = line.split(";");
				// structure
				if (split.length != 10) {
					redirectAttrs.addFlashAttribute("error",
							messageSource.getMessage("error.bad.file.structure", null, locale) + ". "
									+ messageSource.getMessage("general.line", null, locale) + " " + lineNo + ": "
									+ line);
					br.close();
					file.delete();
					return "redirect:/capacity/main";
				}
				// first line with titles
				if (lineNo == 1) {
					continue;
				}
				// values
				try {
					capacityLine = new CapacityLine();
					capacityLine.setOrder(split[0].toUpperCase().trim());
					capacityLine.setCode(split[1].toUpperCase().trim());
					capacityLine.setDescription(split[2].toUpperCase().trim());
					capacityLine.setOperation(Math.abs(Integer.parseInt(split[3])));
					capacityLine.setMachine(split[4].toUpperCase().trim());
					capacityLine.setQuantity(Math.abs(Integer.parseInt(split[5])));
					capacityLine.setQuantityRejected(Math.abs(Integer.parseInt(split[6])));
					capacityLine.setNextOperation(Math.abs(Integer.parseInt(split[7])));
					capacityLine.setGroup(split[8].toUpperCase().trim());
					capacityLine.setOperationDescription(split[9].toUpperCase().trim());
					input.add(capacityLine);
				} catch (NumberFormatException ex) {
					redirectAttrs.addFlashAttribute("error",
							messageSource.getMessage("error.mustbeanumber", null, locale) + ". "
									+ messageSource.getMessage("general.line", null, locale) + " " + lineNo + ": "
									+ line);
					br.close();
					file.delete();
					return "redirect:/capacity/main";
				}
			}
			br.close();
			file.delete();
		}

		// extract assembly values
		Map<String, Integer> assemblyResult = getAssemblyMachinesResult(input);
		System.out.println("Assembly");
		System.out.println(assemblyResult);
		// extract welding values
		Map<String, Integer> weldingResult = getWeldingMachinesResult(input);
		System.out.println("Welding");
		System.out.println(weldingResult);
		// extract mechanical values
		Map<String, Integer> mechanicalResult = getMechanicalMachinesResult(input);
		System.out.println("Mechanical");
		System.out.println(mechanicalResult);

		// model.addAttribute("date", date);
		// model.addAttribute("list", input);
		return "capacity/view";
	}

	private Map<String, Integer> getMechanicalMachinesResult(List<CapacityLine> input) {
		Set<String> machines = new HashSet<>(Arrays.asList("LAS05", "LAS10", "LAS15", "LAS20"));

		Map<String, Integer> result = new TreeMap<>();
		this.fillMapIfSetContains(result, input, machines);
		result.put("ROZPIERAK", this.getMechanicalRozpierakResult(input));
		result.put("CZOP", this.getMechanicalCzopResult(input));
		result.put("BELKA", this.getMechanicalBelkaResult(input));

		return result;
	}

	private Integer getMechanicalBelkaResult(List<CapacityLine> input) {
		int tmpVal = 0;
		for (CapacityLine line : input) {
			if (line.getNextOperation() != 99999) {
				continue;
			}
			if (line.getDescription().startsWith("BEL")
					&& ((line.getMachine().startsWith("TOC") || line.getMachine().startsWith("WIR")))) {
				tmpVal += line.getQuantity();
			}
			else if(line.getMachine().startsWith("TOB") && line.getCode().startsWith("68")) {
				tmpVal += line.getQuantity();
			}
		}
		return tmpVal;
	}

	private Integer getMechanicalCzopResult(List<CapacityLine> input) {
		int tmpVal = 0;
		for (CapacityLine line : input) {
			if (line.getDescription().startsWith("CZOP") && line.getNextOperation() == 99999) {
				if (line.getMachine().startsWith("TOC") || line.getMachine().startsWith("TOB")
						|| line.getMachine().startsWith("WIR") || line.getMachine().startsWith("TOP")
						|| line.getMachine().startsWith("CTOC")) {
					tmpVal += line.getQuantity();
				}

			}
		}
		return tmpVal;
	}

	private Integer getMechanicalRozpierakResult(List<CapacityLine> input) {
		int tmpVal = 0;
		for (CapacityLine line : input) {
			if (line.getDescription().startsWith("ROZP") && line.getNextOperation() == 99999) {
				tmpVal += line.getQuantity();
			}
		}
		return tmpVal;
	}

	private Map<String, Integer> getAssemblyMachinesResult(List<CapacityLine> input) throws Exception {
		Set<String> machines = new HashSet<>(Arrays.asList("LSO11", "LSO15", "LSO16", "LSO21", "LSO27", "ROB50",
				"ROB80", "STS75", "STS15", "LMO05", "LMO10", "LMO15", "LMO20", "LMO30", "STM05", "STM10", "STM15",
				"STM20", "STM25", "STM50", "STM55", "STM60", "STM100", "MPW05", "ROB120"));

		Map<String, Integer> result = new TreeMap<>();
		this.fillMapIfSetContains(result, input, machines);
		result.put("LMO25", this.getAssemblyLmo25Result(input));

		return result;
	}

	private Map<String, Integer> getWeldingMachinesResult(List<CapacityLine> input) {
		// final machines:
		// machines = new HashSet<>(Arrays.asList("STS01", "STS25", "STS30", "STS35",
		// "STS40", "STS45", "STS50",
		// "STS55", "STS70", "STS85", "STS90", "SPA05", "SPA10", "SPA25", "SPA35",
		// "SPE05", "SPE10", "SPE15"));
		Set<String> machines = new HashSet<>(Arrays.asList("ROB05", "ROB10", "ROB15", "ROB16", "ROB20", "ROB35",
				"ROB40", "ROB45", "ROB60", "ROB65", "ROB70", "ROB75", "ROB85", "ROB100", "ROB130", "STM35", "STM36",
				"STM37", "STM40", "TXP10", "WYS10", "STS01", "STS25", "STS30", "STS35", "STS40", "STS45", "STS50",
				"STS55", "STS70", "STS85", "STS90", "SPA05", "SPA10", "SPA25", "SPA35", "SPE05", "SPE10", "SPE15"));

		Map<String, Integer> result = new TreeMap<>();
		this.fillMapIfSetContains(result, input, machines);
		result.put("G-ZAW", this.getWeldingGZawResult(input));
		result.put("K-ZEST", this.getWeldingKZestResult(input));

		return result;
	}

	private Integer getWeldingKZestResult(List<CapacityLine> input) {
		int tmpVal = 0;
		for (CapacityLine line : input) {
			if (line.getMachine().startsWith("LMB") && line.getNextOperation() == 99999
					&& (line.getGroup().equalsIgnoreCase("KZA") || line.getGroup().equalsIgnoreCase("KZG"))) {
				tmpVal += line.getQuantity();
			}
		}
		return tmpVal;
	}

	private Integer getWeldingGZawResult(List<CapacityLine> input) {
		int tmpVal = 0;
		for (CapacityLine line : input) {
			if (line.getMachine().startsWith("LMB") && line.getNextOperation() == 99999
					&& line.getGroup().equalsIgnoreCase("PZA")) {
				tmpVal += line.getQuantity();
			}
		}
		return tmpVal;
	}

	/**
	 * @param input    lines of production file
	 * @param machines machines to consider
	 */
	private void fillMapIfSetContains(Map<String, Integer> result, List<CapacityLine> input, Set<String> machines) {
		int tmpVal;
		for (CapacityLine line : input) {
			tmpVal = 0;
			if (machines.contains(line.getMachine())) {
				if (result.containsKey(line.getMachine())) {
					tmpVal = result.get(line.getMachine());
				}
				result.put(line.getMachine(), tmpVal + line.getQuantity());
			}
		}
		this.fillMapWithMissingKeysBySet(result, machines);
	}

	/**
	 * put zero-value keys from set into the map (if missing)
	 * 
	 * @param result   map to be filled
	 * @param machines set of machines
	 */
	private void fillMapWithMissingKeysBySet(Map<String, Integer> result, Set<String> machines) {
		for (String key : machines) {
			if (!result.containsKey(key)) {
				result.put(key, 0);
			}
		}
	}

	private int getAssemblyLmo25Result(List<CapacityLine> input) throws Exception {
		// get LMO code+operation Set from server file (file from Nicim query)
		Set<String> lmo25Set = this.getLmo25Map();
		int result = 0;
		for (CapacityLine line : input) {
			if (lmo25Set.contains(line.getCode() + line.getOperation())) {
				result += line.getQuantity();
			}
		}
		return result;
	}

	private Set<String> getLmo25Map() throws Exception {
		Set<String> set = new TreeSet<>();
		// file from Nicim query
		URL url = new URL("http://192.168.1.23/CAPACITY/LMO25/lmo25.csv");
		BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		String[] split;
		int lineNo = 0;

		while ((line = read.readLine()) != null) {
			lineNo++;
			;
			split = line.split(";");

			// lmo file has 3 columns
			if (split.length != 3) {
				throw new Exception("Bad lmo25 file structure! Line: " + lineNo);
			}
			set.add(split[0] + split[2]);
		}
		read.close();
		return set;
	}

}

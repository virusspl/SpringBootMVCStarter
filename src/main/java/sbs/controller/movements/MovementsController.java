package sbs.controller.movements;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.DateHelper;
import sbs.model.geode.GeodeMovement;
import sbs.model.x3.X3ProductFinalMachine;
import sbs.model.x3.X3RouteLine;
import sbs.model.x3.X3SalesOrderLine;
import sbs.model.x3.X3ShipmentMovement;
import sbs.model.x3.X3ShipmentStockLineWithPrice;
import sbs.model.x3.X3WarehouseWeightDetailLine;
import sbs.model.x3.X3WarehouseWeightLine;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.production.ProductionService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("movements")
public class MovementsController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleGeodeService geodeService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	DateHelper dateHelper;
	@Autowired
	ProductionService prodService;

	List<String> productionStores;
	List<String> receptionStores;
	List<String> shipmentStores;
	List<String> geodeLoadList;
	List<String> geodeUnloadList;

	@Autowired
	public MovementsController(Environment env) {
		productionStores = Arrays.asList(env.getRequiredProperty("warehouse.store.production").split(";"));
		receptionStores = Arrays.asList(env.getRequiredProperty("warehouse.store.receptions").split(";"));
		shipmentStores = Arrays.asList(env.getRequiredProperty("warehouse.store.shipments").split(";"));
		geodeLoadList = Arrays.asList(env.getRequiredProperty("geode.movements.load").split(";"));
		geodeUnloadList = Arrays.asList(env.getRequiredProperty("geode.movements.unload").split(";"));
	}

	@RequestMapping("/main")
	public String view(Model model, Locale locale,
			@CookieValue(value = "startDateMov", defaultValue = "0") String startDateLong,
			@CookieValue(value = "endDateMov", defaultValue = "0") String endDateLong) {
		
		
		MovementsForm movementsForm = new MovementsForm();
		if (startDateLong.length() > 1 && endDateLong.length() > 1) {
			movementsForm.setStartDate(new java.util.Date(Long.parseLong(startDateLong)));
			movementsForm.setEndDate(new java.util.Date(Long.parseLong(endDateLong)));
		}
		else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			movementsForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
			cal.add(Calendar.DAY_OF_MONTH, -6);
			movementsForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
		}
		
		model.addAttribute("movementsForm", movementsForm);

		return "movements/main";
	}

	@RequestMapping(value = "/calculate", params = { "shipmov" }, method = RequestMethod.POST)
	public String performSearch(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale, HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			return "movements/main";
		}
		
		// create a cookie
		Cookie startDateCookie = new Cookie("startDateMov", "" + movementsForm.getStartDate().getTime());
		Cookie endDateCookie = new Cookie("endDateMov", "" + movementsForm.getEndDate().getTime());
		// set time to live (seconds)
		startDateCookie.setMaxAge(60 * 60 * 24 * 31);
		endDateCookie.setMaxAge(60 * 60 * 24 * 31);
		// add cookie to response
		response.addCookie(startDateCookie);
		response.addCookie(endDateCookie);
		

		Map<String, Double> prices = x3Service.getCurrentStandardCostsMap("ATW");
		List<X3ShipmentMovement> movements = x3Service.findAdrShipmentMovementsInPeriod(movementsForm.getStartDate(),
				movementsForm.getEndDate());
		List<X3SalesOrderLine> orderLines = x3Service.findAdrSalesOrderLinesBasedOnShipmentMovementsInPeriod(movementsForm.getStartDate(),
				movementsForm.getEndDate());
		Map<String, X3SalesOrderLine> orderLinesMap = new HashMap<>();
		
		for(X3SalesOrderLine lin: orderLines) {
			orderLinesMap.put(lin.getOrderNumber()+";"+lin.getProductCode(), lin);
		}
		
		int counter = 0;
		double totalValue = 0;
		double mechanicalValue = 0;
		double weldingValue = 0;
		double assemblyValue = 0;
		double otherValue = 0;
		double warehouseValue = 0;
		double kal05Value = 0;
		double kal10Value = 0;
		LinkedHashSet<String> unassignedMachinesSet = new LinkedHashSet<>();
		String unassignedMachines = "";

		Map<String, X3ProductFinalMachine> machinesIndex = x3Service.findX3ProductFinalMachines("ATW");
		
		String tmpKey;
		X3RouteLine routeInt;
		
		for (X3ShipmentMovement mvt : movements) {
			if (prices.containsKey(mvt.getItemCode()) && !mvt.getItemCategory().equalsIgnoreCase("ACV")) {
				mvt.setPrice(prices.get(mvt.getItemCode()));
			} else {
				mvt.setPrice(mvt.getEmergencyAveragePrice());
			}
			mvt.setValue(mvt.getQuantity() * mvt.getPrice());
			if (mvt.getItemCategory().equals("ACV")) {
				warehouseValue += mvt.getValue();
			} else if (machinesIndex.containsKey(mvt.getItemCode())) {
				//only for 2 machines, don't break/continue:
				if (machinesIndex.get(mvt.getItemCode()).getMachineCode().equals("KAL05")) {
					kal05Value += mvt.getValue();
				}
				if(machinesIndex.get(mvt.getItemCode()).getMachineCode().equals("KAL10")){
					kal10Value += mvt.getValue();
				}
				
				switch (machinesIndex.get(mvt.getItemCode()).getMachineDepartment()) {
				case X3ProductFinalMachine.MECHANICAL:
					mechanicalValue += mvt.getValue();
					break;
				case X3ProductFinalMachine.WELDING:
					weldingValue += mvt.getValue();
					break;
				case X3ProductFinalMachine.ASSEMBLY:
					assemblyValue += mvt.getValue();
					break;
				default:
					otherValue += mvt.getValue();
					unassignedMachinesSet.add(machinesIndex.get(mvt.getItemCode()).getMachineCode());
					break;
				}
			} else {
				otherValue += mvt.getValue();
			}
			tmpKey = mvt.getDocument()+";"+mvt.getItemCode();
			mvt.setOrderDate(orderLinesMap.containsKey(tmpKey) ? orderLinesMap.get(tmpKey).getCreationDate():null);
			mvt.setDemandedDate(orderLinesMap.containsKey(tmpKey) ? orderLinesMap.get(tmpKey).getDemandedDate():null);
			mvt.setFinalClientCode(orderLinesMap.containsKey(tmpKey) ? orderLinesMap.get(tmpKey).getFinalClientCode():"");
			mvt.setFinalClientName(orderLinesMap.containsKey(tmpKey) ? orderLinesMap.get(tmpKey).getFinalClientName():"");

			routeInt = prodService.getLastRouteLineExcludingKAL(mvt.getItemCode(), "ATW");
			if (routeInt != null) {
				mvt.setLastMachineCode(routeInt.getMachine());
				mvt.setLastDepartmentCode(prodService.getMainDepartmentCodeByMachine(mvt.getLastMachineCode(), "ATW"));
			} else {
				mvt.setLastMachineCode("N/A");
				mvt.setLastDepartmentCode("general.na");
			}

			totalValue += mvt.getValue();
			counter++;
		}
		
		
		Map<String, String> gr2dict = x3Service.getVariousTableData("ATW","21",JdbcOracleX3Service.LANG_POLISH);

		model.addAttribute("gr2", gr2dict);
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("counter", counter);
		model.addAttribute("totalValue", totalValue);
		model.addAttribute("mechanicalValue", mechanicalValue);
		model.addAttribute("weldingValue", weldingValue);
		model.addAttribute("kal05Value", kal05Value);
		model.addAttribute("kal10Value", kal10Value);
		model.addAttribute("assemblyValue", assemblyValue);
		model.addAttribute("warehouseValue", warehouseValue);
		model.addAttribute("otherValue", otherValue);
		model.addAttribute("movements", movements);
		for (String str : unassignedMachinesSet) {
			unassignedMachines += str + "; ";
		}
		model.addAttribute("unassignedMachines", unassignedMachines);

		return "movements/main";

	}

	@RequestMapping(value = "/calculate", params = { "rcpweightstats" }, method = RequestMethod.POST)
	public String performRcpWeightStats(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			return "movements/main";
		}
		
		// create a cookie
		Cookie startDateCookie = new Cookie("startDateMov", "" + movementsForm.getStartDate().getTime());
		Cookie endDateCookie = new Cookie("endDateMov", "" + movementsForm.getEndDate().getTime());
		// set time to live (seconds)
		startDateCookie.setMaxAge(60 * 60 * 24 * 31);
		endDateCookie.setMaxAge(60 * 60 * 24 * 31);
		// add cookie to response
		response.addCookie(startDateCookie);
		response.addCookie(endDateCookie);
		
		List<X3WarehouseWeightLine> lines = x3Service.findWeightSumLine(movementsForm.getStartDate(),
				movementsForm.getEndDate(), JdbcOracleX3Service.WEIGHT_QUERY_RECEPTION);
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("counter", lines.size());
		model.addAttribute("weight", lines);
		return "movements/main";
	}

	@RequestMapping(value = "/calculate", params = { "shipweightstats" }, method = RequestMethod.POST)
	public String performShipWeightStats(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			return "movements/main";
		}
		
		// create a cookie
		Cookie startDateCookie = new Cookie("startDateMov", "" + movementsForm.getStartDate().getTime());
		Cookie endDateCookie = new Cookie("endDateMov", "" + movementsForm.getEndDate().getTime());
		// set time to live (seconds)
		startDateCookie.setMaxAge(60 * 60 * 24 * 31);
		endDateCookie.setMaxAge(60 * 60 * 24 * 31);
		// add cookie to response
		response.addCookie(startDateCookie);
		response.addCookie(endDateCookie);
		
		List<X3WarehouseWeightLine> lines = x3Service.findWeightSumLine(movementsForm.getStartDate(),
				movementsForm.getEndDate(), JdbcOracleX3Service.WEIGHT_QUERY_SHIPMENT);
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("counter", lines.size());
		model.addAttribute("weight", lines);
		return "movements/main";
	}

	@RequestMapping(value = "/calculate", params = { "rcpweightdetails" }, method = RequestMethod.POST)
	public String performRcpWeightDetails(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			return "movements/main";
		}
		
		// create a cookie
		Cookie startDateCookie = new Cookie("startDateMov", "" + movementsForm.getStartDate().getTime());
		Cookie endDateCookie = new Cookie("endDateMov", "" + movementsForm.getEndDate().getTime());
		// set time to live (seconds)
		startDateCookie.setMaxAge(60 * 60 * 24 * 31);
		endDateCookie.setMaxAge(60 * 60 * 24 * 31);
		// add cookie to response
		response.addCookie(startDateCookie);
		response.addCookie(endDateCookie);
		
		List<X3WarehouseWeightDetailLine> lines = x3Service.findWeightDetailLine(movementsForm.getStartDate(),
				movementsForm.getEndDate(), JdbcOracleX3Service.WEIGHT_QUERY_RECEPTION_DETAIL);
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("counter", lines.size());
		model.addAttribute("weightdetails", lines);
		return "movements/main";
	}

	@RequestMapping(value = "/calculate", params = { "shipweightdetails" }, method = RequestMethod.POST)
	public String performShipWeightDetails(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			return "movements/main";
		}
		
		// create a cookie
		Cookie startDateCookie = new Cookie("startDateMov", "" + movementsForm.getStartDate().getTime());
		Cookie endDateCookie = new Cookie("endDateMov", "" + movementsForm.getEndDate().getTime());
		// set time to live (seconds)
		startDateCookie.setMaxAge(60 * 60 * 24 * 31);
		endDateCookie.setMaxAge(60 * 60 * 24 * 31);
		// add cookie to response
		response.addCookie(startDateCookie);
		response.addCookie(endDateCookie);
		
		List<X3WarehouseWeightDetailLine> lines = x3Service.findWeightDetailLine(movementsForm.getStartDate(),
				movementsForm.getEndDate(), JdbcOracleX3Service.WEIGHT_QUERY_SHIPMENT_DETAIL);
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("counter", lines.size());
		model.addAttribute("weightdetails", lines);
		return "movements/main";
	}

	@RequestMapping(value = "/calculate", params = { "rcpmovements" }, method = RequestMethod.POST)
	public String performRcpMovements(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			return "movements/main";
		}
		
		// create a cookie
		Cookie startDateCookie = new Cookie("startDateMov", "" + movementsForm.getStartDate().getTime());
		Cookie endDateCookie = new Cookie("endDateMov", "" + movementsForm.getEndDate().getTime());
		// set time to live (seconds)
		startDateCookie.setMaxAge(60 * 60 * 24 * 31);
		endDateCookie.setMaxAge(60 * 60 * 24 * 31);
		// add cookie to response
		response.addCookie(startDateCookie);
		response.addCookie(endDateCookie);
		

		List<GeodeMovement> movements = geodeService.findRcpMovementsInPeriod(movementsForm.getStartDate(),
				movementsForm.getEndDate());
		Map<String, GeodeMovementsForUser> summary = new HashMap<>();
		Set<String> unknownMovements = new HashSet<>();

		for (GeodeMovement mvt : movements) {
			if (!summary.containsKey(mvt.getCreationUserCode())) {
				summary.put(mvt.getCreationUserCode(),
						new GeodeMovementsForUser(mvt.getCreationUserCode(), mvt.getCreationUserName()));
			}

			if (getGeodeMovementType(mvt) == GeodeMovement.GEODE_MOVEMENT_LOAD) {
				summary.get(mvt.getCreationUserCode()).loadCounterIncrement();
			} else if (getGeodeMovementType(mvt) == GeodeMovement.GEODE_MOVEMENT_UNLOAD) {
				summary.get(mvt.getCreationUserCode()).unloadCounterIncrement();
			} else {
				unknownMovements.add(mvt.getMovementCode());
			}
		}

		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("rcpmovsummary", summary.values());
		if (movements.size() <= 5000) {
			model.addAttribute("rcpmovdetails", movements);
		}
		/*
		 * if(unknownMovements.size() > 0){
		 * model.addAttribute("unknownmovs",unknownMovements); }
		 * System.out.println("UNKNOWN:" + unknownMovements);
		 */

		return "movements/main";
	}
	
	@RequestMapping(value = "/calculate", params = { "shipmovements" }, method = RequestMethod.POST)
	public String performShipMovements(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			return "movements/main";
		}
		
		// create a cookie
		Cookie startDateCookie = new Cookie("startDateMov", "" + movementsForm.getStartDate().getTime());
		Cookie endDateCookie = new Cookie("endDateMov", "" + movementsForm.getEndDate().getTime());
		// set time to live (seconds)
		startDateCookie.setMaxAge(60 * 60 * 24 * 31);
		endDateCookie.setMaxAge(60 * 60 * 24 * 31);
		// add cookie to response
		response.addCookie(startDateCookie);
		response.addCookie(endDateCookie);
		
		
		List<GeodeMovement> movements = geodeService.findShipmentMovementsInPeriod(movementsForm.getStartDate(),
				movementsForm.getEndDate());
		List<GeodeMovement> movementsToShow = new ArrayList<>();
		Map<String, GeodeMovementsForUser> summary = new HashMap<>();
		Set<String> unknownMovements = new HashSet<>();
		
		for (GeodeMovement mvt : movements) {
			if (!summary.containsKey(mvt.getCreationUserCode())) {
				summary.put(mvt.getCreationUserCode(),
						new GeodeMovementsForUser(mvt.getCreationUserCode(), mvt.getCreationUserName()));
			}
			
			if (getGeodeMovementType(mvt) == GeodeMovement.GEODE_MOVEMENT_LOAD) {
				summary.get(mvt.getCreationUserCode()).loadCounterIncrement();
				movementsToShow.add(mvt);
			} else if (getGeodeMovementType(mvt) == GeodeMovement.GEODE_MOVEMENT_UNLOAD) {
				summary.get(mvt.getCreationUserCode()).unloadCounterIncrement();
				movementsToShow.add(mvt);
			} else {
				unknownMovements.add(mvt.getMovementCode());
			}
		}
		
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("rcpmovsummary", summary.values());
		if (movementsToShow.size() <= 5000) {
			model.addAttribute("rcpmovdetails", movementsToShow);
		}
		/*
		 * if(unknownMovements.size() > 0){
		 * model.addAttribute("unknownmovs",unknownMovements); }
		 * System.out.println("UNKNOWN:" + unknownMovements);
		 */
		
		return "movements/main";
	}

	public int getGeodeMovementType(GeodeMovement mvt) {
		if (this.geodeLoadList.contains(mvt.getMovementCode())) {
			return GeodeMovement.GEODE_MOVEMENT_LOAD;
		} else if (this.geodeUnloadList.contains(mvt.getMovementCode())) {
			return GeodeMovement.GEODE_MOVEMENT_UNLOAD;
		} else {
			return GeodeMovement.GEODE_MOVEMENT_UNKNOWN;
		}
	}

	@RequestMapping(value = "/calculate", params = { "shipstockvalue" }, method = RequestMethod.POST)
	public String performShipStockValue(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {

		Map<String, Double> stdPrices = x3Service.getCurrentStandardCostsMap("ATW");
		List<X3ShipmentStockLineWithPrice> lines = x3Service.findAllShipStockWithAveragePrice("ATW");

		double acvValue = 0;
		double afvValue = 0;
		double otherValue = 0;
		double totalValue = 0;
		for (X3ShipmentStockLineWithPrice line : lines) {
			if(!line.getCategory().equals("ACV")){
				if (stdPrices.containsKey(line.getCode())) {
					line.setAveragePrice(stdPrices.get(line.getCode()));
				}
			}
			
			line.setLineValue(line.getAveragePrice() * line.getQuantity() * 1.0);
			if (line.getCategory().equals("ACV")) {
				acvValue += line.getLineValue();
			} else if (line.getCategory().equals("AFV") || line.getCategory().equals("AFN")) {
				afvValue += line.getLineValue();
			} else {
				otherValue += line.getLineValue();
			}
		}

		totalValue = acvValue + afvValue + otherValue;

		model.addAttribute("shipmentsCurrentValue", lines);
		model.addAttribute("acvValue", acvValue);
		model.addAttribute("afvValue", afvValue);
		model.addAttribute("otherValue", otherValue);
		model.addAttribute("totalValue", totalValue);

		return "movements/main";
	}

}

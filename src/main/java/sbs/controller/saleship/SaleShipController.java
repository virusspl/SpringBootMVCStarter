package sbs.controller.saleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
import sbs.model.x3.X3RouteLine;
import sbs.model.x3.X3SalesOrderLine;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("saleship")
public class SaleShipController {

	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;
	
	private static final String DEPARTMENT_ASSEMBLY = "department.assembly";
	private static final String DEPARTMENT_WELDING = "department.welding";
	private static final String DEPARTMENT_MECHANICAL = "department.mechanical";
	private static final String DEPARTMENT_UNASSIGNED= "general.unassigned";
	
	private String[] assemblyCenters;
	private String[] weldingCenters;
	private String[] mechanicalCenters;
	
	
	public SaleShipController() {
		assemblyCenters = new String[]{"08","09","10","11","12","13"};
		weldingCenters = new String[]{"15","16","18","19"};
		mechanicalCenters = new String[]{"01","02","03","04","06","07","14"};
	}
	
	private boolean isStringInArray(String str, String[] array){
		boolean result = false;
		for(String s: array){
			if(s.equals(str)){
				return true;
			}
		}
		return result;
	}

	private String getMainDepartmentCodeByMachineCenter(String center){
		if(isStringInArray(center, assemblyCenters)){
			return DEPARTMENT_ASSEMBLY;
		}
		else if(isStringInArray(center, weldingCenters)){
			return DEPARTMENT_WELDING;
		}
		else if(isStringInArray(center, mechanicalCenters)){
			return DEPARTMENT_MECHANICAL;
		}
		else{
			return DEPARTMENT_UNASSIGNED;
		}
	}
	
	private X3RouteLine getLastRouteLineExcludingKAL(Map<Integer, X3RouteLine> routeLinesMap){
		X3RouteLine result = null;
	
		for(X3RouteLine entry: routeLinesMap.values()){
			if(entry.getMachine().startsWith("KAL")){
				if(result==null){
					result = entry;
				}
			}
			else {
				result = entry;
			}
		}
		return result;
		
	}
	
	@RequestMapping(value = "/main")
	public String showForm(Model model) {
		model.addAttribute("saleShipForm", new SaleShipForm());
		return "saleship/main";		
	}

	@RequestMapping(value = "/exec", method = RequestMethod.POST)
	@Transactional
	public String list(@Valid SaleShipForm saleShipForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		
		// validate
		if (bindingResult.hasErrors()) {
			return "saleship/main";
		}
		
		List<X3SalesOrderLine> orderLines = x3Service.findOpenedSalesOrderLinesInPeriod(saleShipForm.getStartDate(), saleShipForm.getEndDate(), "ATW");
		Map<String, Map<Integer, X3RouteLine>> routes = x3Service.getRoutesMap("ATW"); // cache
		Map<String, String> workcenters = x3Service.getWorkcenterNumbersMapByMachines("ATW");
		Map<String, Integer> magStock = x3Service.findGeneralMagStock("ATW");
		Map<String, Integer> shipStock = x3Service.findGeneralShipStock("ATW");
		Map<String, Integer> stockQ = x3Service.findStockByState("Q", "ATW");
		Map<String, String> productionOrdersBySaleOrders = x3Service.getPendingProductionOrdersBySaleOrders("ATW");
		
		List<SaleShipLine> lines = new ArrayList<>();
		SaleShipLine line;

		X3RouteLine routeInt;
		String machineInt;
		String departmentCodeInt;
		String productionOrderAndLineInt;
		
		for(X3SalesOrderLine ord: orderLines){
			if(routes.containsKey(ord.getProductCode())){
				routeInt = getLastRouteLineExcludingKAL(routes.get(ord.getProductCode()));
				machineInt = routeInt.getMachine();
				departmentCodeInt = getMainDepartmentCodeByMachineCenter(workcenters.get(machineInt));
				productionOrderAndLineInt = productionOrdersBySaleOrders.containsKey(ord.getOrderNumber()+"/"+ord.getOrderLineNumber()) ? productionOrdersBySaleOrders.get(ord.getOrderNumber()+"/"+ord.getOrderLineNumber()) : "N/D";
			}
			else {
				machineInt = "N/A";
				departmentCodeInt = "general.na";
				productionOrderAndLineInt = "N/A";
			}
			
			line = new SaleShipLine();
			line.setSalesOrder(ord.getOrderNumber());
			line.setSalesOrderLine(""+ord.getOrderLineNumber());
			line.setProductionOrderAndLine(productionOrderAndLineInt);
			line.setClientCode(ord.getClientCode());
			line.setClientName(ord.getClientName());
			line.setCountry(ord.getCountry());
			line.setDemandedDate(ord.getDemandedDate());
			line.setCreationDate(ord.getCreationDate());
			line.setUpdateDate(ord.getUpdateDate());
			line.setProductCode(ord.getProductCode());
			line.setProductDescription(ord.getProductDescription());
			line.setProductGr1(ord.getProductGr1());
			line.setMachineCode(machineInt);
			line.setDepartmentCode(departmentCodeInt);
			line.setStockProduction(magStock.containsKey(line.getProductCode()) ? magStock.get(line.getProductCode()) : 0);
			line.setStockShipments(shipStock.containsKey(line.getProductCode()) ? shipStock.get(line.getProductCode()) : 0);
			line.setStockQ(stockQ.containsKey(line.getProductCode()) ? stockQ.get(line.getProductCode()) : 0);
			line.setQuantityRemainingToShip(ord.getQuantityLeftToSend());
			line.setQuantityShipped(ord.getQuantityOrdered()-ord.getQuantityLeftToSend());
			line.setQuantityToGive(line.getQuantityRemainingToShip()-line.getStockShipments() < 0 ? 0 : line.getQuantityRemainingToShip()-line.getStockShipments() );
			lines.add(line);
		}
		
		model.addAttribute("list", lines);
		
		return "saleship/main";
	}
	
	
}

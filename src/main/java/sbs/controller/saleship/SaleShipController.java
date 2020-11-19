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
import sbs.service.production.ProductionService;
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
	@Autowired
	ProductionService prodService;

	public SaleShipController() {
		
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
		Map<String, Integer> magStock = x3Service.findGeneralMagStock("ATW");
		Map<String, Integer> shipStock = x3Service.findGeneralShipStock("ATW");
		Map<String, Integer> stockQ = x3Service.findStockByState("Q", "ATW");
		Map<String, Integer> stockR = x3Service.findStockByState("R", "ATW");
		Map<String, String> productionOrdersBySaleOrders = x3Service.getPendingProductionOrdersBySaleOrders("ATW");
		
		List<SaleShipLine> lines = new ArrayList<>();
		SaleShipLine line;

		X3RouteLine routeInt;
		String machineInt;
		String departmentCodeInt;
		String productionOrderAndLineInt;
		int tmpQ, tmpR;
		for(X3SalesOrderLine ord: orderLines){
			routeInt = prodService.getLastRouteLineExcludingKAL(ord.getProductCode(), "ATW");
			if(routeInt != null){
				machineInt = routeInt.getMachine();
				departmentCodeInt = prodService.getMainDepartmentCodeByMachine(machineInt, "ATW");
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
			line.setOriginalDate(ord.getOriginalDate());
			line.setCreationDate(ord.getCreationDate());
			line.setUpdateDate(ord.getUpdateDate());
			line.setProductCode(ord.getProductCode());
			line.setProductDescription(ord.getProductDescription());
			line.setProductCategory(ord.getProductCategory());
			line.setProductGr1(ord.getProductGr1());
			line.setProductGr2(ord.getProductGr2());
			line.setMachineCode(machineInt);
			line.setDepartmentCode(departmentCodeInt);
			line.setDemandStateCode("saleship.demandState."+ord.getDemandState());
			line.setStockProduction(magStock.containsKey(line.getProductCode()) ? magStock.get(line.getProductCode()) : 0);
			line.setStockShipments(shipStock.containsKey(line.getProductCode()) ? shipStock.get(line.getProductCode()) : 0);
			tmpQ = stockQ.containsKey(line.getProductCode()) ? stockQ.get(line.getProductCode()) : 0;
			tmpR = stockR.containsKey(line.getProductCode()) ? stockR.get(line.getProductCode()) : 0;
			line.setStockQ(tmpQ + tmpR);
			line.setQuantityRemainingToShip(ord.getQuantityLeftToSend());
			line.setQuantityShipped(ord.getQuantityOrdered()-ord.getQuantityLeftToSend());
			line.setQuantityToGive(line.getQuantityRemainingToShip()-line.getStockShipments() < 0 ? 0 : line.getQuantityRemainingToShip()-line.getStockShipments() );
			lines.add(line);
		}
		
		model.addAttribute("list", lines);
		
		return "saleship/main";
	}
	
	
}

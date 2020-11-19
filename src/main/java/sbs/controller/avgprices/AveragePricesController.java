package sbs.controller.avgprices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.model.x3.X3AvgPriceLine;
import sbs.model.x3.X3RouteLine;
import sbs.service.production.ProductionService;
import sbs.service.x3.JdbcOracleX3Service;
import sbs.helpers.DateHelper;

@Controller
@RequestMapping("avgprices")
public class AveragePricesController {
	
	private final int TYPE_INVOICES = 1;
	private final int TYPE_ORDERS = 2;

	@Autowired
	DateHelper dateHelper;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	ProductionService prodService;

	@RequestMapping("/main")
	public String viewIndex() {

		return "avgprices/main";
	}

	@RequestMapping("/inv/adr")
	public String invoicesAdr(Model model) {
		setPricesListAndTitleOnModel(TYPE_INVOICES,"ATW", model);
		return "avgprices/main";
	}

	@RequestMapping("/inv/wps")
	public String invoicesWps(Model model) {
		setPricesListAndTitleOnModel(TYPE_INVOICES,"WPS", model);
		return "avgprices/main";
	}

	@RequestMapping("/ord/adr")
	public String ordersAdr(Model model) {
		setPricesListAndTitleOnModel(TYPE_ORDERS,"ATW", model);
		return "avgprices/main";
	}

	@RequestMapping("/ord/wps")
	public String ordersWps(Model model) {
		setPricesListAndTitleOnModel(TYPE_ORDERS,"WPS", model);
		return "avgprices/main";
	}

	private void setPricesListAndTitleOnModel(int type, String company, Model model) {
		List<X3AvgPriceLine> list = new ArrayList<>();
		String titleCode = "";
		if(type == TYPE_INVOICES) {
			list = x3Service.getAveragePricesByInvoices(company);
			titleCode = "avgprices.byinvoices";
		}
		else if (type == TYPE_ORDERS) {
			list = x3Service.getAveragePricesByOrders(company);
			titleCode = "avgprices.byorders";
		}
		
		for(X3AvgPriceLine line: list) {
			X3RouteLine rtLin = prodService.getLastRouteLineExcludingKAL(line.getProductCode(), company);
			if(rtLin != null) {
				line.setLastMachineCode(rtLin.getMachine());
				line.setLastDepartmentCode(prodService.getMainDepartmentCodeByMachine(rtLin.getMachine(), company));
			}
			else {
				line.setLastMachineCode("N/A");
				line.setLastDepartmentCode("general.na");
			}
			
		}
		
		model.addAttribute("list", list);
		model.addAttribute("titleCode", titleCode);
		model.addAttribute("database", company);
	}

}
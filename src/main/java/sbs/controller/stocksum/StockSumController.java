package sbs.controller.stocksum;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.model.x3.X3Product;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("stocksum")
public class StockSumController {

	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcOracleGeodeService geodeService;

	@RequestMapping("/dispatch")
	public String viewDispatch(Model model, Locale locale) {
		return "stocksum/main";
	}
	
	
	@RequestMapping("/main")
	public String viewList(Model model, Locale locale) {

		//long start = System.currentTimeMillis();

		Map<String, X3Product> products = x3Service.findAllActiveProductsMap("ATW");
		Map<String, Integer> stockA = x3Service.findStockForAllProductsWithStock("ATW");
		Map<String, Integer> stockGeoProd = geodeService
				.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_PRODUCTION);
		Map<String, Integer> stockGeoRcp = geodeService
				.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_RECEPTIONS);
		Map<String, Integer> demand = x3Service.getDemandList("ATW");

		Map<String, StockLine> list = new HashMap<>();
		
		// STOCK A 
		for (Map.Entry<String, Integer> entry : stockA.entrySet()) {
			if (list.containsKey(entry.getKey())) {
				list.get(entry.getKey()).setStockX3(entry.getValue());
			} else {
				list.put(
					entry.getKey(), 
					new StockLine(entry.getKey(), "", entry.getValue(), 0, 0, 0)
				);
			}
		}
		// STOCK PROD
		for (Map.Entry<String, Integer> entry : stockGeoProd.entrySet()) {
			if (list.containsKey(entry.getKey())) {
				list.get(entry.getKey()).setStockGeodeProd(entry.getValue());
			} else {
				list.put(
						entry.getKey(), 
						new StockLine(entry.getKey(), "", 0, entry.getValue(), 0, 0)
						);
			}
		}
		// STOCK RCP
		for (Map.Entry<String, Integer> entry : stockGeoRcp.entrySet()) {
			if (list.containsKey(entry.getKey())) {
				list.get(entry.getKey()).setStockGeodeRcp(entry.getValue());
			} else {
				list.put(
						entry.getKey(), 
						new StockLine(entry.getKey(), "" , 0, 0, entry.getValue(), 0)
						);
			}
		}
		// DEMAND
		for (Map.Entry<String, Integer> entry : demand.entrySet()) {
			if (list.containsKey(entry.getKey())) {
				list.get(entry.getKey()).setDemand(entry.getValue());
			} else {
				list.put(
						entry.getKey(), 
						new StockLine(entry.getKey(), "", 0, 0, 0, entry.getValue())
						);
			}
		}
		
		for(StockLine line: list.values()) {
			if(products.containsKey(line.getCode())) {
				line.setCategory(products.get(line.getCode()).getCategory());
				line.setDescription(products.get(line.getCode()).getDescription());
			}
			else {
				line.setCategory("N/D");
				line.setDescription("N/D");
			}
		}
		
		//System.out.println("time: " + (System.currentTimeMillis() - start) + "ms");
		model.addAttribute("list", list.values());

		return "stocksum/main";
	}

}

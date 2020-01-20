package sbs.controller.stocksum;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("stocksum")
public class StockSumController {

	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcOracleGeodeService geodeService;

	@RequestMapping("/main")
	public String view(Model model, Locale locale) {
		double start = System.currentTimeMillis();

		Map<String, Integer> stockA = x3Service.findStockForAllProductsWithStock("ATW");
		Map<String, Integer> stockGeoProd = geodeService
				.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_PRODUCTION);
		Map<String, Integer> stockGeoRcp = geodeService
				.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_RECEPTIONS);
		Map<String, Integer> demand = x3Service.getAcvDemandList("ATW");
		// model.addAttribute("list", list);

		Map<String, StockLine> list = new HashMap<>();
		
		// STOCK A 
		for (Map.Entry<String, Integer> entry : stockA.entrySet()) {
			if (list.containsKey(entry.getKey())) {
				list.get(entry.getKey()).setStockX3(entry.getValue());
			} else {
				list.put(
					entry.getKey(), 
					new StockLine(entry.getKey(), entry.getValue(), 0, 0, 0)
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
						new StockLine(entry.getKey(), 0, entry.getValue(), 0, 0)
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
						new StockLine(entry.getKey(), 0, 0, entry.getValue(), 0)
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
						new StockLine(entry.getKey(), 0, 0, 0, entry.getValue())
						);
			}
		}
		
		model.addAttribute("list", list.values());
		System.out.println((System.currentTimeMillis() - start) + " ms");
		return "stocksum/main";
	}

}

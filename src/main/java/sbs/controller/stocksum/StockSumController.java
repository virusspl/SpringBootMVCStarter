package sbs.controller.stocksum;

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
		
		//Map<String, Integer> stockA = x3Service.findStockForAllProductsWithStock("ATW");
		//Map<String, Integer> stockGeoProd = geodeService.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_PRODUCTION);
		//Map<String, Integer> stockGeoRcp = geodeService.findStockListForStoreType(JdbcOracleGeodeService.STORE_TYPE_RECEPTIONS);
		Map<String, Integer> demand = x3Service.getAcvDemandList("ATW");
		
		System.out.println(demand);
		
		//model.addAttribute("list", list);
		
		return "stocksum/main";
	}



}

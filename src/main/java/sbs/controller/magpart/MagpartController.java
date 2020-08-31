package sbs.controller.magpart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.model.geode.GeodeQuantityObject;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("magpart")
public class MagpartController {


	@Autowired
	JdbcOracleGeodeService geodeService;
	@Autowired
	JdbcOracleX3Service x3Service;
	
	
	@RequestMapping(value = "/main")
	@Transactional
	public String main() {
		return "magpart/list";
	}
	
	@RequestMapping(value = "/list")
	@Transactional
	public String list(Model model) {
	
		Map<String, GeodeQuantityObject> generalStock = geodeService.findGeneralStockForAllCodes();
		Map<String, GeodeQuantityObject> aStock = geodeService.findStockForOneStore("01");
		Map<String, GeodeQuantityObject> bStock = geodeService.findStockForOneStore("G");
		
		List<MagpartInfo> list = new ArrayList<>();
		MagpartInfo mpi;
		for(GeodeQuantityObject obj: generalStock.values()) {
			mpi = new MagpartInfo(obj.getCode(), obj.getQuantity(), obj.getCount());
			list.add(mpi);
		}
		
		for(MagpartInfo mi: list) {
			if(aStock.containsKey(mi.getProductCode())){
				mi.setStockA(aStock.get(mi.getProductCode()).getQuantity());
				mi.setCountA(aStock.get(mi.getProductCode()).getCount());
			}
			
			if(bStock.containsKey(mi.getProductCode())){
				mi.setStockB(bStock.get(mi.getProductCode()).getQuantity());
				mi.setCountB(bStock.get(mi.getProductCode()).getCount());
			}
			
			mi.setStockOther(mi.getTotalStock() - mi.getStockA() - mi.getStockB());
			mi.setCountOther(mi.getTotalCount() - mi.getCountA() - mi.getCountB());
			
		}
		
		model.addAttribute("list", list);
		return "magpart/list";
	}
	
	
}

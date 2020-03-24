package sbs.controller.rcptosale;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.DateHelper;
import sbs.helpers.TextHelper;
import sbs.model.geode.GeodeObject;
import sbs.model.x3.X3SalesOrderLine;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("rcptosale")
public class ReceptionToSaleController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcOracleGeodeService geodeService;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;

	@RequestMapping("/dispatch")
	public String view() {
		return "rcptosale/dispatch";
	}
	
	@RequestMapping("/view")
	public String showView(RedirectAttributes redirectAttrs) {
		
		return "rcptosale/view";
	}

	@RequestMapping(value = "/makelist", params = { "type" }, method = RequestMethod.POST)
	public String viewList(@RequestParam String type, RedirectAttributes redirectAttrs, Locale locale) {
		
		Date startDate, endDate;
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -12);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		startDate = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 3);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		endDate = cal.getTime();
		

		// database list
		
		List<GeodeObject> objectsRcp = geodeService.findObjectsByStoreType(JdbcOracleGeodeService.STORE_TYPE_RECEPTIONS);
		List<GeodeObject> objectsProd = geodeService.findObjectsByStoreType(JdbcOracleGeodeService.STORE_TYPE_PRODUCTION);
		// join rcp and prod objects in one list
		List<GeodeObject> objects = new ArrayList<>();
		for(GeodeObject objRcp: objectsRcp) {
			objects.add(objRcp);
		}
		for(GeodeObject objProd: objectsProd) {
			objects.add(objProd);
		}
		List<X3SalesOrderLine> allLines = x3Service.findOpenedSalesOrderLinesInPeriod(startDate, endDate, "ATW");

		
		// object lists by code
		Map<String, List<GeodeObject>> obc = new HashMap<>();
		List<GeodeObject> list;
		for(GeodeObject obj: objects) {
			if(obc.containsKey(obj.getItemCode())) {
				obc.get(obj.getItemCode()).add(obj);
			}
			else{
				list = new ArrayList<>();
				list.add(obj);
				obc.put(obj.getItemCode(), list);
			}
		}
		
		List<X3SalesOrderLine> targetLines = new ArrayList<>();
		
		
		for(X3SalesOrderLine line: allLines) {
			// skip if no object in store
			if(!obc.containsKey(line.getProductCode())) {
				continue;
			}
			// divide by category (afv or acv) based on type argument
			
			if(line.getProductGr1().equalsIgnoreCase("ACQ") && 
							   type.equalsIgnoreCase("purchase")) {
				targetLines.add(line);
			}
			else if (!line.getProductGr1().equalsIgnoreCase("ACQ") && type.equalsIgnoreCase("production")) {
				targetLines.add(line);
			}
		}
		
		// result lines
		List<ReceptionToSaleLine> result = new ArrayList<>();
		List<ReceptionToSaleLine> tmpObjList;
		ReceptionToSaleLine rl;
		for(X3SalesOrderLine line: targetLines) {
			rl = new ReceptionToSaleLine();
			rl.setOrderNumber(line.getOrderNumber());
			rl.setOrderLine(""+line.getOrderLineNumber());
			rl.setItemCode(line.getProductCode());
			rl.setItemDescription(line.getProductDescription());
			rl.setClientCode(line.getClientCode());
			rl.setClientName(line.getClientName());
			rl.setGr1OrObjectNumber(line.getProductGr1());
			rl.setGr2OrAddress(line.getProductGr2());
			rl.setDate(dateHelper.formatYyyyMmDd(line.getDemandedDate()));
			rl.setToSendOrQuantity(line.getQuantityLeftToSend());
			rl.setOrdered(line.getQuantityOrdered());
			result.add(rl);
			// create list to sort (comparator in object - quantity)
			tmpObjList = new ArrayList<>();
			for(GeodeObject obj: obc.get(line.getProductCode())) {
				rl = new ReceptionToSaleLine();
				rl.setOrderNumber("");
				rl.setOrderLine("");
				rl.setItemCode("");
				rl.setItemDescription("");
				rl.setClientCode("");
				rl.setClientName("");
				rl.setGr1OrObjectNumber(obj.getNumber());
				rl.setGr2OrAddress(obj.getStore()+  obj.getAddress());
				rl.setDate(dateHelper.formatYyyyMmDd(obj.getInputDate()));
				rl.setToSendOrQuantity(obj.getQuantity());
				rl.setOrdered(0);
				tmpObjList.add(rl);
			}
			Collections.sort(tmpObjList);
			for(ReceptionToSaleLine tmpLine: tmpObjList) {
				result.add(tmpLine);
			}
		}
		
		redirectAttrs.addFlashAttribute("lines", result);
		
		if(type.equalsIgnoreCase("production")) {
			redirectAttrs.addFlashAttribute("title", "rcptosale.production");
		}
		else {
			redirectAttrs.addFlashAttribute("title", "rcptosale.purchase");
		}

		return "redirect:/rcptosale/view";	
		
	}
}

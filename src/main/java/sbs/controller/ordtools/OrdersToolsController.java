package sbs.controller.ordtools;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.DateHelper;
import sbs.helpers.TextHelper;
import sbs.model.x3.X3KeyValString;
import sbs.model.x3.X3SalesOrderItemSum;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("ordtools")
public class OrdersToolsController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;

	@RequestMapping("/main")
	public String view(Model model, Locale locale) {

		OrdersToolsForm ordersToolsForm = new OrdersToolsForm();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		ordersToolsForm.setStartDate(new Timestamp(cal.getTimeInMillis()));

		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		ordersToolsForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
		model.addAttribute("ordersToolsForm", ordersToolsForm);
		return "ordtools/main";
	}

	@RequestMapping(value = "/makelist", params = { "viewlist" }, method = RequestMethod.POST)
	public String viewList(@Valid OrdersToolsForm ordersToolsForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "ordtools/main";
		}

		// database list
		//Map<String, Integer> stock = x3Service.findGeneralStockForAllProducts("ATW");
		List<X3SalesOrderItemSum> lines = x3Service.findAllSalesOrdersItemsInPeriod(ordersToolsForm.getStartDate(),
				ordersToolsForm.getEndDate(), "ATW");

		List<X3KeyValString> allBomParts = x3Service.getAllBomPartsInBoms("ATW");
		List<X3KeyValString> allTools = x3Service.getAllToolsInRouting("ATW");
		Map<String, Set<String>> bomPartsByCode = prepareMapByCode(allBomParts);
		Map<String, Set<String>> toolsByCode = prepareMapByCode(allTools);

		// loop through lines
		OrdersToolsLine line;
		List<OrdersToolsLine> list = new ArrayList<>();
		Set<String> set;
		
		/* tmp */
		//Set<String> setTmp = new HashSet<>();
		//fillEmptySetWithToolsByProductCode("GH11N05", setTmp, bomPartsByCode, toolsByCode);
		/* tmp */
		
		for (X3SalesOrderItemSum sois : lines) {
			set = new HashSet<>();
			fillEmptySetWithToolsByProductCode(sois.getProductCode(), set, bomPartsByCode, toolsByCode);
			if (set.size() > 0) {
				for (String tool : set) {
					line = createOrdersToolsLineFromX3Info(sois);
					line.setTool(tool);
					list.add(line);
				}
			} else {
				line = createOrdersToolsLineFromX3Info(sois);
				line.setTool("");
				list.add(line);
			}
		}

		model.addAttribute("list", list);
		model.addAttribute("startDate", ordersToolsForm.getStartDate());
		model.addAttribute("endDate", ordersToolsForm.getEndDate());

		return "ordtools/main";
	}

	private Map<String, Set<String>> prepareMapByCode(List<X3KeyValString> pairs) {
		Map<String, Set<String>> map = new HashMap<>();
		Set<String> set;
		for (X3KeyValString pair : pairs) {
			if (map.containsKey(pair.getKey())) {
				map.get(pair.getKey()).add(pair.getValue());
			} else {
				set = new HashSet<>();
				set.add(pair.getValue());
				map.put(pair.getKey(), set);
			}

		}
		return map;
	}

	private void fillEmptySetWithToolsByProductCode(String code, Set<String> set,
			Map<String, Set<String>> bomPartsByCode, Map<String, Set<String>> toolsByCode) {
		// Tools
		if (toolsByCode.containsKey(code)) {
			for (String tool : toolsByCode.get(code)) {
				set.add(tool);
			}
		}
		// BOM
		if (bomPartsByCode.containsKey(code)) {
			for (String cpnitmref : bomPartsByCode.get(code)) {
				fillEmptySetWithToolsByProductCode(cpnitmref, set, bomPartsByCode, toolsByCode);
			}
		}
	}

	private OrdersToolsLine createOrdersToolsLineFromX3Info(X3SalesOrderItemSum sois) {
		OrdersToolsLine otl = new OrdersToolsLine();
		otl.setOrder(sois.getOrderNumber());
		otl.setProductCode(sois.getProductCode());
		otl.setProductDescription(sois.getProductDescription());
		otl.setQuantity(sois.getQuantityOrdered());
		return otl;
	}

}

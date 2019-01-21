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
import sbs.model.x3.X3SalesOrderItem;
import sbs.model.x3.X3ToolEntry;
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
		// Map<String, Integer> stock =
		// x3Service.findGeneralStockForAllProducts("ATW");
		List<X3SalesOrderItem> lines = x3Service.findAllSalesOrdersAfvItemsInPeriod(ordersToolsForm.getStartDate(),
				ordersToolsForm.getEndDate(), "ATW");

		List<X3KeyValString> allBomParts = x3Service.getAllBomPartsInBoms("ATW");
		List<X3ToolEntry> allTools = x3Service.getAllToolsInRouting("ATW");
		Map<String, Set<String>> bomPartsByCode = prepareMapByCode(allBomParts);
		Map<String, List<X3ToolEntry>> toolsOperations = prepareToolsOperations(allTools);
		// ^List<X3ToolEntry>
		// loop through lines
		OrdersToolsLine line;
		List<OrdersToolsLine> list = new ArrayList<>();
		Set<X3ToolEntry> set;

		/* tmp */
		// Set<String> setTmp = new HashSet<>();
		// fillEmptySetWithToolsByProductCode("GH11N05", setTmp, bomPartsByCode,
		// toolsByCode);
		/* tmp */

		for (X3SalesOrderItem sois : lines) {
			set = new HashSet<>();
			fillEmptySetWithToolsByProductCode(sois.getProductCode(), set, bomPartsByCode, toolsOperations);
			if (set.size() > 0) {
				for (X3ToolEntry tool : set) {
					line = createOrdersToolsLineFromX3Info(sois);
					line.setTool(tool);
					list.add(line);
				}
			} else {
				line = createOrdersToolsLineFromX3Info(sois);
				line.setTool(new X3ToolEntry());
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

	private Map<String, List<X3ToolEntry>> prepareToolsOperations(List<X3ToolEntry> toolsList) {
		Map<String, List<X3ToolEntry>> map = new HashMap<>();
		List<X3ToolEntry> list;
		for (X3ToolEntry entry : toolsList) {
			if (map.containsKey(entry.getCode())) {
				map.get(entry.getCode()).add(entry);
			} else {
				list = new ArrayList<>();
				list.add(entry);
				map.put(entry.getCode(), list);
			}

		}
		return map;
	}

	private void fillEmptySetWithToolsByProductCode(String code, Set<X3ToolEntry> set,
			Map<String, Set<String>> bomPartsByCode, Map<String, List<X3ToolEntry>> toolsOperations) {
		
		// Tools
		if (toolsOperations.containsKey(code)) {
			for (X3ToolEntry tool : toolsOperations.get(code)) {
				set.add(tool);
			}
		}

		// BOM
		if (bomPartsByCode.containsKey(code)) {
			for (String cpnitmref : bomPartsByCode.get(code)) {
				fillEmptySetWithToolsByProductCode(cpnitmref, set, bomPartsByCode, toolsOperations);
			}
		}
	}

	private OrdersToolsLine createOrdersToolsLineFromX3Info(X3SalesOrderItem sois) {
		OrdersToolsLine otl = new OrdersToolsLine();
		otl.setOrder(sois.getOrderNumber());
		otl.setDemandedDate(sois.getDemandedDate());
		otl.setProductCode(sois.getProductCode());
		otl.setProductDescription(sois.getProductDescription());
		otl.setQuantity(sois.getQuantityOrdered());
		return otl;

	}

}

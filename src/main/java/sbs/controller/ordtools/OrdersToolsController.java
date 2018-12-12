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
import sbs.model.x3.X3SalesOrderItemSum;
import sbs.model.x3.X3SalesOrderLine;
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
		cal.add(Calendar.MONTH, -3);
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
		Map<String, Integer> stock = x3Service.findGeneralStockForAllProducts("ATW");
		List<X3SalesOrderItemSum> lines = x3Service.findAllSalesOrdersItemsInPeriod(ordersToolsForm.getStartDate(), ordersToolsForm.getEndDate(), "ATW");

		
		// loop through lines
		OrdersToolsLine line;
		int tmp;
		List<OrdersToolsLine> list = new ArrayList<>();
		for (X3SalesOrderItemSum sois : lines) {
			line = createOrdersToolsLineFromX3Info(sois);
			line.setTool("");
			list.add(line);
		}
		
		
		model.addAttribute("list", list);
		model.addAttribute("startDate", ordersToolsForm.getStartDate());
		model.addAttribute("endDate", ordersToolsForm.getEndDate());

		return "ordtools/main";
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

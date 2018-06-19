package sbs.controller.prodtosale;

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
import sbs.model.x3.X3SalesOrderLine;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("prodtosale")
public class ProductionToSaleController {

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
		ProductionToSaleForm productionToSaleForm = new ProductionToSaleForm();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -12);
		productionToSaleForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
		
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		productionToSaleForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
		model.addAttribute("productionToSaleForm", productionToSaleForm);
		return "prodtosale/main";
	}

	@RequestMapping(value = "/makelist", params = { "viewlist" }, method = RequestMethod.POST)
	public String viewList(@Valid ProductionToSaleForm productionToSaleForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "prodtosale/main";
		}

		// database list
		Map<String, Integer> stock = x3Service.findGeneralStockForAllProducts("ATW");
		List<X3SalesOrderLine> lines = x3Service.findOpenedSalesOrderLinesInPeriod(productionToSaleForm.getStartDate(),
				productionToSaleForm.getEndDate(), "ATW");

		// variables for summary calculation
		Set<String> allGroups = new HashSet<>();
		Map<String, Integer> tillToday = new HashMap<>();
		Map<String, Integer> endOfMonth = new HashMap<>();
		Map<String, Integer> oneMonthAhead = new HashMap<>();
		Map<String, Integer> furtherDates = new HashMap<>();
		// border dates for summary
		Calendar cal = Calendar.getInstance();
		Timestamp today = new Timestamp(cal.getTime().getTime());
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Timestamp thisMonth = new Timestamp(cal.getTime().getTime());
		cal.add(Calendar.MONTH, 1);
		Timestamp nextMonth = new Timestamp(cal.getTime().getTime());

		// loop through lines
		ProductionToSaleLine sl;
		int tmp;
		List<ProductionToSaleLine> showList = new ArrayList<>();
		for (X3SalesOrderLine sol : lines) {
			sl = new ProductionToSaleLine();
			sl.setOrderNumberAndLine(sol.getOrderNumber() + "/" + sol.getOrderLineNumber());
			sl.setProductCode(sol.getProductCode());
			sl.setProductDescription(sol.getProductDescription());
			sl.setProductGr1(sol.getProductGr1());
			sl.setProductGr2(sol.getProductGr2());
			sl.setProductGr3(sol.getProductGr3());
			sl.setClientCode(sol.getClientCode());
			sl.setClientName(sol.getClientName());
			sl.setDemandedDate(sol.getDemandedDate());
			sl.setQuantityOrdered(sol.getQuantityOrdered());
			sl.setQuantityLeftToSend(sol.getQuantityLeftToSend());
			// available is minimum of "left to send" and "stock"
			sl.setQuantityAvailable(Math.min(stock.get(sol.getProductCode()), sol.getQuantityLeftToSend()));
			// decrease in stock
			stock.put(sol.getProductCode(),
					decreaseZeroPositive(stock.get(sol.getProductCode()), sol.getQuantityLeftToSend()));
			// add line to list
			showList.add(sl);

			// calculation for summary
			allGroups.add(sl.getProductGr2());

			if (dateHelper.dateBefore(sol.getDemandedDate(), today)) {
				tmp = tillToday.containsKey(sol.getProductGr2()) ? tillToday.get(sol.getProductGr2()) : 0;
				tillToday.put(sol.getProductGr2(), (tmp + sol.getQuantityLeftToSend()));
			} 
			else if (dateHelper.isDateInRange(sol.getDemandedDate(), today, thisMonth)) {
				tmp = endOfMonth.containsKey(sol.getProductGr2()) ? endOfMonth.get(sol.getProductGr2()) : 0;
				endOfMonth.put(sol.getProductGr2(), (tmp + sol.getQuantityLeftToSend()));
			} 
			else if (dateHelper.isDateInRange(sol.getDemandedDate(), thisMonth, nextMonth)) {
				tmp = oneMonthAhead.containsKey(sol.getProductGr2()) ? oneMonthAhead.get(sol.getProductGr2()) : 0;
				oneMonthAhead.put(sol.getProductGr2(), (tmp + sol.getQuantityLeftToSend()));
			} 
			else if (dateHelper.dateAfter(sol.getDemandedDate(), nextMonth)) {
				tmp = furtherDates.containsKey(sol.getProductGr2()) ? furtherDates.get(sol.getProductGr2()) : 0;
				furtherDates.put(sol.getProductGr2(), (tmp + sol.getQuantityLeftToSend()));
			}
		}
		
		// prepare summary lines
		ArrayList<ProductionToSaleSummaryLine> summary = new ArrayList<>();
		ProductionToSaleSummaryLine suml;
		for(String gr: allGroups){
			suml = new ProductionToSaleSummaryLine();
			suml.setGroup(gr);
			suml.setQuantityToday(tillToday.containsKey(gr) ? tillToday.get(gr) : 0);
			suml.setQuantityEndMonth(endOfMonth.containsKey(gr) ? endOfMonth.get(gr) : 0);
			suml.setQuantityNextMonth(oneMonthAhead.containsKey(gr) ? oneMonthAhead.get(gr) : 0);
			suml.setQuantityFurther(furtherDates.containsKey(gr) ? furtherDates.get(gr) : 0);
			summary.add(suml);
		}
		
		
		model.addAttribute("summary", summary);
		model.addAttribute("showList", showList);
		model.addAttribute("startDate", productionToSaleForm.getStartDate());
		model.addAttribute("endDate", productionToSaleForm.getEndDate());

		return "prodtosale/main";
	}

	private int decreaseZeroPositive(int initial, int decrease) {
		return (initial - decrease <= 0) ? 0 : initial - decrease;
	}

}

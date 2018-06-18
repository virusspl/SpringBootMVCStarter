package sbs.controller.prodtosale;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


	@RequestMapping("/main")
	public String view(Model model, Locale locale) {
		ProductionToSaleForm productionToSaleForm = new ProductionToSaleForm();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		productionToSaleForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
		cal.add(Calendar.DAY_OF_MONTH, -6);
		productionToSaleForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
		model.addAttribute("productionToSaleForm", productionToSaleForm);
		return "prodtosale/main";
	}

	@RequestMapping(value = "/makelist", params = { "viewlist" }, method = RequestMethod.POST)
	public String viewList(@Valid ProductionToSaleForm productionToSaleForm,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "prodtosale/main";
		}

		List<X3SalesOrderLine> lines = x3Service.findOpenedSalesOrderLinesInPeriod(productionToSaleForm.getStartDate(), productionToSaleForm.getEndDate(), "ATW");
		Map<String, Integer> stock = x3Service.findGeneralStockForAllProducts("ATW");
		
		System.out.println(stock);
		
		
		model.addAttribute("startDate", productionToSaleForm.getStartDate());
		model.addAttribute("endDate", productionToSaleForm.getEndDate());

		return "dirrcpship/main";
	}
	
}

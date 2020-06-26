package sbs.controller.histock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import sbs.helpers.DateHelper;
import sbs.helpers.TextHelper;
import sbs.model.x3.X3ConsumptionProductInfo;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3ProductEventsHistory;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("histock")
public class HistoryStockController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;
	

	public HistoryStockController() {

	}

	@RequestMapping("/main")
	public String view(Model model, Locale locale) {

		FormHistoryStock formHistoryStock = new FormHistoryStock();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		formHistoryStock.setStartDate(new Timestamp(cal.getTimeInMillis()));

		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		formHistoryStock.setEndDate(new Timestamp(cal.getTimeInMillis()));

		model.addAttribute("formHistoryStock", formHistoryStock);
		return "histock/main";
	}

	@RequestMapping(value = "/showhistory", params = { "execute" }, method = RequestMethod.POST)
	public String findChains(@Valid FormHistoryStock formHistoryStock, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Model model) {

		if (bindingResult.hasErrors()) {
			return "histock/main";
		}

		formHistoryStock.setItem(formHistoryStock.getItem().toUpperCase().trim());
		if (formHistoryStock.getItem().length() > 0) {
			String itemDescription = x3Service.findItemDescription("ATW", formHistoryStock.getItem());
			if (itemDescription == null) {
				bindingResult.rejectValue("item", "error.no.such.product", "ERROR");
				return "histock/main";
			}
			redirectAttrs.addFlashAttribute("code", formHistoryStock.getItem());
		}
		redirectAttrs.addFlashAttribute("startDate", formHistoryStock.getStartDate());
		redirectAttrs.addFlashAttribute("endDate", formHistoryStock.getEndDate());

		return "redirect:/histock/make";
	}

	@RequestMapping("/make")
	public String doMake(Model model, Locale locale, RedirectAttributes redirectAttrs)
			throws FileNotFoundException, IOException {

		if(!model.asMap().containsKey("startDate")) {
			return "redirect:/histock/main"; 
		}
		//String code;
		Date startDate = (Date)model.asMap().get("startDate");
		Date endDate = (Date)model.asMap().get("endDate");
		
		//if (model.asMap().containsKey("code")) {
		//	code = (String) model.asMap().get("code");
		//}
		
		List<X3ConsumptionProductInfo> acvInfo = x3Service.getAcvListForConsumptionReport("ATW");
		Map<String, X3ProductEventsHistory> history = x3Service.getAcvProductsEventsHistory(startDate, endDate, acvInfo, "ATW");
		Map<String, X3Product> products = x3Service.findAllActiveProductsMap("ATW");
		List<List<String>> lines = new ArrayList<>();
		List<String> line;
		
		X3ProductEventsHistory hist;
		for(X3ConsumptionProductInfo info: acvInfo) {
			line = new ArrayList<>();
			line.add(info.getProductCode());
			line.add(info.getProductDescriptionPl());
			line.add(textHelper.numberFormatIntegerRoundNoSpace(info.getStock()));
			line.add(textHelper.numberFormatDotNoSpace(info.getAverageCost()));
			line.add(textHelper.numberFormatIntegerRoundNoSpace(info.getEwz()));
			line.add(textHelper.numberFormatIntegerRoundNoSpace(info.getTechnicalLot()));
			line.add(textHelper.numberFormatIntegerRoundNoSpace(info.getMaxStsock()));
			line.add(textHelper.numberFormatIntegerRoundNoSpace(info.getReorderPoint()));
			line.add(textHelper.numberFormatIntegerRoundNoSpace(info.getSafetyStock()));
			if(history.containsKey(info.getProductCode())) {
				hist = history.get(info.getProductCode());
				hist.countDrops(info.getSafetyStock());
				line.add(textHelper.numberFormatIntegerRoundNoSpace(hist.getZeroCounter()));
				line.add(textHelper.numberFormatIntegerRoundNoSpace(hist.getZeroDays()));
				line.add(textHelper.numberFormatIntegerRoundNoSpace(hist.getMinimumCounter()));	
				line.add(hist.getZeroDates());	
			}
			else {
				line.add("N/D");
				line.add("N/D");
				line.add("N/D");
				line.add("N/D");
			}
			line.add(products.containsKey(info.getProductCode()) ? products.get(info.getProductCode()).isVerifyStock()+"" : "B/D");
			lines.add(line);
		}
		model.addAttribute("startDate", dateHelper.formatDdMmYyyy(startDate));
		model.addAttribute("endDate", dateHelper.formatDdMmYyyy(endDate));
		model.addAttribute("lines", lines);
		return "histock/view";
	}

}

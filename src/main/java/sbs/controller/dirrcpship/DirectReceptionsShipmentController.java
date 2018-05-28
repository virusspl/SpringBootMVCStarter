package sbs.controller.dirrcpship;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
import sbs.service.dictionary.X3HistoryPriceService;
import sbs.service.x3.JdbcOracleX3Service;


@Controller
@RequestMapping("dirrcpship")
public class DirectReceptionsShipmentController {
	
	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	X3HistoryPriceService x3HistoryPriceService;
	@Autowired
	TextHelper textHelper;

	List<String> alwaysShow;
	List<String> alwaysHide;

	@Autowired
	public DirectReceptionsShipmentController(Environment env) {
		alwaysShow = Arrays.asList(env.getRequiredProperty("dirrcpship.codes.alwaysshow").split(";"));
		alwaysHide = Arrays.asList(env.getRequiredProperty("dirrcpship.codes.alwayshide").split(";"));
	}

	private boolean isAlwaysShow(String code) {
		for (String prefix : alwaysShow) {
			if (code.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAlwaysHide(String code) {
		for (String prefix : alwaysHide) {
			if (code.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping("/main")
	public String view(Model model, Locale locale) {
		DirectReceptionsShipmentForm directReceptionsShipmentForm = new DirectReceptionsShipmentForm();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		directReceptionsShipmentForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
		cal.add(Calendar.DAY_OF_MONTH, -6);
		directReceptionsShipmentForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
		model.addAttribute("directReceptionsShipmentForm", directReceptionsShipmentForm);
		return "dirrcpship/main";
	}

	@RequestMapping(value = "/makelist", params = { "viewlist" }, method = RequestMethod.POST)
	public String viewList(@Valid DirectReceptionsShipmentForm directReceptionsShipmentForm,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "dirrcpship/main";
		}
		model.addAttribute("showList", prepareList(directReceptionsShipmentForm, false));
		model.addAttribute("startDate", directReceptionsShipmentForm.getStartDate());
		model.addAttribute("endDate", directReceptionsShipmentForm.getEndDate());

		return "dirrcpship/main";
	}
	@RequestMapping(value = "/makelist", params = { "printlist" }, method = RequestMethod.POST)
	public String printList(@Valid DirectReceptionsShipmentForm directReceptionsShipmentForm,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "dirrcpship/print";
		}
		model.addAttribute("list", prepareList(directReceptionsShipmentForm, false));
		model.addAttribute("title", messageSource.getMessage("dirrcpship.list.show", null, locale));
		
		return "dirrcpship/print";
	}
	@RequestMapping(value = "/makelist", params = { "viewnegativelist" }, method = RequestMethod.POST)
	public String viewNegativeList(@Valid DirectReceptionsShipmentForm directReceptionsShipmentForm,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "dirrcpship/main";
		}
		model.addAttribute("negativeList", prepareList(directReceptionsShipmentForm, true));
		model.addAttribute("startDate", directReceptionsShipmentForm.getStartDate());
		model.addAttribute("endDate", directReceptionsShipmentForm.getEndDate());
		
		return "dirrcpship/main";
	}
	@RequestMapping(value = "/makelist", params = { "printnegativelist" }, method = RequestMethod.POST)
	public String printNegativeList(@Valid DirectReceptionsShipmentForm directReceptionsShipmentForm,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs, Locale locale) {
		if (bindingResult.hasErrors()) {
			return "dirrcpship/print";
		}
		model.addAttribute("list", prepareList(directReceptionsShipmentForm, true));
		model.addAttribute("title", messageSource.getMessage("dirrcpship.list.negative", null, locale));
		
		return "dirrcpship/print";
	}


	private List<DirectReceptionsShipmentLine> prepareList(DirectReceptionsShipmentForm directReceptionsShipmentForm, boolean negative) {
		// average 1-month usage
				String startPeriod, endPeriod;
				Calendar calendar = Calendar.getInstance();
				endPeriod = calendar.get(Calendar.YEAR) + ""
						+ textHelper.fillWithLeadingZero("" + calendar.get(Calendar.MONTH), 2);
				calendar.add(Calendar.MONTH, -7);
				startPeriod = calendar.get(Calendar.YEAR) + ""
						+ textHelper.fillWithLeadingZero("" + calendar.get(Calendar.MONTH), 2);
				// average usage
				Map<String, Integer> averageUsage = x3Service.findAcvAverageUsageInPeriod(startPeriod, endPeriod,"ATW");
				// MAG stock
				Map<String, Integer> magStock = x3Service.findAcvMagStock("ATW");
				// shipment stock
				Map<String, Integer> shipStock = x3Service.findAcvShipStock("ATW");
				// non production codes
				List<String> nonProductionCodes = x3Service.findAcvNonProductionCodes("ATW");
				// main list
				List<DirectReceptionsShipmentLine> list = x3Service.findDirectReceptionsShipmentLines(
						directReceptionsShipmentForm.getStartDate(), directReceptionsShipmentForm.getEndDate(), "ATW");
				// calculate mag stock and left to give
				calculateMagAndToGiveStock(list, magStock, shipStock);
				// calculate total current shipment demand
				Map<String, Integer> shipDemand = calculateShipmentDemandFromLines(list);

				
				List<DirectReceptionsShipmentLine> showList = new ArrayList<>();
				List<DirectReceptionsShipmentLine> negativeList = new ArrayList<>();
				
				int avg, dem, stck;
				
				for (DirectReceptionsShipmentLine line : list) {
					// if nothing to give, never show
					if(line.getToGive() == 0){
						continue;
					}
					// if no stock, skip, never show
					if(!magStock.containsKey(line.getProductCode())){
						continue;
					}
					// if always hide, negative
					if(isAlwaysHide(line.getProductCode())){
						negativeList.add(line);
						continue;
					}
					// is always show, show
					if(isAlwaysShow(line.getProductCode())){
						showList.add(line);
						continue;
					}
					// if not in BOM, show
					if(nonProductionCodes.contains(line.getProductCode())){
						showList.add(line);
						continue;
					}
					
					// get average one month consumption 
					avg = averageUsage.containsKey(line.getProductCode()) ? averageUsage.get(line.getProductCode()) : 0;
					// total demand for code
					dem = shipDemand.containsKey(line.getProductCode()) ? shipDemand.get(line.getProductCode()): 0;
					// get receptions stock for code
					stck = magStock.containsKey(line.getProductCode()) ? magStock.get(line.getProductCode()) : 0;
					
					// if demand <= 1-month-consumption and stock > 3-month-consumption, show, else negative
					if ((dem <= avg) && (stck > (3*avg))){
						showList.add(line);
					}
					else{
						negativeList.add(line);
					}	
				}
				
				if(!negative){
					return showList;
				}
				else{
					return negativeList;
				}
	}

	private void calculateMagAndToGiveStock(List<DirectReceptionsShipmentLine> list, Map<String, Integer> magStock, Map<String, Integer> shipStock) {
		for (DirectReceptionsShipmentLine line : list) {
			line.setMagStock(magStock.containsKey(line.getProductCode()) ? magStock.get(line.getProductCode()) : 0);
			line.setToGive(shipStock.containsKey(line.getProductCode())
					? line.getLeftToSend() - shipStock.get(line.getProductCode()) : line.getLeftToSend());
			if (line.getToGive() < 0) {
				line.setToGive(0);
			}

		}
	}
	
	private Map<String, Integer> calculateShipmentDemandFromLines(List<DirectReceptionsShipmentLine> list) {
		Map<String, Integer> map = new HashMap<>();
		for (DirectReceptionsShipmentLine line : list) {
			if (map.containsKey(line.getProductCode())) {
				map.put(line.getProductCode(), map.get(line.getProductCode()) + line.getToGive());
			} else {
				map.put(line.getProductCode(), line.getToGive());
			}
		}
		return map;
	}
}

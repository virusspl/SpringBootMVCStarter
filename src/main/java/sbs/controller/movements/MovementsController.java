package sbs.controller.movements;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import sbs.controller.geolook.GeodeSearchForm;
import sbs.model.x3.X3HistoryPrice;
import sbs.model.x3.X3ShipmentMovement;
import sbs.model.x3.X3UtrMachine;
import sbs.model.x3.X3UtrWorker;
import sbs.service.dictionary.X3HistoryPriceService;
import sbs.service.x3.JdbcOracleX3Service;


@Controller
@RequestMapping("movements")
public class MovementsController {
	
	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service x3Service;
	@Autowired
	X3HistoryPriceService x3HistoryPriceService;  
	
    @Autowired
    public MovementsController(Environment env) {
    	
    }
	
    @RequestMapping("/shipment")
    public String view(Model model, Locale locale){
    	MovementsForm movementsForm = new MovementsForm();
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	cal.add(Calendar.DAY_OF_MONTH, -1);
    	movementsForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
    	cal.add(Calendar.DAY_OF_MONTH, -6);
    	movementsForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
    	model.addAttribute("movementsForm", movementsForm);
    	
    	return "movements/shipment";
    }
    
    
	@RequestMapping(value = "/shipment", method = RequestMethod.POST)
	public String performSearch(@Valid MovementsForm movementsForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs, Locale locale){
		
		if(bindingResult.hasErrors()){
			return "movements/shipment";
		}
		
		Map<String, Double> prices = x3HistoryPriceService.findAllX3HistoryPrices();
		List<X3ShipmentMovement> movements = x3Service.findAdrShipmentMovementsInPeriod(movementsForm.getStartDate(), movementsForm.getEndDate());
		int counter = 0;
		double totalValue = 0;
		
		for(X3ShipmentMovement mvt: movements){
			if(prices.get(mvt.getItemCode())!=null){
				mvt.setPrice(prices.get(mvt.getItemCode()));
			}
			else{
				mvt.setPrice(mvt.getEmergencyAveragePrice());
			}
			mvt.setValue(mvt.getQuantity()*mvt.getPrice());
			totalValue += mvt.getValue();
			counter++;
		}
		
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("counter", counter);
		model.addAttribute("totalValue", totalValue);
		model.addAttribute("movements", movements);
		
		System.out.println(counter + " " + totalValue);
		return "movements/shipment";
	}
    
    
    
}

package sbs.controller.movements;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashSet;
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

import sbs.model.x3.X3ProductFinalMachine;
import sbs.model.x3.X3ShipmentMovement;
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
		double mechanicalValue = 0;
		double weldingValue = 0;
		double assemblyValue = 0;
		double otherValue = 0;
		LinkedHashSet<String> unassignedMachinesSet = new LinkedHashSet<>();
		String unassignedMachines = "";
		
		Map<String, X3ProductFinalMachine> machinesIndex = x3Service.findX3ProductFinalMachines("ATW");
		
		for(X3ShipmentMovement mvt: movements){
			if(prices.get(mvt.getItemCode())!=null){
				mvt.setPrice(prices.get(mvt.getItemCode()));
			}
			else{
				mvt.setPrice(mvt.getEmergencyAveragePrice());
			}
			mvt.setValue(mvt.getQuantity()*mvt.getPrice());
			
			if(machinesIndex.containsKey(mvt.getItemCode())){
				switch(machinesIndex.get(mvt.getItemCode()).getMachineDepartment()){
				case X3ProductFinalMachine.MECHANICAL:
					mechanicalValue += mvt.getValue();
					break;
				case X3ProductFinalMachine.WELDING:
					weldingValue += mvt.getValue();
					break;
				case X3ProductFinalMachine.ASSEMBLY:
					assemblyValue += mvt.getValue();
					break;
				default:
					otherValue += mvt.getValue();
					unassignedMachinesSet.add(machinesIndex.get(mvt.getItemCode()).getMachineCode()); 
					break;
				}
			}
			else{
				otherValue += mvt.getValue();
			}
			
			totalValue += mvt.getValue();
			counter++;
		}
		
		model.addAttribute("startDate", movementsForm.getStartDate());
		model.addAttribute("endDate", movementsForm.getEndDate());
		model.addAttribute("counter", counter);
		model.addAttribute("totalValue", totalValue);
		model.addAttribute("mechanicalValue", mechanicalValue);
		model.addAttribute("weldingValue", weldingValue);
		model.addAttribute("assemblyValue", assemblyValue);
		model.addAttribute("otherValue", otherValue);
		model.addAttribute("movements", movements);
		for (String str: unassignedMachinesSet){
			unassignedMachines += str + "; ";
		}
		model.addAttribute("unassignedMachines", unassignedMachines);
		
		return "movements/shipment";
	}
    
    
    
}

package sbs.controller.saleship;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
import sbs.model.x3.X3Client;
import sbs.model.x3.X3RouteLine;
import sbs.model.x3.X3SalesOrderLine;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("saleship")
public class SaleShipController {

	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;
	
	private static final String DEPARTMENT_ASSEMBLY = "department.assembly";
	private static final String DEPARTMENT_WELDING = "department.welding";
	private static final String DEPARTMENT_MECHANICAL = "department.mechanical";
	private static final String DEPARTMENT_UNASSIGNED= "department.unassigned";
	
	private String[] assemblyCenters;
	private String[] weldingCenters;
	private String[] mechanicalCenters;
	
	
	public SaleShipController() {
		assemblyCenters = new String[]{"08","09","10","11","12","13"};
		weldingCenters = new String[]{"15","16","18","19"};
		mechanicalCenters = new String[]{"01","02","03","04","06","07","14"};
	}
	
	private boolean isStringInArray(String str, String[] array){
		boolean result = false;
		for(String s: array){
			if(s.equals(str)){
				return true;
			}
		}
		return result;
	}

	private String getMainDepartmentCodeByMachineCenter(String center){
		if(isStringInArray(center, assemblyCenters)){
			return DEPARTMENT_ASSEMBLY;
		}
		else if(isStringInArray(center, weldingCenters)){
			return DEPARTMENT_WELDING;
		}
		else if(isStringInArray(center, mechanicalCenters)){
			return DEPARTMENT_MECHANICAL;
		}
		else{
			return DEPARTMENT_UNASSIGNED;
		}
	}
	
	private X3RouteLine getLastRouteLineExcludingKAL(Map<Integer, X3RouteLine> routeLinesMap){
		X3RouteLine result = null;
	
		for(X3RouteLine entry: routeLinesMap.values()){
			if(entry.getMachine().startsWith("KAL")){
				if(result==null){
					result = entry;
				}
			}
			else {
				result = entry;
			}
		}
		return result;
		
	}
	
	@RequestMapping(value = "/main")
	public String showForm(Model model) {
		model.addAttribute("saleShipForm", new SaleShipForm());
		return "saleship/main";		
	}

	@RequestMapping(value = "/exec", method = RequestMethod.POST)
	@Transactional
	public String list(@Valid SaleShipForm saleShipForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		
		// validate
		if (bindingResult.hasErrors()) {
			return "saleship/main";
		}	
		
		List<X3SalesOrderLine> orderLines = x3Service.findOpenedSalesOrderLinesInPeriod(saleShipForm.getStartDate(), saleShipForm.getEndDate(), "ATW");
		Map<String, Map<Integer, X3RouteLine>> routes = x3Service.getRoutesMap("ATW");
		Map<String, String> workcenters = x3Service.getWorkcenterNumbersMapByMachines("ATW");
		
		for(Map<Integer, X3RouteLine> map: routes.values()){
			for(X3RouteLine line: map.values()){
				System.out.println("-" + line);
			}
			System.out.println("final operation: " + getLastRouteLineExcludingKAL(map));
		}
		
		return "saleship/main";
	}
	
	
}

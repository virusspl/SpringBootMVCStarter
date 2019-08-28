package sbs.controller.saleship;

import java.util.List;
import java.util.Locale;

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
	
	private List<X3Client> availableClients;
	
	@ModelAttribute("availableClients")
	public List<X3Client> availableClients() {
		return availableClients;
	}
	
	public SaleShipController() {
		
	}

	@RequestMapping(value = "/main")
	public String showForm(Model model) {
		//availableClients = x3Service.findAllClients("ATW");
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
		
		System.out.println(saleShipForm.getStartDate() + " - " + saleShipForm.getEndDate());
		
		//x3Service.findOpenedSalesOrderLinesInPeriod(saleShipForm.getStartDate(), saleShipForm.getEndDate(), "ATW");
		

		return "saleship/main";
	}
	
	
}

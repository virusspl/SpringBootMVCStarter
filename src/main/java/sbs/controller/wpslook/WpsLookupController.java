package sbs.controller.wpslook;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("/wpslook")
public class WpsLookupController {

	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service jdbcOracleX3Service;
	
	
	@RequestMapping("/search")
	public String search(WpsSearchForm WpsSearchForm) {
		return "wpslook/search";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String performSearch(@Valid WpsSearchForm WpsSearchForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Locale locale){
		String itemDesc;
		if(bindingResult.hasErrors()){
			return "wpslook/search";
		}
		
		WpsSearchForm.setProduct(WpsSearchForm.getProduct().toUpperCase());
		itemDesc = jdbcOracleX3Service.findItemDescription("WPS", WpsSearchForm.getProduct());
		
		if(itemDesc == null){
			bindingResult.rejectValue("product", "error.no.such.product", "ERROR");
			return "wpslook/search";
		}
		redirectAttrs.addFlashAttribute("wpsList", jdbcOracleX3Service.findLocationsOfProduct("WPS", WpsSearchForm.getProduct()));
		redirectAttrs.addFlashAttribute("itemCode", WpsSearchForm.getProduct());
		redirectAttrs.addFlashAttribute("itemDescription", itemDesc);

		return "redirect:/wpslook/search";
	}
	
	
}

package sbs.controller.geolook;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.service.JdbcOracleGeodeService;
import sbs.service.JdbcOracleX3Service;

@Controller
@RequestMapping("/geolook")
public class GeodeLookupController {

	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service jdbcOracleX3Service;
	@Autowired
	JdbcOracleGeodeService jdbcOracleGeodeService;
	
	@RequestMapping("/map")
	public String map(GeodeSearchForm geodeSearchForm) {
		return "geolook/map";
	}
	
	@RequestMapping("/search")
	public String search(GeodeSearchForm geodeSearchForm) {
		return "geolook/search";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String performSearch(@Valid GeodeSearchForm geodeSearchForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Locale locale){
		String itemDesc;
		if(bindingResult.hasErrors()){
			return "geolook/search";
		}
		
		geodeSearchForm.setProduct(geodeSearchForm.getProduct().toUpperCase());
		itemDesc = jdbcOracleX3Service.findItemDescription("ATW", geodeSearchForm.getProduct());
		
		if(itemDesc == null){
			bindingResult.rejectValue("product", "error.no.such.product", "ERROR");
			return "geolook/search";
		}
		redirectAttrs.addFlashAttribute("geodeList", jdbcOracleGeodeService.findLocationsOfProduct(geodeSearchForm.getProduct()));
		redirectAttrs.addFlashAttribute("itemCode", geodeSearchForm.getProduct());
		redirectAttrs.addFlashAttribute("itemDescription", itemDesc);

		return "redirect:/geolook/search";
	}
	
	
}

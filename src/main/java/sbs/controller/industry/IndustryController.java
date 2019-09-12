package sbs.controller.industry;

import java.util.Locale;

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

import sbs.controller.industry.HrUserInfoSessionHolder;
import sbs.model.hr.HrUserInfo;
import sbs.service.optima.JdbcAdrOptimaService;

@Controller
@RequestMapping("industry")
public class IndustryController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcAdrOptimaService optimaService;
	@Autowired
	HrUserInfoSessionHolder userHolder;

	@ModelAttribute("userHolder")
	public HrUserInfoSessionHolder getUserHolder(){
	    return userHolder;
	}
	
	@RequestMapping(value = "/dispatch")
	public String dispatch(Model model) {
		if (userHolder.isSet()) {
			return "industry/dispatch";
		} else {
			model.addAttribute("rcpCardForm", new RcpCardForm());
			return "industry/readcard";
		}
	}
	
	@RequestMapping(value = "/rcplogin", method = RequestMethod.POST)
	public String findUser(@Valid RcpCardForm rcpCardForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale) {
		
		String cardNo = rcpCardForm.getCardNumber().trim().toUpperCase();
		HrUserInfo hrInfo = optimaService.findCurrentlyEmployedByCardNo(cardNo);
		if(hrInfo != null){
			userHolder.setInfo(hrInfo);
			return "redirect:/industry/dispatch";
		}
		else{
			bindingResult.rejectValue("cardNumber", "error.rcp.incorrect", "ERROR");
			return "industry/readcard";
		}

	}
	
	@RequestMapping(value = "/clearrcp")
	public String clearHolder() {
		this.userHolder.clear();
		return "redirect:/industry/dispatch";
	}
	
	
}

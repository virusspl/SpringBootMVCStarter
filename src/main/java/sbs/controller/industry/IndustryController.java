package sbs.controller.industry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.controller.auth.HrUserInfoSessionHolder;

@Controller
@RequestMapping("industry")
public class IndustryController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	HrUserInfoSessionHolder userHolder;

	@ModelAttribute("userHolder")
	public HrUserInfoSessionHolder getUserHolder(){
	    return userHolder;
	}
	
	@RequestMapping(value = "/dispatch")
	public String dispatch(Model model) {
			return "industry/dispatch";
	}
	
	
}

package sbs.controller.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.controller.industry.HrUserInfoSessionHolder;
import sbs.service.optima.JdbcAdrOptimaService;

@Controller
@RequestMapping("downtimes")
public class DowntimesController {

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
	public String dispatch() {
		if (userHolder.isSet()) {
			return "downtimes/dispatch";
		} else {
			return "redirect:/industry/dispatch";
		}
	}
}

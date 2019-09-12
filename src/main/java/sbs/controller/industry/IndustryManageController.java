package sbs.controller.industry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.service.optima.JdbcAdrOptimaService;

@Controller
@RequestMapping("industry/manage")
public class IndustryManageController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcAdrOptimaService optimaService;

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
			return "industry/dispatch.manage";
	}

	
}

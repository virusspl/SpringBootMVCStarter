package sbs.controller.industry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.service.optima.JdbcOptimaService;

@Controller
@RequestMapping("industry/manage")
public class IndustryManageController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOptimaService optimaService;

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
			return "industry/dispatch.manage";
	}

	
}

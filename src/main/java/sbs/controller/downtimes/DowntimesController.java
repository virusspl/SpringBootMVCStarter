package sbs.controller.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.helpers.TextHelper;

@Controller
@RequestMapping("downtimes")
public class DowntimesController {
	
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		return "bhptickets/dispatch";
	}

}

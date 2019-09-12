package sbs.controller.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("downtimes/manage")
public class DowntimesManageController {

	@Autowired
	MessageSource messageSource;
	
	@RequestMapping(value = "causes")
	public String dshowManageCauses() {
		return "downtimes/causes";
	}
}

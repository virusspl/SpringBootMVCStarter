package sbs.controller.various;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VariousPagesController {

	@RequestMapping("/noaccess")
	public String noAccess() {
		return "various/noaccess";
	}
	
	@RequestMapping("/underconstruction")
	public String underConstruction() {
		return "various/construction";
	}
}

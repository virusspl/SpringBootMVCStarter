package sbs.controller.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * show welcome view
	 * @return welcome view
	 */
	@RequestMapping("/")	
	public String home() {
		//return "redirect:/init";
		return "welcome";
	}
	
	/**
	 * show terminal view
	 * @return welcome view
	 */
	@RequestMapping("/terminal")	
	public String terminal() {
		return "terminal/welcome_terminal";
	}
}
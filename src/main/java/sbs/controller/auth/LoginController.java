package sbs.controller.auth;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.service.users.RoleService;
import sbs.service.users.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	MessageSource messageSource;

	@RequestMapping("/login")
	public String authenticate() {
		return "various/login";
	}
	
	@RequestMapping("/logout")
	public String logout(Model model, Locale locale) {
		model.addAttribute("msg", messageSource.getMessage("action.logged.out", null, locale));
		return "welcome";
	}	
}

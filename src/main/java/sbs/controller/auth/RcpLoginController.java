package sbs.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RcpLoginController {

	@Autowired
	UserDetailsService userDetailsService;

	@RequestMapping("/rcplogin")
	public String authenticate(Model model) {
		UserDetails user = userDetailsService.loadUserByUsername("Krzysie_k");
		Authentication auth = 
				  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
		model.addAttribute("error", "cześć KRZYSIEK?!");
		return "welcome";
	}
	
	
}

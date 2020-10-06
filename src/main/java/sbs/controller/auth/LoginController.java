package sbs.controller.auth;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.model.hr.HrUserInfo;
import sbs.model.users.User;
import sbs.service.optima.JdbcOptimaService;
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
	@Autowired
	HrUserInfoSessionHolder userHolder;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	JdbcOptimaService optimaService;


	@RequestMapping(value = "/readcard")
	public String showRcpLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
	        model.addAttribute("rcpCardForm", new RcpCardForm());
		return "various/readcard";
	}
	
	@RequestMapping("/login")
	public String login() {
		this.userHolder.clear();
		return "various/login";
	}	

	@RequestMapping(value = "/rcplogin", method = RequestMethod.POST)
	public String findUser(@Valid RcpCardForm rcpCardForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale) {

		String message = "";

		String cardNo = rcpCardForm.getCardNumber().trim().toUpperCase();
		HrUserInfo hrInfo = optimaService.findCurrentlyEmployedByCardNo(cardNo, JdbcOptimaService.DB_ADR);
		if (hrInfo != null) {
			userHolder.setInfo(hrInfo);
			message = messageSource.getMessage("rcplogin.userfound", null, locale) + ": <b>"
					+ userHolder.getInfo().getFirstName() + " " + userHolder.getInfo().getLastName() + "</b>";
		} else {
			bindingResult.rejectValue("cardNumber", "error.rcp.incorrect", "ERROR");
			return "various/readcard";
		}

		User user = userService.findByRcpNumber(userHolder.getInfo().getRcpNumber());
		if (user != null) {
			UserDetails principal = userDetailsService.loadUserByUsername(user.getUsername());
			Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			message += ". " + messageSource.getMessage("rcplogin.usermatch", null, locale) + ": <b>" + user.getUsername() + "</b>";
		} else {
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("rcplogin.nousermatch", null, locale));
		}

		// redirect
		redirectAttrs.addFlashAttribute("msg", message);
		return "redirect:/";

	}

	@RequestMapping("/postlogin")
	public String postlogin(RedirectAttributes redirectAttrs, Locale locale) {
		User current = userService.getAuthenticatedUser();
		if(current != null && current.getRcpNumber().length()>0) {
			String message = ""; 
			HrUserInfo hrInfo = optimaService.findCurrentlyEmployedByCardNo(current.getRcpNumber(), JdbcOptimaService.DB_ADR);
			if (hrInfo != null) {
				userHolder.setInfo(hrInfo);
				message = messageSource.getMessage("rcplogin.userfound", null, locale) + ": <b>"
						+ userHolder.getInfo().getFirstName() + " " + userHolder.getInfo().getLastName() + "</b>";
				redirectAttrs.addFlashAttribute("msg", message);
			}
		}
		
		return "redirect:/";
	}
	
	@RequestMapping("/logout")
	public String logout(RedirectAttributes redirectAttrs, Locale locale) {
		this.userHolder.clear();
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.logged.out", null, locale));
		return "redirect:/";
	}
	
	
	
	@RequestMapping("/tlogin")
	public String authenticateTerminal() {
		return "various/login_terminal";
	}
	
	@RequestMapping("/tlogout")
	public String logoutTerminal() {
		return "terminal/welcome_terminal";
	}

}

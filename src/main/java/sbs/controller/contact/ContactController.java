package sbs.controller.contact;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;


import sbs.model.users.User;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;



@Controller
public class ContactController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	MessageSource messageSource;
	@Autowired
	MailService mailService;
	@Autowired
	UserService userService;
	
	@RequestMapping("/contact")
	public String showBlog(Model model, HttpServletRequest request) {
		User user;
		ContactForm contactForm = new ContactForm();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			user = userService.findByUsername(auth.getName());
			contactForm.setName(user.getName());
			contactForm.setEmail(user.getEmail());
		}
		contactForm.setIp(request.getRemoteAddr());
		model.addAttribute("contactForm", contactForm);
		return "contact";
	}
	
	@RequestMapping("/contact/send")
	public String sendEmail(@Valid ContactForm contactForm, BindingResult bindingResult, Model model, HttpServletRequest request, Locale locale) {
		model.addAttribute("clientAddr", request.getRemoteAddr());
			if (bindingResult.hasErrors()) {
				model.addAttribute("mailerror", "");
				return "contact";
			}
			String content = "<html><b>" + contactForm.getName()+ "</b>: " +contactForm.getContent()+ " (" + contactForm.getEmail()+")</html>";
			try {
				mailService.sendEmail(
						contactForm.getEmail(), 
						new String[]{"michalak.k@atwsystem.pl"},
						new String[]{},
						"SpringBootStarter Contact Form", 
						content);
			} catch (MessagingException e) {
				log.debug("e-mail send error", e);
			}
			
			model.addAttribute("msg",messageSource.getMessage("contact.sent", null, locale)
					+
					". "
					+
					messageSource.getMessage("contact.thank.you", null, locale));
			return "welcome";
	}
}
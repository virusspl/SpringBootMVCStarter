package sbs.controller.contact;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.service.MailService;



@Controller
public class ContactController {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	MessageSource messageSource;
	@Autowired
	MailService mailService;
	
	@RequestMapping("/contact")
	public String showBlog(Model model, HttpServletRequest request) {
		model.addAttribute("clientAddr", request.getRemoteAddr());
		model.addAttribute("contactForm", new ContactForm());
		return "contact";
	}
	
	@RequestMapping("/contact/send")
	public String sendEmail(@Valid ContactForm contactForm, BindingResult bindingResult, Model model, HttpServletRequest request, Locale locale) {
		model.addAttribute("clientAddr", request.getRemoteAddr());
			if (bindingResult.hasErrors()) {
				return "contact";
			}
			 
			mailService.sendEmail(
					"no-reply@sjava.herokuapp.com", 
					"viruss.snk@gmail.com", 
					"Spring-Java contact form E-Mail", 
					contactForm.getName()+ ": " +contactForm.getContent()+ " (" + contactForm.getEmail()+")");
		model.addAttribute("title",messageSource.getMessage("contact.sent", null, locale));
		model.addAttribute("message",messageSource.getMessage("contact.thank.you", null, locale));
		return "message";
	}
}
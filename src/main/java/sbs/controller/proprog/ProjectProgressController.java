package sbs.controller.proprog;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javassist.NotFoundException;
import sbs.model.proprog.Project;
import sbs.model.users.User;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

/**
 * TO USE THIS CONTROLLER, SBS.MODEL.PROJECTENTITY IS REQUIRED TO BE USED. SWAP
 * ITS NAME TO SBS.MODEL.PROJECT ALSO ERASE 'ENTITYVERSION' IN THIS CLASS
 * 
 * @author Michalakk
 *
 */

@Controller
@RequestMapping("proprog")
public class ProjectProgressController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	UserService userService;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;

	@RequestMapping(value = "/list")
	@Transactional
	public String listAll(Model model) {
		ArrayList<ListItem> projects = new ArrayList<>();
		List<Project> list = x3Service.findPendingProjectsProgress();
		for (Project project : list) {
			projects.add(new ListItem(project));
		}
		model.addAttribute("projects", projects);
		return "proprog/list";
	}

	@RequestMapping(value = "/view/{id}")
	@Transactional
	public String view(@PathVariable("id") String number, Model model, Locale locale) throws NotFoundException {
		Project project = x3Service.findProjectProgressByNumber(number);
		if (project == null) {
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		model.addAttribute("project", project);
		model.addAttribute("progress", project.getProgressTotal());
		model.addAttribute("color", project.getProgressBootstrapTitle());
		return "proprog/view";
	}

	@RequestMapping(value = "/sendemails")
	@Transactional
	public String sendEmails(HttpServletRequest request, RedirectAttributes redirectAttrs, Locale locale) throws MessagingException, UnknownHostException {
		// projects
		ArrayList<ListItem> projects = new ArrayList<>();
		List<Project> list = x3Service.findPendingProjectsProgress();
		for (Project project : list) {
			projects.add(new ListItem(project));
		}

		// users list
		ArrayList<String> mailingList = new ArrayList<>();
		List<User> users = userService.findByAnyRole(new String[] { "ROLE_PROPROG" });
		for (User user : users) {
			mailingList.add(user.getEmail());
		}
		// supervisors list
		users = userService.findByAnyRole(new String[] { "ROLE_PROPROGSUPERVISOR" });
		ArrayList<String> mailingCCList = new ArrayList<>();
		for (User user : users) {
			mailingCCList.add(user.getEmail());
		}
		
		// send e-mail
		Context context = new Context();
		context.setVariable("projects", projects);
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		String body = templateEngine.process("proprog/mailtemplate", context);
		mailService.sendEmail("webapp@atwsystem.pl", mailingList.toArray(new String[0]),
				mailingCCList.toArray(new String[0]), "ADR Polska S.A. - Projekty", body);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("email.sent", null, locale));
		
		return "redirect:/proprog/list";
	}
}

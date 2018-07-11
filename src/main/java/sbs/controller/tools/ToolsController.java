package sbs.controller.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javassist.NotFoundException;
import sbs.controller.upload.UploadController;
import sbs.helpers.TextHelper;
import sbs.model.bhptickets.BhpTicket;
import sbs.model.bhptickets.BhpTicketState;
import sbs.model.users.User;
import sbs.service.bhptickets.BhpTicketStateService;
import sbs.service.bhptickets.BhpTicketsService;
import sbs.service.mail.MailService;
import sbs.service.tools.ToolsProjectService;
import sbs.service.tools.ToolsProjectStateService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("tools")
public class ToolsController {
	@Autowired
	ToolsProjectService toolsProjectService;
	@Autowired
	ToolsProjectStateService toolsProjectStateService;
	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	UploadController uploadController;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	TextHelper textHelper;

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		return "tools/dispatch";
	}
	
	@RequestMapping(value = "/manager")
	@Transactional
	public String managerView(Model model) {
		model.addAttribute("title", "manager");
		return "tools/manager";
	}
	
	@RequestMapping(value = "/user")
	@Transactional
	public String userView(Model model) {
		model.addAttribute("title", "user");
		return "tools/user";
	}

}

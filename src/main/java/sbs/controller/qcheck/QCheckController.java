package sbs.controller.qcheck;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javassist.NotFoundException;
import sbs.controller.bhptickets.UserTicketsHolder;
import sbs.model.qcheck.QCheck;
import sbs.model.qcheck.QCheckAction;
import sbs.model.qcheck.QCheckState;
import sbs.model.users.User;
import sbs.model.x3.X3Product;
import sbs.service.mail.MailService;
import sbs.service.qcheck.QCheckActionsService;
import sbs.service.qcheck.QCheckService;
import sbs.service.qcheck.QCheckStatesService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("/qcheck")
public class QCheckController {

	@Autowired
	UserService userService;
	@Autowired
	QCheckService checksService;
	@Autowired
	QCheckStatesService statesService;
	@Autowired
	QCheckActionsService actionsService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service jdbcOracleX3Service;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;

	List<String> possibleActions;

	public QCheckController() {
		possibleActions = new ArrayList<>();
		possibleActions.add("start");
		possibleActions.add("reject");
		possibleActions.add("suspend");
		possibleActions.add("markok");
		possibleActions.add("markbad");
		possibleActions.add("checkagain");
	}

	@RequestMapping("/list")
	public String current(Model model) {
		model.addAttribute("list", checksService.findAllPending());
		return "qcheck/list";
	}

	@RequestMapping("/tocorrect")
	public String tocorrect(Model model) {
		model.addAttribute("list", checksService.findAllWithStateOrder(40));
		return "qcheck/list";
	}

	@RequestMapping("/archive")
	public String closed(Model model) {
		model.addAttribute("list", checksService.findAllClosed());
		return "qcheck/list";
	}

	@RequestMapping(value = "/create")
	@Transactional
	public String create(Model model) {
		model.addAttribute("QCheckCreateForm", new QCheckCreateForm());
		return "qcheck/create";
	}

	@RequestMapping(value = "/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String save(@Valid QCheckCreateForm qCheckCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		// validate
		if (bindingResult.hasErrors()) {
			return "qcheck/create";
		}

		X3Product product = jdbcOracleX3Service.findProductByCode("ATW", qCheckCreateForm.getProductCode().trim());
		if (product == null) {
			bindingResult.rejectValue("productCode", "error.no.such.product", "ERROR");
			return "qcheck/create";
		}

		QCheckState stateNew = statesService.findByOrder(10);
		if (stateNew == null) {
			throw new NotFoundException("state \"new\" not found");
		}

		QCheck check = new QCheck();
		check.setProductCode(product.getCode());
		check.setProductDescription(product.getDescription());
		check.setContents(qCheckCreateForm.getContents().trim());
		check.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		check.setCreator(userService.getAuthenticatedUser());
		check.setCurrentState(stateNew);
		checksService.save(check);

		QCheckAction action = new QCheckAction();

		action.setUser(userService.getAuthenticatedUser());
		action.setTime(new Timestamp(new java.util.Date().getTime()));
		action.setComment("");
		action.setState(statesService.findByOrder(10));
		action.setCheck(check);
		check.getActions().add(action);
		actionsService.save(action);
		checksService.save(check);

		try {
			sendMail(check, action.getTime(), action.getComment(), locale);
		} catch (UnknownHostException | MessagingException e) {
			redirectAttrs.addFlashAttribute("warning", e.getMessage());
		}
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qcheck/show/" + check.getId();
	}

	@RequestMapping(value = "/show/{id}")
	@Transactional
	public String answer(@PathVariable("id") int id, ResponseForm responseForm, Model model, Locale locale)
			throws NotFoundException {
		QCheck check = checksService.findById(id);
		if (check == null) {
			throw new NotFoundException(messageSource.getMessage("qcheck.notfound", null, locale));
		}

		addPossibleActions(check, model);
		model.addAttribute("check", check);
		model.addAttribute("actions", actionsService.findByCheckId(check.getId()));
		model.addAttribute("QCheckActionForm", new QCheckActionForm());

		return "qcheck/show";
	}

	@RequestMapping(value = "/action", method = RequestMethod.POST)
	@Transactional
	public String save(@Valid QCheckActionForm qCheckActionForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, @RequestParam Map<String, String> allParams, Locale locale, Model model)
			throws NotFoundException {

		int id;
		id = Integer.parseInt(allParams.get(checkAction(allParams)));

		QCheck check = checksService.findById(id);
		if (check == null) {
			throw new NotFoundException(
					messageSource.getMessage("qcheck.notfound", null, locale) + "[#id = " + id + "]");
		}

		// validate
		if (bindingResult.hasErrors()) {
			addPossibleActions(check, model);
			model.addAttribute("check", check);
			model.addAttribute("actions", actionsService.findByCheckId(check.getId()));
			return "qcheck/show";
		}

		QCheckAction action = new QCheckAction();

		action.setUser(userService.getAuthenticatedUser());
		action.setTime(new Timestamp(new java.util.Date().getTime()));
		action.setComment(qCheckActionForm.getComment().trim());

		int stateOrder;
		switch (checkAction(allParams)) {
		case "start":
			stateOrder = 20;
			check.setCurrentUser(action.getUser());
			break;
		case "reject":
			stateOrder = 90;
			break;
		case "suspend":
			stateOrder = 30;
			check.setCurrentUser(null);
			break;
		case "markok":
			stateOrder = 50;
			check.setCurrentUser(null);
			break;
		case "markbad":
			stateOrder = 40;
			check.setCurrentUser(null);
			break;
		case "checkagain":
			stateOrder = 10;
			break;
		default:
			stateOrder = 10;
			break;
		}

		action.setState(statesService.findByOrder(stateOrder));
		check.setCurrentState(action.getState());
		action.setCheck(check);
		check.getActions().add(action);
		actionsService.save(action);
		checksService.save(check);

		try {
			sendMail(check, action.getTime(), action.getComment(), locale);
		} catch (UnknownHostException | MessagingException e) {
			redirectAttrs.addFlashAttribute("warning", e.getMessage());
		}

		return "redirect:/qcheck/show/" + id;
	}

	private String checkAction(Map<String, String> allParams) {
		for (String param : allParams.keySet()) {
			if (possibleActions.contains(param)) {
				return param;
			}
		}
		return null;
	}

	private void addPossibleActions(QCheck check, Model model) {
		boolean qualityRole = userService.getAuthenticatedUser().hasRole("ROLE_QCHECKMANAGER");
		boolean userRole = userService.getAuthenticatedUser().hasRole("ROLE_QCHECKUSER");

		switch (check.getCurrentState().getCode()) {
		case "qcheck.state.tocheck":
			if (qualityRole) {
				model.addAttribute("action", true);
				model.addAttribute("start", true);
				model.addAttribute("reject", true);
			} else {
				model.addAttribute("action", false);
			}
			break;
		case "qcheck.state.inprogress":
			if (check.getCurrentUser() != null && qualityRole
					&& check.getCurrentUser().getId() == userService.getAuthenticatedUser().getId()) {
				model.addAttribute("action", true);
				model.addAttribute("suspend", true);
				model.addAttribute("markok", true);
				model.addAttribute("markbad", true);
			} else {
				model.addAttribute("action", false);
			}
			break;
		case "qcheck.state.suspended":
			if (qualityRole) {
				model.addAttribute("action", true);
				model.addAttribute("start", true);
			} else {
				model.addAttribute("action", false);
			}
			break;
		case "qcheck.state.tocorrect":
			if (userRole && check.getCreator().getId() == userService.getAuthenticatedUser().getId()) {
				model.addAttribute("action", true);
				model.addAttribute("checkagain", true);
			} else {
				model.addAttribute("action", false);
			}
			break;
		case "qcheck.state.checked":
		case "qcheck.state.rejected":
			model.addAttribute("action", false);
		default:
			model.addAttribute("unknownstate", check.getCurrentState().getCode());
			break;
		}
	}

	private void sendMail(QCheck check, Timestamp time, String comment, Locale locale)
			throws UnknownHostException, MessagingException {

		List<User> qualityUsers = userService.findByRole("ROLE_QCHECKMANAGER");
		ArrayList<String> mailCC = new ArrayList<>();
		for (User user : qualityUsers) {
			mailCC.add(user.getEmail());
		}

		ArrayList<String> mailTO = new ArrayList<>();
		mailTO.add(check.getCreator().getEmail());

		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("time", time);
		context.setVariable("comment", comment);
		context.setVariable("check", check);
		String body = templateEngine.process("qcheck/mailtemplate", context);
		mailService.sendEmail("webapp@atwsystem.pl", mailTO.toArray(new String[0]), mailCC.toArray(new String[0]),
				"QCheck #" + check.getId() + " - " + messageSource.getMessage(check.getCurrentState().getCode(), null, locale),
				body);
	}

}

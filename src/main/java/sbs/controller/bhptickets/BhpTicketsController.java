package sbs.controller.bhptickets;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import sbs.model.bhptickets.BhpTicket;
import sbs.model.bhptickets.BhpTicketState;
import sbs.model.users.User;
import sbs.service.bhptickets.BhpTicketStateService;
import sbs.service.bhptickets.BhpTicketsService;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
@RequestMapping("bhptickets")
public class BhpTicketsController {
	@Autowired
	BhpTicketsService bhpTicketsService;
	@Autowired
	BhpTicketStateService bhpTicketStateService;
	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	UploadController uploadController;
	@Autowired 
	MailService mailService;
	@Autowired TemplateEngine templateEngine;
	

	
	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		return "bhptickets/dispatch";
	}

	@RequestMapping("/sendemails")
	@Transactional
	public String sendEmails(HttpServletRequest request) throws MessagingException, UnknownHostException {
		Set<User> users = bhpTicketsService.findAllPendingTicketsUsers();
		ArrayList<UserTicketsHolder> holders = new ArrayList<>();
		UserTicketsHolder holder;
		for(User user: users){
			holder = new UserTicketsHolder();
			holder.setUser(user);
			holder.setTickets(bhpTicketsService.findPendingTicketsByUser(user));
			holders.add(holder);
		}
		
		  	Context context = new Context();
	        context.setVariable("description", "Tutaj jakis opis...");
	        context.setVariable("loop", holders);
			context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
	        String body = templateEngine.process("bhptickets/mailtemplate", context);
	        mailService.sendEmail("webapp@atwsystem.pl", new String[]{"michalak.k@atwsystem.pl","michalak.k@atwsystem.pl"}, new String[]{"michalak.k@atwsystem.pl"}, "Compiled template", body);
		return "redirect:/bhptickets/dispatch";
	}
	
	@RequestMapping(value = "/list")
	@Transactional
	public String listAll(Model model) {
		model.addAttribute("tickets", bhpTicketsService.findAll());
		return "bhptickets/list";
	}

	@RequestMapping(value = "/create")
	public String createTicket(Model model, Locale locale) {
		model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
		model.addAttribute("ticketCreateForm", new TicketCreateForm());
		return "bhptickets/create";
	}

	@RequestMapping("/edit/{id}")
	@Transactional
	public String showBhpTicketEditForm(@PathVariable("id") int id, Model model) throws NotFoundException {
		BhpTicket ticket = bhpTicketsService.findById(id);
		if (ticket == null) {
			throw new NotFoundException("Ticket not found");
		}
		TicketCreateForm ticketCreateForm = new TicketCreateForm();
		ticketCreateForm.setTitle(ticket.getTitle());
		ticketCreateForm.setDescription(ticket.getDescription());
		ticketCreateForm.setAssignedUser(ticket.getAssignedUser().getId());
		ticketCreateForm.setToDoDate(ticket.getToDoDate());
		ticketCreateForm.setToSend(ticket.getToSend());
		ticketCreateForm.setId(ticket.getId());
		ticketCreateForm.setStateDescription(ticket.getState().getDescription());
		ticketCreateForm.setStateOrder(ticket.getState().getOrder());
		model.addAttribute("ticketCreateForm", ticketCreateForm);
		model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));

		return "bhptickets/edit";
	}

	@RequestMapping("/edit/photos/{id}")
	@Transactional
	public String showBhpTicketPhotosForm(@PathVariable("id") int id, Model model) throws NotFoundException {
		BhpTicket ticket = bhpTicketsService.findById(id);
		if (ticket == null) {
			throw new NotFoundException("Ticket not found");
		}
		model.addAttribute("ticket", ticket);

		ArrayList<String> fileList = uploadController.listFiles(uploadController.getBhpPhotoPath());
		for (int i = fileList.size() - 1; i >= 0; i--) {
			if (!fileList.get(i).startsWith("bhp_" + id + "_")) {
				fileList.remove(i);
			}
		}
		model.addAttribute("photos", fileList);
		return "bhptickets/photos";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional
	public String create(@Valid TicketCreateForm ticketCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/create";
		}

		BhpTicket ticket = new BhpTicket();

		// fields
		Timestamp credat = new Timestamp(new java.util.Date().getTime());
		ticket.setCreationDate(credat);
		ticket.setUpdateDate(credat);
		ticket.setToDoDate(new Timestamp(ticketCreateForm.getToDoDate().getTime()));
		ticket.setTitle(ticketCreateForm.getTitle());
		ticket.setDescription(ticketCreateForm.getDescription());
		ticket.setComment("");
		ticket.setToSend(true);
		// relations
		// state
		BhpTicketState state = bhpTicketStateService.findByOrder(10);
		state.getTickets().add(ticket);
		ticket.setState(state);
		// creator
		User creator = userService.getAuthenticatedUser();
		creator.getCreatedBhpTickets().add(ticket);
		ticket.setCreator(creator);
		// assigned user
		User assignedUser = userService.findById(ticketCreateForm.getAssignedUser());
		if (assignedUser == null) {
			bindingResult.rejectValue("assignedUser", "error.user.not.found", "ERROR");
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/create";
		}

		assignedUser.getAssignedBhpTickets().add(ticket);
		ticket.setAssignedUser(assignedUser);

		// save
		bhpTicketsService.save(ticket);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/edit/" + ticket.getId();
	}

	@RequestMapping(value = "/edit", params = { "cancel" }, method = RequestMethod.POST)
	@Transactional
	public String cancel(@Valid TicketCreateForm ticketEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		BhpTicket ticket = bhpTicketsService.findById(ticketEditForm.getId());
		BhpTicketState cancelState = bhpTicketStateService.findByOrder(90);
		ticket.setState(cancelState);
		ticket.setToSend(false);
		cancelState.getTickets().add(ticket);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/list";
		
	}
	
	@RequestMapping(value = "/edit", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String edit(@Valid TicketCreateForm ticketEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/edit";
		}

		BhpTicket ticket = bhpTicketsService.findById(ticketEditForm.getId());

		ticket.setToDoDate(new Timestamp(ticketEditForm.getToDoDate().getTime()));
		ticket.setTitle(ticketEditForm.getTitle());
		ticket.setDescription(ticketEditForm.getDescription());
		ticket.setToSend(ticketEditForm.isToSend());

		// assigned user
		User newAssignedUser = userService.findById(ticketEditForm.getAssignedUser());
		if (newAssignedUser == null) {
			bindingResult.rejectValue("assignedUser", "error.user.not.found", "ERROR");
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/create";
		} else if (ticket.getAssignedUser().getId() != newAssignedUser.getId()) {
			newAssignedUser.getAssignedBhpTickets().add(ticket);
			ticket.setAssignedUser(newAssignedUser);
		}

		// save
		bhpTicketsService.saveOrUpdate(ticket);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/edit/" + ticket.getId();
	}

	@RequestMapping("/show/{id}")
	@Transactional
	public String showBhpTicket(@PathVariable("id") int id, Model model) throws NotFoundException {
		TicketResponseForm responseForm = new TicketResponseForm();
		// get ticket
		BhpTicket ticket = bhpTicketsService.findById(id);
		if (ticket == null) {
			throw new NotFoundException("Ticket not found");
		}
		// set viewed state and view date + decide of modification
		User authUser = userService.getAuthenticatedUser();
		if (authUser == null || (authUser.getId() != ticket.getAssignedUser().getId())
				|| (ticket.getState().getOrder() >= 40)) {
			responseForm.setModificationAllowed(false);
		} else {
			responseForm.setId(ticket.getId());
			responseForm.setModificationAllowed(true);
			responseForm.setComment(ticket.getComment());
			if (ticket.getState().getOrder() < 20) {
				BhpTicketState stateViewed = bhpTicketStateService.findByOrder(20);
				ticket.setState(stateViewed);
				ticket.setUpdateDate(new Timestamp(new java.util.Date().getTime()));
				stateViewed.getTickets().add(ticket);
			}
		}
		// get photos list
		ArrayList<String> fileList = uploadController.listFiles(uploadController.getBhpPhotoPath());
		for (int i = fileList.size() - 1; i >= 0; i--) {
			if (!fileList.get(i).startsWith("bhp_" + id + "_")) {
				fileList.remove(i);
			}
		}
		model.addAttribute("ticketResponseForm", responseForm);
		model.addAttribute("photos", fileList);
		model.addAttribute("ticket", ticket);
		return "bhptickets/show";
	}

	@RequestMapping(value = "/response", params = { "passticket" }, method = RequestMethod.POST)
	@Transactional
	public String passTicket(@Valid TicketResponseForm ticketResponseForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		// get ticket
		BhpTicket ticket = bhpTicketsService.findById(ticketResponseForm.getId());
		// validate
		if (bindingResult.hasErrors()) {
			ArrayList<String> fileList = uploadController.listFiles(uploadController.getBhpPhotoPath());
			for (int i = fileList.size() - 1; i >= 0; i--) {
				if (!fileList.get(i).startsWith("bhp_" + ticketResponseForm.getId() + "_")) {
					fileList.remove(i);
				}
			}
			model.addAttribute("photos", fileList);
			model.addAttribute("ticket", ticket);
			return "bhptickets/show";
		}
		BhpTicketState passedState = bhpTicketStateService.findByOrder(30);
		passedState.getTickets().add(ticket);
		ticket.setState(passedState);
		ticket.setUpdateDate(new Timestamp(new java.util.Date().getTime()));
		ticket.setComment(ticketResponseForm.getComment().trim());
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("bhp.tickets.confirm.pass", null, locale));
		return "redirect:/bhptickets/list";
	}

	@RequestMapping(value = "/response", params = { "closeticket" }, method = RequestMethod.POST)
	@Transactional
	public String closeTicket(@Valid TicketResponseForm ticketResponseForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		// get ticket
		BhpTicket ticket = bhpTicketsService.findById(ticketResponseForm.getId());
		// validate
		if (bindingResult.hasErrors()) {
			ArrayList<String> fileList = uploadController.listFiles(uploadController.getBhpPhotoPath());
			for (int i = fileList.size() - 1; i >= 0; i--) {
				if (!fileList.get(i).startsWith("bhp_" + ticketResponseForm.getId() + "_")) {
					fileList.remove(i);
				}
			}
			model.addAttribute("photos", fileList);
			model.addAttribute("ticket", ticket);
			return "bhptickets/show";
		}
		BhpTicketState closedState = bhpTicketStateService.findByOrder(40);
		closedState.getTickets().add(ticket);
		ticket.setState(closedState);
		ticket.setUpdateDate(new Timestamp(new java.util.Date().getTime()));
		ticket.setToSend(false);
		ticket.setComment(ticketResponseForm.getComment().trim());
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("bhp.tickets.confirm.success", null, locale));
		return "redirect:/bhptickets/list";
	}

}

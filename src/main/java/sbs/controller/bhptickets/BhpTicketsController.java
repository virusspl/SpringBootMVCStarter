package sbs.controller.bhptickets;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
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
import org.springframework.core.io.DefaultResourceLoader;
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
import sbs.helpers.DateHelper;
import sbs.helpers.TextHelper;
import sbs.model.bhptickets.BhpTicket;
import sbs.model.bhptickets.BhpTicketState;
import sbs.model.users.User;
import sbs.service.bhptickets.BhpTicketStateService;
import sbs.service.bhptickets.BhpTicketsService;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;

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
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		return "bhptickets/dispatch";
	}

	@RequestMapping("/sendemails")
	@Transactional
	public String sendEmails(HttpServletRequest request, RedirectAttributes redirectAttrs, Locale locale)
			throws MessagingException, UnknownHostException {
		Set<User> users = bhpTicketsService.findAllPendingTicketsUsers();
		ArrayList<UserTicketsHolder> holders = new ArrayList<>();
		ArrayList<String> mailingList = new ArrayList<>();
		UserTicketsHolder holder;
		for (User user : users) {
			holder = new UserTicketsHolder();
			holder.setUser(user);
			holder.setTickets(bhpTicketsService.findPendingTicketsByUser(user));
			holders.add(holder);
			mailingList.add(user.getEmail());
		}

		List<User> supervisors = userService
				.findByAnyRole(new String[] { "ROLE_BHPMANAGER", "ROLE_BHPSUPERVISOR", "ROLE_BHPTICKETSUTRUSER" });
		ArrayList<String> supervisorsMailingList = new ArrayList<>();
		for (User sprv : supervisors) {
			supervisorsMailingList.add(sprv.getEmail());
		}
		Context context = new Context();
		context.setVariable("loop", holders);
		//context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("host", InetAddress.getLocalHost().getHostName());
		String body = templateEngine.process("bhptickets/mailtemplate", context);
		mailService.sendEmail("webapp@atwsystem.pl", mailingList.toArray(new String[0]),
				supervisorsMailingList.toArray(new String[0]), "ADR Polska S.A. - BHP", body);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("email.sent", null, locale));

		return "redirect:/bhptickets/dispatch";
	}

	@RequestMapping(value = "/list")
	@Transactional
	public String listAllPending(Model model) {
		User user = userService.getAuthenticatedUser();
		if (userService.hasAnyRole(user, Arrays.asList("ROLE_ADMIN", "ROLE_BHPMANAGER", "ROLE_BHPSUPERVISOR"))) {
			model.addAttribute("tickets", bhpTicketsService.findAllNotArchivedTickets());
		} else if (userService.hasAnyRole(user, Arrays.asList("ROLE_BHPTICKETSUTRUSER"))) {
			model.addAttribute("tickets", bhpTicketsService.findPendingUtrTickets());
		} else {
			model.addAttribute("tickets", bhpTicketsService.findPendingTicketsByUser(user));
		}

		return "bhptickets/list";
	}

	@RequestMapping(value = "/archive")
	@Transactional
	public String listAll(Model model) {
		model.addAttribute("tickets", bhpTicketsService.findArchivedTickets());
		return "bhptickets/list";
	}

	@RequestMapping(value = "/create")
	public String createTicket(Model model, Locale locale) {
		model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
		model.addAttribute("ticketCreateForm", new TicketCreateForm());
		return "bhptickets/create";
	}

	@RequestMapping(value = "/create/transfer")
	public String createTransfer(Model model, Locale locale) {
		model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
		model.addAttribute("transferCreateForm", new TransferCreateForm());
		return "bhptickets/transfer";
	}

	@RequestMapping(value = "/create/transfer", method = RequestMethod.POST)
	@Transactional
	public String createTransferPost(@Valid TransferCreateForm transferCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/create";
		}

		User from = userService.findById(transferCreateForm.getFromUserId());
		User to = userService.findById(transferCreateForm.getToUserId());

		for (BhpTicket ticket : from.getAssignedBhpTickets()) {
			if (ticket.getState().getOrder() < 90) {
				ticket.setAssignedUser(to);
				to.getAssignedBhpTickets().add(ticket);
			}
		}

		System.out.println(transferCreateForm.getFromUserId() + " --> " + transferCreateForm.getToUserId());

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/create/transfer";
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

		List<BhpFileInfo> fileList = listBhpFiles(uploadController.getBhpPhotoPath(), id);
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
		ticket.setUtrComment("");
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

	@RequestMapping(value = "/edit", params = { "reopen" }, method = RequestMethod.POST)
	@Transactional
	public String reopen(@Valid TicketCreateForm ticketEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		BhpTicket ticket = bhpTicketsService.findById(ticketEditForm.getId());
		BhpTicketState reopenState = bhpTicketStateService.findByOrder(25);
		ticket.setState(reopenState);
		ticket.setToSend(true);
		reopenState.getTickets().add(ticket);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/list";
	}

	@RequestMapping(value = "/edit", params = { "archive" }, method = RequestMethod.POST)
	@Transactional
	public String archive(@Valid TicketCreateForm ticketEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		BhpTicket ticket = bhpTicketsService.findById(ticketEditForm.getId());
		BhpTicketState archiveState = bhpTicketStateService.findByOrder(95);
		ticket.setState(archiveState);
		archiveState.getTickets().add(ticket);
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

		// get ticket
		BhpTicket ticket = bhpTicketsService.findById(id);
		if (ticket == null) {
			throw new NotFoundException("Ticket not found");
		}

		// create response form
		TicketResponseForm responseForm = new TicketResponseForm();
		responseForm.setId(ticket.getId());
		responseForm.setComment(ticket.getComment());
		responseForm.setUtrComment(ticket.getUtrComment());
		responseForm.setUtrCommentNeeded(ticket.isUtrCommentNeeded());

		// get user
		User authUser = userService.getAuthenticatedUser();
		// set viewed state and view date + decide of modification for user
		// response part
		if (authUser == null || (authUser.getId() != ticket.getAssignedUser().getId())
				|| (ticket.getState().getOrder() >= 40)) {
			responseForm.setTicketsUserModificationAllowed(false);
		} else {
			responseForm.setTicketsUserModificationAllowed(true);
			if (ticket.getState().getOrder() < 20) {
				BhpTicketState stateViewed = bhpTicketStateService.findByOrder(20);
				ticket.setState(stateViewed);
				ticket.setUpdateDate(new Timestamp(new java.util.Date().getTime()));
				stateViewed.getTickets().add(ticket);
			}
		}
		// set modification for utr user response part
		if (authUser == null || (!authUser.hasRole("ROLE_BHPTICKETSUTRUSER")) || 
			(
						(ticket.getState().getOrder() != 32) && (ticket.getState().getOrder() != 35) 
			)) {
			responseForm.setUtrUserModificationAllowed(false);
		} else {
			responseForm.setUtrUserModificationAllowed(true);

		}

		
		// get photos list
		List<BhpFileInfo> fileList = listBhpFiles(uploadController.getBhpPhotoPath(), id);

		model.addAttribute("ticketResponseForm", responseForm);
		model.addAttribute("photos", fileList);
		model.addAttribute("ticket", ticket);
		return "bhptickets/show";
	}

	private List<BhpFileInfo> listBhpFiles(String path, int ticketId) {
		ArrayList<BhpFileInfo> list = new ArrayList<>();
		
		try {
			File[] filesList = (new DefaultResourceLoader()).getResource(path).getFile().listFiles();
			FileTime creationTime;
			String creationTimeString;
			for(File file: filesList){
				if(file.getName().startsWith("bhp_" + ticketId + "_")){
					creationTime = (FileTime)Files.getAttribute(Paths.get(file.getPath()), "basic:creationTime");
					creationTimeString = dateHelper.formatDdMmYyyyHhMmDot(new Timestamp(creationTime.toMillis()));
					list.add(new BhpFileInfo(file.getName(), creationTimeString));
				}
				
			}
		} catch (IOException e) {
		} catch (NullPointerException np){
			list.clear();
		}
		
		return list;
	}

	@RequestMapping("/print/{id}")
	@Transactional
	public String printBhpTicket(@PathVariable("id") int id, Model model) throws NotFoundException {

		// get ticket
		BhpTicket ticket = bhpTicketsService.findById(id);
		if (ticket == null) {
			throw new NotFoundException("Ticket not found");
		}

		// get photos list
		ArrayList<String> fileList = uploadController.listFiles(uploadController.getBhpPhotoPath());
		for (int i = fileList.size() - 1; i >= 0; i--) {
			if (!fileList.get(i).startsWith("bhp_" + id + "_")) {
				fileList.remove(i);
			}
		}

		model.addAttribute("photos", fileList);
		model.addAttribute("ticket", ticket);
		return "bhptickets/print";
	}

	@RequestMapping(value = "/response", params = { "passticket" }, method = RequestMethod.POST)
	@Transactional
	public String passTicket(@Valid TicketResponseForm ticketResponseForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		if (ticketResponseForm.getComment().trim().length() == 0) {
			bindingResult.rejectValue("comment", "NotEmpty.ticketResponseForm.comment", "ERROR");
		}
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

	@RequestMapping(value = "/response", params = { "passtickettour" }, method = RequestMethod.POST)
	@Transactional
	public String passTicketToUr(@Valid TicketResponseForm ticketResponseForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		if (ticketResponseForm.getComment().trim().length() == 0) {
			bindingResult.rejectValue("comment", "NotEmpty.ticketResponseForm.comment", "ERROR");
		}
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
		BhpTicketState passedToUrState = bhpTicketStateService.findByOrder(32);
		passedToUrState.getTickets().add(ticket);
		ticket.setState(passedToUrState);
		ticket.setUpdateDate(new Timestamp(new java.util.Date().getTime()));
		ticket.setComment(ticketResponseForm.getComment().trim());
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("bhp.tickets.confirm.passtour", null, locale));
		return "redirect:/bhptickets/list";
	}

	@RequestMapping(value = "/response", params = { "closeticket" }, method = RequestMethod.POST)
	@Transactional
	public String closeTicket(@Valid TicketResponseForm ticketResponseForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		if (ticketResponseForm.getComment().trim().length() == 0) {
			bindingResult.rejectValue("comment", "NotEmpty.ticketResponseForm.comment", "ERROR");
		}
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

	@RequestMapping(value = "/response", params = { "commentutr" }, method = RequestMethod.POST)
	@Transactional
	public String commentUtr(@Valid TicketResponseForm ticketResponseForm, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		if (ticketResponseForm.getUtrComment().trim().length() == 0) {
			bindingResult.rejectValue("utrComment", "NotEmpty.ticketResponseForm.utrComment", "ERROR");
		}
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
		BhpTicketState utrComment = bhpTicketStateService.findByOrder(35);
		utrComment.getTickets().add(ticket);
		ticket.setState(utrComment);
		ticket.setUpdateDate(new Timestamp(new java.util.Date().getTime()));
		ticket.setUtrComment(ticketResponseForm.getUtrComment().trim() + textHelper.newLine() + "["
				+ userService.getAuthenticatedUser().getName() + "]");
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/list";
	}

}

package sbs.controller.bhptickets;

import java.sql.Timestamp;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
import sbs.model.bhptickets.BhpTicket;
import sbs.model.bhptickets.BhpTicketState;
import sbs.model.users.User;
import sbs.service.bhptickets.BhpTicketStateService;
import sbs.service.bhptickets.BhpTicketsService;
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
	TextHelper textHelper;
	
	 @RequestMapping(value = "/list")
	 @Transactional
	 public String listAll(Model model){
		 model.addAttribute("tickets", bhpTicketsService.findAll());
		 return "bhptickets/list";
	}
	 
	
	@RequestMapping(value = "/dispatch")
	public String dispatch(){
		return "bhptickets/dispatch";
	}
	
	@RequestMapping(value = "/create")
	public String createTicket(Model model, Locale locale) {
		model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
		model.addAttribute("ticketCreateForm", new TicketCreateForm());
		return "bhptickets/create";
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
		//assigned user
		User assignedUser = userService.findById(ticketCreateForm.getAssignedUser());
		if (assignedUser == null) {
			bindingResult.rejectValue("id", "error.user.not.found", "ERROR");
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/create";
		}
		
		assignedUser.getAssignedBhpTickets().add(ticket);
		ticket.setAssignedUser(assignedUser);
		
		// save
		bhpTicketsService.save(ticket);
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/dispatch";
	}
	
}

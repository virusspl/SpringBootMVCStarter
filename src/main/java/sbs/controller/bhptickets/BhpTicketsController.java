package sbs.controller.bhptickets;

import java.util.List;
import java.util.Locale;

import javax.sound.midi.Soundbank;
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

import sbs.controller.qualitysurveys.OperatorForm;
import sbs.controller.qualitysurveys.ProductionOrderForm;
import sbs.controller.users.UserEditForm;
import sbs.helpers.TextHelper;
import sbs.model.hr.HrUserInfo;
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
	public String create(@Valid TicketCreateForm ticketCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/create";
		}
		// get user
		User user = userService.findById(ticketCreateForm.getAssignedUser());
		if (user == null) {
			bindingResult.rejectValue("id", "error.user.not.found", "ERROR");
			model.addAttribute("bhpUsers", userService.findByRole("ROLE_BHPTICKETSUSER"));
			return "bhptickets/create";
		}
		
		/*
		 * modelUser.setUsername(userEditForm.getUsername());
		 * modelUser.setName(userEditForm.getName());
		 * modelUser.setEmail(userEditForm.getEmail());
		 * modelUser.setActive(userEditForm.getActive());
		 * userService.update(modelUser);
		 */
		
		System.out.println(ticketCreateForm);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/bhptickets/dispatch";
	}
	
}

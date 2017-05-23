package sbs.controller.bhptickets;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.helpers.TextHelper;
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
	
	

}

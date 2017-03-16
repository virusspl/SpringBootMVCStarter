package sbs.controller.messaging;

import java.security.Principal;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import sbs.service.users.UserService;


@Controller
public class MessagingController {
	@Autowired
	private SimpMessageSendingOperations messaging;
	@Autowired 
	private UserService userService;

	public MessagingController(){
	}


	@MessageMapping("/connect")
	public void login(Principal principal) throws Exception {
		
	}

	@MessageMapping("/postmessage")
    public void message(InfoMessage message, Principal principal) throws Exception {
			messaging.convertAndSend("/topic/messages", 
					new InfoMessage(message.getSender(),Jsoup.parse(message.getContent()).text()));
		}	
}
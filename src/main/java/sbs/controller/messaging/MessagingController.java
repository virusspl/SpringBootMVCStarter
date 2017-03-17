package sbs.controller.messaging;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import sbs.singleton.LiveFeedSingleton;


@Controller
public class MessagingController {
	@Autowired
	private SimpMessageSendingOperations messaging;
	@Autowired
	private LiveFeedSingleton liveFeedSingleton;
	
	public MessagingController(){
	}


	@MessageMapping("/connect")
	public void login() throws Exception {
	}

	@MessageMapping("/postmessage")
    public void message(InfoMessage message) throws Exception {
		liveFeedSingleton.setMessage(message.getContent());
		messaging.convertAndSend("/topic/messages", 
					new InfoMessage(message.getSender(),Jsoup.parse(message.getContent()).text()));
		}	
	
	@MessageMapping("/removemessage")
    public void removeMessage() throws Exception {
		liveFeedSingleton.setMessage(null);
		messaging.convertAndSend("/topic/cancelMessage", "del");
		}
	
	
}
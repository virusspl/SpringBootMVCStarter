package sbs.controller.messaging;

import java.util.Calendar;

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
		
		//System.out.println(message.getContent());
		if(message.getContent().startsWith("timer:")){
			String[] val = message.getContent().split(":");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(val[1]));
			cal.set(Calendar.MONTH, Integer.parseInt(val[2])-1);
			cal.set(Calendar.YEAR, Integer.parseInt(val[3]));
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(val[4]));
			cal.set(Calendar.MINUTE, Integer.parseInt(val[5]));
			cal.set(Calendar.SECOND, 0);
			
			liveFeedSingleton.setTimerMillis(cal.getTimeInMillis());
			return;
		}
		
		
		
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
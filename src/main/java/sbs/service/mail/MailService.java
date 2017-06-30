package sbs.service.mail;

import javax.mail.MessagingException;

public interface MailService {

	public void sendEmail(String from, String[] to, String[] cc, String topic, String content) throws MessagingException;
	
}

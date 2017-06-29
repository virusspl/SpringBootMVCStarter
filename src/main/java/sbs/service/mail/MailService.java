package sbs.service.mail;

import javax.mail.MessagingException;

public interface MailService {

	public void sendEmail(String from, String[] to, String[] bcc, String topic, String content) throws MessagingException;
	
}

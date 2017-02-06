package sbs.service;

import javax.mail.MessagingException;

public interface MailService {

	public boolean sendEmail(String from, String to, String topic, String content) throws MessagingException;
	
}

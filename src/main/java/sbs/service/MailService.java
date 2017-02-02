package sbs.service;

public interface MailService {

	public boolean sendEmail(String from, String to, String topic, String content);
	
}

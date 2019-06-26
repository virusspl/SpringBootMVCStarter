package sbs.service.mail;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void sendEmail(String from, String[] to, String[] cc, String subject, String content) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(to);
		helper.setFrom(from);
		helper.setCc(cc);
		message.setSubject(subject, "UTF-8");
		message.setContent(content, "text/html; charset=utf-8");
		javaMailSender.send(message);
	}

	@Override
	public void sendEmail(String from, String replyTo, String[] to, String[] cc, String[] bcc, Date date, String subject,
			String content) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(from);
		helper.setReplyTo(replyTo);
		helper.setTo(to);
		helper.setCc(cc);
		helper.setBcc(bcc);
		helper.setSentDate(date);
		message.setSubject(subject, "UTF-8");
		message.setContent(content, "text/html; charset=utf-8");
		javaMailSender.send(message);
		
	}

}

package sbs.controller.mailer;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

public class MailerCreateForm {

	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date date;
	private String hour;
	@Email
	@Size(min=5, max=50)
	private String from;
	@Email
	@Size(min=5, max=50)
	private String replyTo;
	@Size(min=5, max=1000)
	private String to;
	private String cc;
	private String bcc;
	@Size(min=2, max=200)
	private String title;
	private String contents;
	

	
	public MailerCreateForm() {
		this.from = "webapp@atwsystem.pl";
		this.replyTo = "no-reply@atwsystem.pl";
		this.date = new java.util.Date();
		this.to="michalak.k@atwsystem.pl";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	@Override
	public String toString() {
		return "MailerCreateForm [date=" + date + ", hour=" + hour + ", from=" + from + ", replyTo=" + replyTo + ", to="
				+ to + ", cc=" + cc + ", bcc=" + bcc + ", title=" + title + ", contents=" + contents + "]";
	}

	
}

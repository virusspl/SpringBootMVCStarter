package sbs.scheduling;

import java.net.UnknownHostException;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import sbs.controller.payterm.PaymentTermsBean;
import sbs.model.x3.X3SalesInvoice;
import sbs.service.mail.MailService;

public class PaymentTermsScheduler {

	
	@Autowired
	PaymentTermsBean termBean;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;


	Logger logger;
	
	public PaymentTermsScheduler() {
		// CRON format: second, minute, hour, day, month, weekday
		logger = LoggerFactory.getLogger(PaymentTermsScheduler.class);
	}

	@Scheduled(cron = "0 0 6 * * *")
	public String schedulePaymentNotifications() {
		
		try {
				this.sendMail(termBean.getInvoicesList(), termBean.getMailingList(), termBean.getDaysParameter());
		} catch (MessagingException e) {

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return "various/test";
	}

	private void sendMail(List<X3SalesInvoice> list, List<String> mailTo, int daysParameter) throws UnknownHostException, MessagingException {
		
		Context context = new Context();
		context.setVariable("title", "Nowe ogłoszenia!");
		context.setVariable("message", "Pojawiło się nowe ogłoszenie!");
		context.setVariable("list", list);
		context.setVariable("days", daysParameter);
		
		String body = templateEngine.process("payterm/mailtemplate", context);
		if (mailTo.size() > 0) {
			mailService.sendEmail("webapp@atwsystem.pl", mailTo.toArray(new String[0]), new String[0], "Zbliżające się terminy płatności",
					body);
		}
	}

	// cron
	/*
	 * @
	 * 
	 * https://docs.spring.io/spring/docs/current/javadoc-api/org/
	 * springframework/scheduling/support/CronSequenceGenerator.html The pattern is
	 * a list of six single space-separated fields representing: second, minute,
	 * hour, day, month, weekday. Month and weekday names can be given as the first
	 * three letters of the English names.
	 * 
	 * Example patterns: "0 0 * * * *" = the top of every hour of every day.
	 * "*(no space)/10 * * * * *" = every ten seconds. "0 0 8-10 * * *" = 8, 9 and
	 * 10 o'clock of every day. "0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.
	 * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
	 * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays "0 0 0 25 12 ?" =
	 * every Christmas Day at midnight
	 * 
	 * represents all values, so if used in the second field it means every second
	 * or used in the day field meaning run every day. ? represents no specific
	 * value and can be used in either the day of month or day of week field where
	 * using one invalidates the other. If we specify to trigger on the 15th day of
	 * a month then a ? will be used in the Day of week field. - represents a
	 * inclusive range of values, for example 1-3 in the hours field means the hours
	 * 1, 2 and 3. , represents additional values, for example MON,WED,SUN in the
	 * day of week field means on Monday, Wednesday and Sunday. / represents
	 * increments, for example 0/15 in the seconds field triggers every 15 seconds
	 * starting from 0 (0, 15, 30 and 45). L represents the last day of the week or
	 * month. Remember that Saturday is the end of the week in this context, so
	 * using L in the day of week field will trigger on a Saturday. This can be used
	 * in conjunction with a number in the day of month field, such as 6L to
	 * represent the last Friday of the month or an expression like L-3 denoting the
	 * third from last day of the month. If we specify a value in the day of week
	 * field we must use ? in the day of month field, and vise versa. W represents
	 * the nearest weekday of the month. For example if 15W will trigger on 15th day
	 * of the month if it is a weekday, otherwise it will run on the closest
	 * weekday. This value cannot be used in a list of day values. # specifies both
	 * the day of the week and the week that the task should trigger. For example,
	 * 5#2 means the second Thursday of the month. If the day and week you specified
	 * overflows into the next month then it will not trigger.
	 */
}

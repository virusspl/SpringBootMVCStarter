package sbs.scheduling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import sbs.helpers.DateHelper;
import sbs.service.mail.MailService;
import sbs.service.system.SystemInfoParametersService;
import sbs.service.x3.JdbcOracleX3Service;

public class RmgtScheduler {

	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	SystemInfoParametersService parametersService;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	DateHelper dateHelper;

	private ArrayList<String> urls;
	private ArrayList<String> oldUrls;
	private String source;

	Logger logger;
	
	public RmgtScheduler() {
		// CRON format: second, minute, hour, day, month, weekday
		logger = LoggerFactory.getLogger(DocViewCacheScheduler.class);
		this.urls = new ArrayList<String>();
		this.oldUrls = new ArrayList<String>();
		this.source = "https://www.otomoto.pl/osobowe/renault/megane/seg-city-car--seg-compact--seg-sedan/od-2016/?search%5Bfilter_float_engine_power%3Afrom%5D=200&search%5Bfilter_float_engine_power%3Ato%5D=250&search%5Bfilter_enum_damaged%5D=0&search%5Border%5D=created_at_first%3Adesc&search%5Bbrand_program_id%5D%5B0%5D=&search%5Bcountry%5D=";
	}

	@Scheduled(cron = "0 0 * * * *")
	public String scheduleRMGT() {
		URL address;
		try {
			address = new URL(this.source);
			BufferedReader in = new BufferedReader(new InputStreamReader(address.openStream()));

			// save current list in backup
			makeUrlsBackupArray(urls, oldUrls);
			// clear current list
			urls.clear();
			
			String inputLine;
			boolean newLines = false;
			while ((inputLine = in.readLine()) != null)
				// if header
				if (inputLine.contains("class=\"offer-title__link\" data-ninja-extradata=")) {
					// read next line (link)
					if((inputLine = in.readLine()) != null) {
						urls.add(inputLine.split("\"")[1]);
						// if already in old list
						if(!arrayContains(inputLine.split("\"")[1], oldUrls)) {
							newLines = true;
						}
					};
				}
			in.close();

			if (newLines) {
				this.sendMail();
			}

		} catch (MalformedURLException e) {

		} catch (IOException e) {

		} catch (MessagingException e) {

		}

		return "various/test";
	}

	private void makeUrlsBackupArray(ArrayList<String> from, ArrayList<String> to) {
		to.clear();
		for(String url: from) {
			to.add(url);
		}
	}

	private boolean arrayContains(String string, ArrayList<String> urls2) {
		for(String url: urls2) {
			if(string.equalsIgnoreCase(url)) {
				return true;
			}
		}
		return false;
	}

	private void sendMail() throws UnknownHostException, MessagingException {
		ArrayList<String> mailTo = new ArrayList<>();
		mailTo.add("viruss.snk@gmail.com");
		//mailTo.add("michalak.k@atwsystem.pl");

		Context context = new Context();
		context.setVariable("title", "Nowe ogłoszenia!");
		context.setVariable("message", "Pojawiło się nowe ogłoszenie!");
		context.setVariable("urls", this.urls);
		context.setVariable("source", this.source);
		context.setVariable("date", dateHelper.formatDdMmYyyyHhMmDot(new java.util.Date()));
		
		String body = templateEngine.process("various/rmgtTemplate", context);
		if (mailTo.size() > 0) {
			mailService.sendEmail("notify@oto.pl", mailTo.toArray(new String[0]), new String[0], "Nowe ogłoszenie!",
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

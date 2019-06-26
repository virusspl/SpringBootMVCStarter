package sbs.controller.mailer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import sbs.helpers.TextHelper;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("mailer")
public class MailerController {

	@Autowired
	TextHelper textHelper;
	@Autowired
	UserService userService;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	MessageSource messageSource;

	@ModelAttribute
	public void addAttributes(Model model) {

	}

	public MailerController() {

	}

	@RequestMapping("/create")
	public String order(Model model) {
		model.addAttribute(new MailerCreateForm());

		return "mailer/create";
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String createTransferPost(@Valid MailerCreateForm mailerCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws UnknownHostException, MessagingException {

		String from, replyTo, title, contents;
		String[] to, cc, bcc;

		from = mailerCreateForm.getFrom().trim();
		replyTo = mailerCreateForm.getReplyTo().trim();
		title = mailerCreateForm.getTitle().trim();
		contents = mailerCreateForm.getContents().trim().replace(System.getProperty("line.separator"), "<br/>");

		to = splitList(mailerCreateForm.getTo());
		cc = splitList(mailerCreateForm.getCc());
		bcc = splitList(mailerCreateForm.getBcc());

		if (to == null) {
			bindingResult.rejectValue("to", null, "mail format");
		}
		if (cc == null) {
			bindingResult.rejectValue("cc", null, "mail format");
		}
		if (bcc == null) {
			bindingResult.rejectValue("bcc", null, "mail format");
		}

		if (to != null && to.length == 0) {
			bindingResult.rejectValue("to", null, "must not be empty");
		}

		Calendar curr = Calendar.getInstance();
		Calendar sent = Calendar.getInstance();
		sent.setTime(mailerCreateForm.getDate());

		if (mailerCreateForm.getHour().trim().length() > 0) {
			int[] hour = getHourFromString(mailerCreateForm.getHour().trim());
			if (hour != null) {
				sent.set(Calendar.HOUR_OF_DAY, hour[0]);
				sent.set(Calendar.MINUTE, hour[1]);
				sent.set(Calendar.SECOND, hour[2]);
			} else {
				bindingResult.rejectValue("hour", null, "hh:mm:ss");
			}
		} else {
			sent.set(Calendar.HOUR_OF_DAY, curr.get(Calendar.HOUR_OF_DAY));
			sent.set(Calendar.MINUTE, curr.get(Calendar.MINUTE));
			sent.set(Calendar.SECOND, curr.get(Calendar.SECOND));
		}

		// validate
		if (bindingResult.hasErrors()) {
			return "mailer/create";
		}

		Context context = new Context();
		context.setVariable("contents", contents);
		String body = templateEngine.process("mailer/plainTemplate", context);

		mailService.sendEmail(from, replyTo, to, cc, bcc, sent.getTime(), title, body);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("email.sent", null, locale));
		
		return "redirect:/mailer/create";
	}

	private int[] getHourFromString(String hourString) {
		try {
			int[] result = new int[3];
			String[] spl = hourString.split(":");
			if (spl.length != 3) {
				return null;
			}
			else{
				result[0] = Integer.parseInt(spl[0]);
				result[1] = Integer.parseInt(spl[1]);
				result[2] = Integer.parseInt(spl[2]);
			}

			return result;
		} catch (NumberFormatException nfe) {
			return null;
		}

	}

	/**
	 * @param list separated by ;
	 * @return null if contains not valid e-mail
	 */
	private String[] splitList(String list) {

		ArrayList<String> tmp = new ArrayList<>();
		String entry;

		String[] result = list.split(";");
		for (int i = 0; i < result.length; i++) {
			entry = result[i].trim();
			if (entry.length() == 0) {
				continue;
			} else if (textHelper.isMailValid(entry)) {
				tmp.add(entry);
			} else {
				return null;
			}
		}

		return tmp.toArray(new String[tmp.size()]);
	}

}

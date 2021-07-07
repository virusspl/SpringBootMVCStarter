package sbs.controller.cebs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javassist.NotFoundException;
import sbs.helpers.DateHelper;
import sbs.model.cebs.CebsEvent;
import sbs.model.cebs.CebsLine;
import sbs.model.users.User;
import sbs.service.cebs.CebsEventsService;
import sbs.service.cebs.CebsLinesService;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("cebs")
@Transactional
public class CebsController {

	// kebabmania - maria_koszyk@yahoo.it

	@Autowired
	DateHelper dateHelper;
	@Autowired
	UserService userService;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	CebsEventsService eventsService;
	@Autowired
	CebsLinesService linesService;

	private boolean active;
	private boolean confirmed;
	private boolean sent;
	private Map<Long, CebsItem> items;
	private String organizer;
	private Calendar actionDate;
	private String dayCode;
	private String location;
	private String locationCode;
	private CebsEvent event;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("organizer", organizer);
		model.addAttribute("active", active);
		model.addAttribute("confirmed", confirmed);
		model.addAttribute("sent", sent);
		if (actionDate != null) {
			model.addAttribute("actionDate", dateHelper.formatDdMmYyyy(actionDate.getTime()));
		}
		model.addAttribute("dayCode", dayCode);
		model.addAttribute("location", this.location);
	}

	public CebsController() {
		active = false;
		confirmed = false;
		sent = false;
		items = new TreeMap<>();
	}

	@RequestMapping("/order")
	public String order(Model model) {

		prepareOrderView(model);
		model.addAttribute(new CebsOrderForm());

		return "cebs/order";
	}

	private void prepareOrderView(Model model) {
		// db_start
		// refresh event info
		this.event = eventsService.findActiveEvent();
		if (event != null) {
			this.active = true;
			this.confirmed = event.isConfirmed();
			this.sent = event.isSent();
			this.organizer = event.getOrganizer();
			Calendar cal = Calendar.getInstance();
			cal.setTime(event.getActionDate());
			this.actionDate = cal;
			this.dayCode = event.getDayCode();
			this.location = event.getLocation();
			this.locationCode = event.getLocationCode();
			addAttributes(model);
		} else {
			this.active = false;
		}
		// db_end

		if (this.active) {
			User currentUser = userService.getAuthenticatedUser();
			List<CebsItem> myItems = new ArrayList<>();
			Double amount = 0.0;
			// db_start
			// refresh items list
			List<CebsLine> lines = linesService.findByEventId(this.event.getEventId());
			this.items.clear();
			CebsItem tmpItem;
			for (CebsLine line : lines) {
				tmpItem = new CebsItem(userService.findById(line.getUserId()));
				tmpItem.setComment(line.getComment().trim());
				tmpItem.setItem(line.getItem());
				tmpItem.setQuantity(line.getQuantity());
				tmpItem.setAmount(line.getAmount());
				tmpItem.setDbId(line.getLineId());
				tmpItem.setId(line.getLongId());
				tmpItem.setPaid(line.isPaid());
				this.items.put(line.getLongId(), tmpItem);
			}
			// db_end

			for (CebsItem item : this.items.values()) {
				if (item.getUser().getId().equals(currentUser.getId())) {
					myItems.add(item);
					amount += item.getAmount();
				}
			}

			model.addAttribute("menu", getMenuList());
			model.addAttribute("items", myItems);
			model.addAttribute("amount", amount);
		}
		model.addAttribute("active", active);
		model.addAttribute("confirmed", confirmed);
		model.addAttribute("sent", sent);
	}

	private List<MenuItem> getMenuList() {
		List<MenuItem> list = new ArrayList<>();
		try {
			String file = "";
			switch (this.locationCode) {
			case "kml":
				file = "data/cebs_KM.dat";
				break;
			case "bks":
				file = "data/cebs_BK.dat";
				break;
			}

			File resource = new ClassPathResource(file).getFile();

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource), "UTF8"));

			String st;
			MenuItem mi;
			String[] line;
			while ((st = br.readLine()) != null) {
				mi = new MenuItem();
				line = st.split(";");
				mi.setId(line[0]);
				mi.setText(line[1]);
				mi.setPrice(Double.parseDouble(line[2]));
				list.add(mi);
			}
			br.close();

		} catch (Exception ex) {
		}
		return list;
	}

	@RequestMapping("/order/delete/{itemId}")
	public String deleteQuestion(@PathVariable("itemId") long itemId, RedirectAttributes redirectAttrs) {
		if (!sent) {
			// db_start
			CebsLine line = linesService.findByLongId(itemId);
			if (line != null) {
				linesService.remove(line);
			}
			// db_end
			this.items.remove(itemId);
		}
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/paid/{itemId}")
	public String itempaidSwitch(@PathVariable("itemId") long itemId, RedirectAttributes redirectAttrs) {
		// db_start
		CebsLine line = linesService.findByLongId(itemId);
		if (line != null) {
			line.setPaid(!line.isPaid());
			linesService.update(line);
		}
		// db_end
		items.get(itemId).setPaid(!items.get(itemId).isPaid());
		return "redirect:/cebs/manage";
	}

	@RequestMapping("/manage/confirmed")
	public String confirmedSwitch(RedirectAttributes redirectAttrs) throws UnknownHostException, MessagingException {
		// db_start
		if (this.event != null) {
			event.setConfirmed(!event.isConfirmed());
			eventsService.update(event);
		}
		// db_end
		this.confirmed = !this.confirmed;
		if (confirmed) {
			String title = "Zamówienie potwierdzone";
			String message = "Zamówienie zostało zaakceptowane przez bar i na pewno zostanie złożone. Prosimy o zapłatę.";
			this.sendMail(title, message, true, null);
		} else {
			String title = "Potwierdzenie ODWOŁANE";
			String message = "Potwierdzenie zostało ODWOŁANE. Nie wiemy, czy zostanie zrealizowane - wstrzymujemy zbiórkę pieniędzy.";
			this.sendMail(title, message, true, null);
		}
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/sent")
	public String sentSwitch(RedirectAttributes redirectAttrs) throws UnknownHostException, MessagingException {
		// db_start
		if (this.event != null) {
			event.setSent(!event.isSent());
			eventsService.update(event);
		}
		// db_end
		this.sent = !this.sent;
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/arrived3min")
	public String arrived3min(RedirectAttributes redirectAttrs) throws UnknownHostException, MessagingException {
		return this.arrived(180000, redirectAttrs);
	}

	@RequestMapping("/manage/arrivedInstant")
	public String arrivedInstant(RedirectAttributes redirectAttrs) throws UnknownHostException, MessagingException {
		return this.arrived(1000, redirectAttrs);
	}

	public String arrived(long delay, RedirectAttributes redirectAttrs)
			throws UnknownHostException, MessagingException {

		String title = "Zamówienie gotowe do odbioru!";
		String message = "Przyjechała dostawa. Zapraszamy po odbiór ;-)";

		List<User> toSend = this.getOrderedMailingList();
		System.out.println(toSend);
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					sendMail(title, message, true, toSend);
				} catch (UnknownHostException | MessagingException e) {

				}
			}
		}, delay);

		if(delay>10000) {
			redirectAttrs.addFlashAttribute("msg", "Zaplanowano wysłanie powiadomienia (3 min)");
		}
		else {
			redirectAttrs.addFlashAttribute("msg", "Powiadomienie zostało wysłane");
		}
		
		return "redirect:/cebs/order";
	}

	@RequestMapping(value = "/order/add", params = { "add" }, method = RequestMethod.POST)
	public String addToOrder(@RequestParam String add, CebsOrderForm cebsOrderFom, RedirectAttributes redirectAttrs,
			Locale locale, Model model) throws NotFoundException {
		if (!sent || userService.getAuthenticatedUser().hasRole("ROLE_CEBSMANAGER")
				|| userService.getAuthenticatedUser().hasRole("ROLE_ADMIN")) {
			List<MenuItem> menu = getMenuList();
			for (MenuItem item : menu) {
				if (item.getId().equals(add)) {
					CebsItem ci = new CebsItem(userService.getAuthenticatedUser());
					ci.setComment(cebsOrderFom.getComment().trim());
					ci.setItem(item.getText());
					ci.setQuantity(cebsOrderFom.getQuantity());
					ci.setAmount(item.getPrice() * cebsOrderFom.getQuantity());

					// db_start
					CebsLine line = new CebsLine();
					line.setAmount(ci.getAmount());
					line.setCebsEvent(this.event);
					line.setComment(ci.getComment());
					line.setItem(ci.getItem());
					line.setLongId(ci.getId());
					line.setPaid(ci.isPaid());
					line.setQuantity(ci.getQuantity());
					line.setUserId(ci.getUser().getId());

					linesService.save(line);
					ci.setDbId(line.getLineId());
					// db_end
					this.items.put(ci.getId(), ci);
				}
			}
		}
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/starttoday/{loc}")
	public String startToday(@PathVariable("loc") String loc, Model model) throws Exception {
		this.locationCode = loc;
		switch (loc) {
		case "kml":
			this.location = "KebabMania LESKO";
			break;
		case "bks":
			this.location = "Bar Kebab SANOK";
			break;
		default:
			throw new Exception("Unrecognized location " + loc);
		}
		this.active = true;
		this.confirmed = false;
		this.sent = false;
		this.actionDate = Calendar.getInstance();
		this.dayCode = "general.calendar.day" + actionDate.get(Calendar.DAY_OF_WEEK);
		this.organizer = userService.getAuthenticatedUser().getName();
		String title = "Zbieramy na DZISIAJ w " + this.location + "!";
		String message = "Jeżeli wszystko się uda, zamówienie będzie relizowane DZISIAJ, "
				+ dateHelper.formatDdMmYyyy(actionDate.getTime()) + " w " + this.location
				+ ". <br/>Proszę wejść na stronę z linku poniżej i dopisać się do listy ;)";
		this.sendMail(title, message, false, null);

		// db_start
		CebsEvent ev = new CebsEvent();
		ev.setActionDate(new Timestamp(this.actionDate.getTime().getTime()));
		ev.setActive(this.active);
		ev.setConfirmed(this.confirmed);
		ev.setDayCode(this.dayCode);
		ev.setLocation(this.location);
		ev.setLocationCode(this.locationCode);
		ev.setOrganizer(this.organizer);
		ev.setSent(this.sent);
		eventsService.save(ev);
		// db_end

		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/starttomorrow/{loc}")
	public String startTomorrow(@PathVariable("loc") String loc, Model model) throws Exception {
		this.locationCode = loc;
		switch (loc) {
		case "kml":
			this.location = "KebabMania LESKO";
			break;
		case "bks":
			this.location = "Bar Kebab SANOK";
			break;
		default:
			throw new Exception("Unrecognized location " + loc);
		}
		this.active = true;
		this.confirmed = false;
		this.sent = false;
		this.actionDate = Calendar.getInstance();
		this.actionDate.add(Calendar.DAY_OF_MONTH, 1);
		this.dayCode = "general.calendar.day" + actionDate.get(Calendar.DAY_OF_WEEK);
		this.organizer = userService.getAuthenticatedUser().getName();
		String title = "Zbieramy na JUTRO w " + this.location + "!";
		String message = "Jeżeli wszystko się uda, zamówienie będzie relizowane JUTRO, "
				+ dateHelper.formatDdMmYyyy(actionDate.getTime()) + " w " + this.location
				+ ". <br/>Proszę wejść na stronę z linku poniżej i dopisać się do listy ;)";
		this.sendMail(title, message, false, null);

		// db_start
		CebsEvent ev = new CebsEvent();
		ev.setActionDate(new Timestamp(this.actionDate.getTime().getTime()));
		ev.setActive(this.active);
		ev.setConfirmed(this.confirmed);
		ev.setDayCode(this.dayCode);
		ev.setLocation(this.location);
		ev.setLocationCode(this.locationCode);
		ev.setOrganizer(this.organizer);
		ev.setSent(this.sent);
		eventsService.save(ev);
		// db_end

		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/cancel")
	public String cancel(Model model) throws UnknownHostException, MessagingException {
		// db_start
		if (this.event != null) {
			event.setActive(false);
			eventsService.update(event);
		}
		// db_end
		this.active = false;
		this.confirmed = false;
		this.sent = false;
		String title = "Anulujemy akcję zamawiania";
		String message = "Anulujemy akcję zamawiania, bar nieczynny lub inny powód - może innym razem";
		this.sendMail(title, message, true, null);
		this.items.clear();
		// TODO switch to inactive in cebs event
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/finish")
	public String finish(Model model) throws UnknownHostException, MessagingException {
		// db_start
		if (this.event != null) {
			event.setActive(false);
			eventsService.update(event);
		}
		// db_end
		this.active = false;
		this.confirmed = false;
		this.sent = false;
		this.items.clear();
		// TODO switch to inactive in cebs event
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage")
	public String manage(Model model) {
		Double amount = 0.0;
		Double paid = 0.0;
		List<CebsItem> currentItems = new ArrayList<>();
		Map<String, Integer> summary = new TreeMap<>();

		for (CebsItem item : items.values()) {
			currentItems.add(item);
			amount += item.getAmount();
			if (item.isPaid()) {
				paid += item.getAmount();
			}
			summary.put(item.getItem(), item.getQuantity() + summary.getOrDefault(item.getItem(), 0));
		}

		model.addAttribute("items", currentItems);
		model.addAttribute("amount", amount);
		model.addAttribute("paid", paid);
		model.addAttribute("missing", amount - paid);
		model.addAttribute("summary", summary);

		return "cebs/manage";
	}

	@RequestMapping("/manage/createsummarymail")
	public String summaryMail(Model model) throws MessagingException, UnknownHostException {
		Double amount = 0.0;
		List<CebsItem> currentItems = new ArrayList<>();
		Map<String, Integer> summary = new TreeMap<>();

		for (CebsItem item : items.values()) {
			currentItems.add(item);
			amount += item.getAmount();
			summary.put(item.getItem(), item.getQuantity() + summary.getOrDefault(item.getItem(), 0));
		}

		ArrayList<String> mailTo = new ArrayList<>();
		mailTo.add(userService.getAuthenticatedUser().getEmail());

		Context context = new Context();
		// context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("host", InetAddress.getLocalHost().getHostName());
		context.setVariable("items", currentItems);
		context.setVariable("amount", amount);
		context.setVariable("summary", summary);
		context.setVariable("date", dateHelper.formatDdMmYyyy(actionDate.getTime()));
		String body = templateEngine.process("cebs/mailsummary", context);
		if (mailTo.size() > 0) {
			mailService.sendEmail("webapp@atwsystem.pl", mailTo.toArray(new String[0]), new String[0],
					"Zamówienie ADR Polska - " + dateHelper.formatDdMmYyyy(actionDate.getTime()), body);
		}

		return "redirect:/cebs/order";
	}

	private void sendMail(String title, String message, boolean orderedOnly, List<User> recipients)
			throws UnknownHostException, MessagingException {

		List<User> cebsUsers = new ArrayList<>();

		if (recipients == null) {
			if (orderedOnly) {
				cebsUsers = getOrderedMailingList();
			} else {
				cebsUsers = userService
						.findByAnyRole(new String[] { "ROLE_CEBSMANAGER", "ROLE_CEBSUSER", "ROLE_ADMIN" });
			}
		} else {
			cebsUsers = recipients;
		}

		ArrayList<String> mailTo = new ArrayList<>();
		for (User user : cebsUsers) {
			mailTo.add(user.getEmail());
		}

		Context context = new Context();
		// context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("host", InetAddress.getLocalHost().getHostName());
		context.setVariable("title", title);
		context.setVariable("message", message);
		String body = templateEngine.process("cebs/mailtemplate", context);
		if (mailTo.size() > 0) {
			mailService.sendEmail("webapp@atwsystem.pl", mailTo.toArray(new String[0]), new String[0], title, body);
		}
	}

	private List<User> getOrderedMailingList() {
		List<User> list = new ArrayList<>();
		for (CebsItem item : items.values()) {
			list.add(item.getUser());
		}
		return list;
	}

}

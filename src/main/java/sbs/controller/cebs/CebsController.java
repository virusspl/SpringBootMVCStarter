package sbs.controller.cebs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.mail.MessagingException;

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
import sbs.model.users.User;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("cebs")
public class CebsController {

	@Autowired
	DateHelper dateHelper;
	@Autowired
	UserService userService;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;

	private boolean active;
	private Map<Long, CebsItem> items;
	private String organizer;
	private Calendar actionDate;
	private String dayCode;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("organizer", organizer);
		model.addAttribute("active", active);
		model.addAttribute("actionDate", dateHelper.formatDdMmYyyy(actionDate.getTime()));
		model.addAttribute("dayCode", dayCode);
	}

	public CebsController() {
		active = false;
		items = new TreeMap<>();
		actionDate = Calendar.getInstance();
	}

	@RequestMapping("/order")
	public String order(Model model) {

		prepareOrderView(model);
		model.addAttribute(new CebsOrderForm());

		return "cebs/order";
	}

	private void prepareOrderView(Model model) {
		if (active) {
			User currentUser = userService.getAuthenticatedUser();
			List<CebsItem> myItems = new ArrayList<>();
			Double amount = 0.0;
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
	}

	private List<MenuItem> getMenuList() {
		List<MenuItem> list = new ArrayList<>();
		try {
			File resource = new ClassPathResource("data/cebs.dat").getFile();

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
		this.items.remove(itemId);
		return "redirect:/cebs/order";
	}
	
	@RequestMapping(value = "/order/add", params = { "add" }, method = RequestMethod.POST)
	public String showMakeBomSurvey(@RequestParam String add, CebsOrderForm cebsOrderFom, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		List<MenuItem> menu = getMenuList();
		for(MenuItem item: menu){
			if(item.getId().equals(add)){
				CebsItem ci = new CebsItem(userService.getAuthenticatedUser());
				ci.setComment(cebsOrderFom.getComment().trim());
				ci.setItem(item.getText());
				ci.setQuantity(cebsOrderFom.getQuantity());
				ci.setAmount(item.getPrice() * cebsOrderFom.getQuantity());
				this.items.put(ci.getId(), ci);
			}
		}
		return "redirect:/cebs/order";
	}
	

	@RequestMapping("/manage/starttoday")
	public String startToday(Model model) throws UnknownHostException, MessagingException {
		this.active = true;
		this.actionDate = Calendar.getInstance();
		this.dayCode = "general.calendar.day" + actionDate.get(Calendar.DAY_OF_WEEK);
		this.organizer = userService.getAuthenticatedUser().getName();
		String title = "Zbieramy zamówienia na DZISIAJ!";
		String message = "Jeżeli wszystko się uda, zamówienie będzie relizowane DZISIAJ, "+ dateHelper.formatDdMmYyyy(actionDate.getTime()) +". <br/>Proszę wejść na stronę z linku poniżej i dopisać się do listy ;)";
		this.sendMail(title, message);
		return "redirect:/cebs/order";
	}
	
	@RequestMapping("/manage/starttomorrow")
	public String startTomorrow(Model model) throws UnknownHostException, MessagingException {
		this.active = true;
		this.actionDate = Calendar.getInstance();
		this.actionDate.add(Calendar.DAY_OF_MONTH, 1);
		this.dayCode = "general.calendar.day" + actionDate.get(Calendar.DAY_OF_WEEK); 
		this.organizer = userService.getAuthenticatedUser().getName();
		String title = "Zbieramy zamówienia na JUTRO!";
		String message = "Jeżeli wszystko się uda, zamówienie będzie relizowane JUTRO, "+ dateHelper.formatDdMmYyyy(actionDate.getTime()) +". <br/>Proszę wejść na stronę z linku poniżej i dopisać się do listy ;)";
		this.sendMail(title, message);
		return "redirect:/cebs/order";
	}
	

	@RequestMapping("/manage/cancel")
	public String cancel(Model model) throws UnknownHostException, MessagingException {
		this.active = false;
		this.items.clear();
		String title = "Przerywamy akcję zamawiania";
		String message = "Anulujemy akcję zamawiania - może innym razem";
		this.sendMail(title, message);
		return "redirect:/cebs/order";
	}
	
	@RequestMapping("/manage/finish")
	public String finish(Model model) throws UnknownHostException, MessagingException {
		this.active = false;
		this.items.clear();
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage")
	public String manage(Model model) {
		Double amount = 0.0;
		List<CebsItem> currentItems = new ArrayList<>();
		Map<String, Integer> summary = new TreeMap<>();
		
		for (CebsItem item : items.values()) {
			currentItems.add(item);
			amount += item.getAmount();
			summary.put(item.getItem(), item.getQuantity() + summary.getOrDefault(item.getItem(), 0));
		}

		model.addAttribute("items", currentItems);
		model.addAttribute("amount", amount);
		model.addAttribute("summary", summary);

		return "cebs/manage";
	}
	
	private void sendMail(String title, String message)
			throws UnknownHostException, MessagingException {

		List<User> cebsUsers = userService.findByAnyRole(new String[]{"ROLE_CEBSMANAGER", "ROLE_CEBSUSER", "ROLE_ADMIN"});
		ArrayList<String> mailTo = new ArrayList<>();
		for (User user : cebsUsers) {
			mailTo.add(user.getEmail());
		}

		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("title", title);
		context.setVariable("message", message);
		String body = templateEngine.process("cebs/mailtemplate", context);
		mailService.sendEmail("webapp@atwsystem.pl", mailTo.toArray(new String[0]), new String[0],
				title, body);
	}

}

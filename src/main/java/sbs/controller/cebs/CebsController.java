package sbs.controller.cebs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

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

import javassist.NotFoundException;
import sbs.helpers.DateHelper;
import sbs.model.users.User;
import sbs.service.users.UserService;

@Controller
@RequestMapping("cebs")
public class CebsController {

	@Autowired
	DateHelper dateHelper;
	@Autowired
	UserService userService;

	private boolean active;
	private Map<Long, CebsItem> items;
	private String organizer;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("organizer", organizer);
		model.addAttribute("active", active);
	}

	public CebsController() {
		active = false;
		items = new TreeMap<>();
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
	

	@RequestMapping("/manage/start")
	public String start(Model model) {
		this.active = true;
		this.organizer = userService.getAuthenticatedUser().getName();
		return "redirect:/cebs/order";
	}

	@RequestMapping("/manage/cancel")
	public String cancel(Model model) {
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

}

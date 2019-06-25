package sbs.controller.home;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.helpers.DateHelper;

@Controller
public class HomeController {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DateHelper dateHelper;

	@RequestMapping("/")
	public String home(Model model) {
		Calendar cal = Calendar.getInstance();
		model.addAttribute("today", dateHelper.formatDdMmYyyyDot(cal.getTime()));
		model.addAttribute("nameDay", getNameDay(cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1)));
		model.addAttribute("dayCode", "general.calendar.day" + cal.get(Calendar.DAY_OF_WEEK));

		return "welcome";
	}

	@RequestMapping("/terminal")
	public String terminal() {
		return "welcome_terminal";
	}
	
	// fake login link - path secured
	@RequestMapping("/terminal/auth")
	public String terminalAuthentication() {
		return "redirect:/terminal";
	}

	private String getNameDay(String key) {
		try {
			File resource = new ClassPathResource("data/namedays.dat").getFile();

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resource), "UTF8"));

			String st;
			while ((st = br.readLine()) != null) {
				if (st.split(";")[0].equals(key)) {
					br.close();
					return st.split(";")[1];
				}
			}
			br.close();

		} catch (Exception ex) {
		}
		return "";
	}
}
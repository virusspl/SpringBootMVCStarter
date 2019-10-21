package sbs.controller.redmine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.helpers.TextHelper;
import sbs.service.redmine.RedmineService;

@Controller
@RequestMapping("redmine")
public class RedmineController {

	@Autowired
	TextHelper textHelper;
	@Autowired
	RedmineService redmineService;


	public RedmineController() {

	}

	@RequestMapping("/userslist")
	public String showUsersList(Model model) {
		model.addAttribute("list", redmineService.getPlUsers().values());
		return "redmine/userslist";
	}

}

package sbs.controller.redmine;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.helpers.TextHelper;
import sbs.model.redmine.RedmineUser;
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
		Map<Integer, RedmineUser> users = redmineService.getPlUsers();
		for(RedmineUser user: users.values()) {
			user.createCopyString();
		}
		model.addAttribute("list", users.values() );
		return "redmine/userslist";
	}

}

package sbs.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.model.Role;
import sbs.model.User;
import sbs.service.RoleService;
import sbs.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;

	@RequestMapping("/login")
	public String authenticate() {

		// get basic users from db
		User admin = userService.findByUsername("Admin");
		User user = userService.findByUsername("User");
		Role adminRole = roleService.findByName("ROLE_ADMIN");
		Role userRole = roleService.findByName("ROLE_USER");
		
		// init if null
		if (adminRole == null) {
			adminRole = new Role();
			adminRole.setName("ROLE_ADMIN");
		}
		if (userRole == null) {
			userRole = new Role();
			userRole.setName("ROLE_USER");
		}

		if (admin == null) {
			// object
			admin = new User();
			admin.setUsername("Admin");
			admin.setPassword("$2a$04$UZWCi1I779DTvZzdYI/YG.oRidHjWNsxQcW9I7QqapOrYE8tXelu6");
			admin.setName("Admin Name");
			admin.setEmail("admin@sjava.com");
			// many to many
			admin.getRoles().add(adminRole);
			adminRole.getUsers().add(admin);
			admin.getRoles().add(userRole);
			userRole.getUsers().add(admin);
			// save
			userService.save(admin);
		}
		
		if (user == null){
			// object
			user = new User();
			user.setUsername("User");
			user.setPassword("$2a$04$VYg7rxx7ZKLszJbLxAW48eMu/cYy8Asg4BFmOkEawwK6WfuywISdS");
			user.setName("Normal User");
			user.setEmail("normaluser@sjava.com");
			// many to many
			user.getRoles().add(userRole);
			userRole.getUsers().add(user);
			// save
			userService.save(user);
		}

		return "various/login";
	}
}

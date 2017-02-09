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
		User krzysiek = userService.findByUsername("Krzysiek");
		User maciek = userService.findByUsername("Maciek");
		User seweryn = userService.findByUsername("Seweryn");
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
			admin.setActive(true);
			// many to many
			admin.getRoles().add(adminRole);
			adminRole.getUsers().add(admin);
			admin.getRoles().add(userRole);
			userRole.getUsers().add(admin);
			// save
			userService.save(admin);
		}
		if (maciek == null) {
			// object
			maciek = new User();
			maciek.setUsername("Maciek");
			maciek.setPassword("$2a$04$VYg7rxx7ZKLszJbLxAW48eMu/cYy8Asg4BFmOkEawwK6WfuywISdS");
			maciek.setName("Maciej Rycyk");
			maciek.setEmail("rycyk.m@atwsystem.pl");
			maciek.setActive(true);
			// many to many
			maciek.getRoles().add(adminRole);
			adminRole.getUsers().add(maciek);
			maciek.getRoles().add(userRole);
			userRole.getUsers().add(maciek);
			// save
			userService.save(maciek);
		}
		if (seweryn == null) {
			// object
			seweryn = new User();
			seweryn.setUsername("Seweryn");
			seweryn.setPassword("$2a$04$VYg7rxx7ZKLszJbLxAW48eMu/cYy8Asg4BFmOkEawwK6WfuywISdS");
			seweryn.setName("Seweryn Prenkiewicz");
			seweryn.setEmail("prenkiewicz.s@atwsystem.pl");
			seweryn.setActive(false);
			// many to many
			seweryn.getRoles().add(adminRole);
			adminRole.getUsers().add(seweryn);
			seweryn.getRoles().add(userRole);
			userRole.getUsers().add(seweryn);
			// save
			userService.save(seweryn);
		}
		if (krzysiek == null) {
			// object
			krzysiek = new User();
			krzysiek.setUsername("Krzysiek");
			krzysiek.setPassword("$2a$04$VYg7rxx7ZKLszJbLxAW48eMu/cYy8Asg4BFmOkEawwK6WfuywISdS");
			krzysiek.setName("Krzysztof Michalak");
			krzysiek.setEmail("michalak.k@atwsystem.pl");
			krzysiek.setActive(true);
			// many to many
			krzysiek.getRoles().add(adminRole);
			adminRole.getUsers().add(krzysiek);
			krzysiek.getRoles().add(userRole);
			userRole.getUsers().add(krzysiek);
			// save
			userService.save(krzysiek);
		}
		
		if (user == null){
			// object
			user = new User();
			user.setUsername("User");
			user.setPassword("$2a$04$VYg7rxx7ZKLszJbLxAW48eMu/cYy8Asg4BFmOkEawwK6WfuywISdS");
			user.setName("Normal User");
			user.setEmail("normaluser@sjava.com");
			user.setActive(true);
			// many to many
			user.getRoles().add(userRole);
			userRole.getUsers().add(user);
			// save
			userService.save(user);
		}

		return "various/login";
	}
}

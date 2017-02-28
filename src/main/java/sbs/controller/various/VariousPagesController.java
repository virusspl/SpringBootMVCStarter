package sbs.controller.various;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.model.Role;
import sbs.model.User;
import sbs.model.hr.HrUserInfo;
import sbs.model.hr.RcpInfo;
import sbs.service.JdbcAdrOptimaService;
import sbs.service.JdbcOracleGeodeService;
import sbs.service.JdbcOracleX3Service;
import sbs.service.RoleService;
import sbs.service.UserService;

@Controller
public class VariousPagesController {

	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service jdbcOracleX3Service;
	@Autowired
	JdbcAdrOptimaService jdbcAdrOptimaService;
	@Autowired
	JdbcOracleGeodeService jdbcOracleGeodeService;

	@RequestMapping("/noaccess")
	public String noAccess() {
		return "various/noaccess";
	}

	@RequestMapping("/underconstruction")
	public String underConstruction() {
		return "various/construction";
	}

	@RequestMapping("/jdbc")
	public String jdbc(Model model) {
		model.addAttribute("resultOptima",jdbcAdrOptimaService.findAllCurrentlyEmployed());
		// model.addAttribute("resultOptima",
		// jdbcAdrOptimaService.findAllUsers());
		// model.addAttribute("resultX3",
		// jdbcOracleX3Service.findAllUsers("ATW"));
		// model.addAttribute("geodeList",
		// jdbcOracleGeodeService.findLocationsOfProduct("836004"));
		RcpInfo rcp = jdbcAdrOptimaService.findRcpInfoByUserId("1116");
		if (rcp != null) {
			System.out.println(" ===== RCP INFO =====");
			System.out.println(
					rcp.getId() + " " + rcp.getLastName() + " " + rcp.getDepartment() + " " + rcp.getRcpNumber());
		} else {
			System.out.println("no user found by id");
		}
		RcpInfo rcp2 = jdbcAdrOptimaService.findRcpInfoByCardNo("01029EF8F9");
		if (rcp2 != null) {
			System.out.println(" ===== RCP INFO =====");
			System.out.println(rcp2.getId() + " " + rcp2.getDepartment() + " " + rcp2.getRcpNumber());
		} else {
			System.out.println("no user found by card");
		}

		HrUserInfo hr = jdbcAdrOptimaService.findCurrentlyEmployedById("1116");
		if (hr != null) {
			System.out.println(" ==== HR INFO =====");
			System.out.println(hr.getId());
			System.out.println(hr.getFirstName());
			System.out.println(hr.getLastName());
			System.out.println(hr.getCurentJobStart());
			System.out.println(hr.getCurrentJobEnd());
			System.out.println(hr.getDepartment());
			System.out.println(hr.getPosition());
			System.out.println(hr.getRcpNumber());
		} else {
			System.out.println("no current user HR info found by id");
		}

		HrUserInfo hr2 = jdbcAdrOptimaService.findCurrentlyEmployedByCardNo("600817731");
		if (hr2 != null) {
			System.out.println(" ==== HR INFO =====");
			System.out.println(hr2.getId());
			System.out.println(hr2.getFirstName());
			System.out.println(hr2.getLastName());
			System.out.println(hr2.getCurentJobStart());
			System.out.println(hr2.getCurrentJobEnd());
			System.out.println(hr2.getDepartment());
			System.out.println(hr2.getPosition());
			System.out.println(hr2.getRcpNumber());
		} else {
			System.out.println("no current user HR info found by card no");
		}
		
		return "various/jdbc";
	}

	@RequestMapping("/init")
	public String initDatabaseData(Model model, Locale locale) {
		String msg = messageSource.getMessage("action.objects.created", null, locale) + ": ";

		Role adminRole = roleService.findByName("ROLE_ADMIN");
		if (adminRole == null) {
			adminRole = new Role();
			adminRole.setName("ROLE_ADMIN");
			msg += "[role: " + adminRole.getName() + "], ";
		}
		Role userRole = roleService.findByName("ROLE_USER");
		if (userRole == null) {
			userRole = new Role();
			userRole.setName("ROLE_USER");
			msg += "[role: " + userRole.getName() + "], ";
		}
		Role qualityManagerRole = roleService.findByName("ROLE_QUALITYMANAGER");
		if (qualityManagerRole == null) {
			qualityManagerRole = new Role();
			qualityManagerRole.setName("ROLE_QUALITYMANAGER");
			roleService.save(qualityManagerRole);
			msg += "[role: " + qualityManagerRole.getName() + "], ";
		}
		Role qualityUserRole = roleService.findByName("ROLE_QUALITYUSER");
		if (qualityUserRole == null) {
			qualityUserRole = new Role();
			qualityUserRole.setName("ROLE_QUALITYUSER");
			roleService.save(qualityUserRole);
			msg += "[role: " + qualityUserRole.getName() + "], ";
		}

		User admin = userService.findByUsername("Admin");
		User user = userService.findByUsername("User");
		User krzysiek = userService.findByUsername("Krzysiek");
		User maciek = userService.findByUsername("Maciek");
		User seweryn = userService.findByUsername("Seweryn");
		if (admin == null) {
			// object
			admin = new User();
			admin.setUsername("Admin");
			admin.setPassword("$2a$04$7gatGBXoTWA.rLmFONVz/Oepajcpp7FTOwaAiFjEQOx/BPMm/gJL6");
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
			msg += "[user: " + admin.getUsername() + "], ";
		}
		if (maciek == null) {
			// object
			maciek = new User();
			maciek.setUsername("Maciek");
			maciek.setName("Maciej Rycyk");
			maciek.setEmail("rycyk.m@atwsystem.pl");
			maciek.setActive(false);
			// many to many
			maciek.getRoles().add(adminRole);
			adminRole.getUsers().add(maciek);
			maciek.getRoles().add(userRole);
			userRole.getUsers().add(maciek);
			// save
			userService.save(maciek);
			msg += "[user: " + maciek.getUsername() + "], ";
		}
		if (seweryn == null) {
			// object
			seweryn = new User();
			seweryn.setUsername("Seweryn");
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
			msg += "[user: " + seweryn.getUsername() + "], ";
		}
		if (krzysiek == null) {
			// object
			krzysiek = new User();
			krzysiek.setUsername("Krzysiek");
			krzysiek.setPassword("$2a$04$UZWCi1I779DTvZzdYI/YG.oRidHjWNsxQcW9I7QqapOrYE8tXelu6");
			krzysiek.setName("Krzysztof Michalak");
			krzysiek.setEmail("michalak.k@atwsystem.pl");
			krzysiek.setActive(false);
			// many to many
			krzysiek.getRoles().add(adminRole);
			adminRole.getUsers().add(krzysiek);
			krzysiek.getRoles().add(userRole);
			userRole.getUsers().add(krzysiek);
			// save
			userService.save(krzysiek);
			msg += "[user: " + krzysiek.getUsername() + "], ";
		}

		if (user == null) {
			// object
			user = new User();
			user.setUsername("User");
			user.setPassword("$2a$04$VYg7rxx7ZKLszJbLxAW48eMu/cYy8Asg4BFmOkEawwK6WfuywISdS");
			user.setName("Normal User");
			user.setEmail("normaluser@sjava.com");
			user.setActive(false);
			// many to many
			user.getRoles().add(userRole);
			userRole.getUsers().add(user);
			// save
			userService.save(user);
			msg += "[user: " + user.getUsername() + "], ";
		}

		model.addAttribute("msg", msg);
		return "welcome";
	}

}

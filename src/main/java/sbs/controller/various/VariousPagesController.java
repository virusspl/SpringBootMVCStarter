package sbs.controller.various;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.model.qualitysurveys.QualitySurvey;
import sbs.model.qualitysurveys.QualitySurveyParameterAnswer;
import sbs.model.users.Role;
import sbs.model.users.User;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.qualitysurveys.QualitySurveysService;
import sbs.service.users.RoleService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

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
	@Autowired
	QualitySurveysService qualitySurveysService;

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
		
		QualitySurvey qs = new QualitySurvey();
		qs.setClientCode("CAD40");
		qs.setClientName("ADR Uboldo");
		qs.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		qs.setOperatorDepartment("Informatyka");
		qs.setOperatorFirstName("Krzysztof");
		qs.setOperatorId("1116");
		qs.setOperatorLastName("Michalak");
		qs.setOperatorPosition("Ciężko powiedzieć / programista");
		qs.setOperatorRcpNo("BLABLABLA123");
		qs.setParameterAnswers(new HashSet<QualitySurveyParameterAnswer>());
		qs.setProductCode("98E01");
		qs.setProductDescription("BLA.. blabla...bla bla bla bla (Gigi D'Agostino");
		qs.setProductionOrder("X111111111111");
		qs.setSalesOrder("ADASALES00001");
		qs.setUser(userService.findByUsername("admin"));
		qs.setType("bom");
		
		qualitySurveysService.save(qs);
		System.out.println(qs.getId());

		
		/*
		model.addAttribute("resultOptima",jdbcAdrOptimaService.findAllCurrentlyEmployed());
		 model.addAttribute("resultX3", jdbcOracleX3Service.findAllUsers("ATW"));
		// model.addAttribute("geodeList",jdbcOracleGeodeService.findLocationsOfProduct("836004"));
		
		
		HrUserInfo hr = jdbcAdrOptimaService.findCurrentlyEmployedById("1116");
		if (hr != null) {
			System.out.println(" ==== HR INFO =====");
			System.out.println(hr.getId());
			System.out.println(hr.getFirstName());
			System.out.println(hr.getLastName());
			System.out.println(hr.getCurrentJobStart());
			System.out.println(hr.getCurrentJobEnd());
			System.out.println(hr.getEmployDate());
			System.out.println(hr.getQuitDate());
			System.out.println(hr.getDepartment());
			System.out.println(hr.getPosition());
			System.out.println(hr.getRcpNumber());
		} else {
			System.out.println("no current user HR info found by id");
		}

		HrUserInfo hr2 = jdbcAdrOptimaService.findCurrentlyEmployedByCardNo("1600DFF297");
		if (hr2 != null) {
			System.out.println(" ==== HR INFO =====");
			System.out.println(hr2.getId());
			System.out.println(hr2.getFirstName());
			System.out.println(hr2.getLastName());
			System.out.println(hr2.getCurrentJobStart());
			System.out.println(hr2.getCurrentJobEnd());
			System.out.println(hr2.getEmployDate());
			System.out.println(hr2.getQuitDate());
			System.out.println(hr2.getDepartment());
			System.out.println(hr2.getPosition());
			System.out.println(hr2.getRcpNumber());
		} else {
			System.out.println("no current user HR info found by card no");
		}
		*/
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

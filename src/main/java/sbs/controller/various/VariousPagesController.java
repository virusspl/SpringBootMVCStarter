package sbs.controller.various;

/*
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
*/
import java.util.Locale;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.model.bhptickets.BhpTicketState;
import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.model.users.Role;
import sbs.model.users.User;
import sbs.service.bhptickets.BhpTicketStateService;
import sbs.service.bhptickets.BhpTicketsService;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.mail.MailService;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.qualitysurveys.QualitySurveyParametersService;
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
	@Autowired
	QualitySurveyParametersService parametersService;
	@Autowired
	BhpTicketsService bhpTicketsService;
	@Autowired
	BhpTicketStateService bhpTicketStateService;
	@Autowired
	MailService mailService;

	@RequestMapping("/noaccess")
	public String noAccess() {
		return "various/noaccess";
	}
	
	@RequestMapping("/expired")
	public String expired(RedirectAttributes redirectAttributes, Locale locale) {
		redirectAttributes.addFlashAttribute("error", messageSource.getMessage("session.expired", null, locale));
		return "redirect:/login";
	}

	@RequestMapping("/underconstruction")
	public String underConstruction() {
		return "various/construction";
	}

	@RequestMapping("/mail")
	public String mail(Model model) {
		try {
			mailService.sendEmail("spring@spring.pl", new String[]{"michalak.k@atwsystem.pl"},new String[]{} ,"Spring mail", "Little test. Without polish znaków :)");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "welcome";
	}
	
	@RequestMapping("/test")
	public String test(Model model) {


		//URL url = new URL ("ftp://username:password@www.superland.example/server");
		//URLConnection urlc = url.openConnection();
		//InputStream is = urlc.getInputStream();
		

		//List<BhpTicketState> listb = bhpTicketStateService.findAll();
		//System.out.println(listb);
		
		//X3Client result = jdbcOracleX3Service.findClientByCode("ATW", "cad40");
		//X3SalesOrder result = jdbcOracleX3Service.findSalesOrderByNumber("ATW", "yza140099");
		//X3Product result = jdbcOracleX3Service.findProductByCode("ATW", "98e01");
		//X3ProductionOrderDetails result = jdbcOracleX3Service.getProductionOrderInfoByNumber("ATW", "X31121400099455");
		//System.out.println(result);
		
		///List<X3BomItem> list = jdbcOracleX3Service.findBomPartsByParent("ATW", "431S1001");
		//model.addAttribute("list", listb);
		
		/*
		// CREATE DEMO SURVEY
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
		 */
		
		/*
		 // VARIOUS TESTS
		model.addAttribute("resultOptima",jdbcAdrOptimaService.findAllCurrentlyEmployed());
		model.addAttribute("resultX3", jdbcOracleX3Service.findAllUsers("ATW"));
		model.addAttribute("geodeList",jdbcOracleGeodeService.findLocationsOfProduct("836004"));
		*/
		
		/*
		// TEST HR
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
		//return "various/jdbc";
		return "various/test";
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
		Role qualityManagerRole = roleService.findByName("ROLE_QSURVEYMANAGER");
		if (qualityManagerRole == null) {
			qualityManagerRole = new Role();
			qualityManagerRole.setName("ROLE_QSURVEYMANAGER");
			roleService.save(qualityManagerRole);
			msg += "[role: " + qualityManagerRole.getName() + "], ";
		}
		Role qualityUserRole = roleService.findByName("ROLE_QSURVEYUSER");
		if (qualityUserRole == null) {
			qualityUserRole = new Role();
			qualityUserRole.setName("ROLE_QSURVEYUSER");
			roleService.save(qualityUserRole);
			msg += "[role: " + qualityUserRole.getName() + "], ";
		}
		
		Role bhpManager = roleService.findByName("ROLE_BHPMANAGER");
		if (bhpManager == null) {
			bhpManager = new Role();
			bhpManager.setName("ROLE_BHPMANAGER");
			roleService.save(bhpManager);
			msg += "[role: " + bhpManager.getName() + "], ";
		}
		Role bhpTicketsUser = roleService.findByName("ROLE_BHPTICKETSUSER");
		if (bhpTicketsUser == null) {
			bhpTicketsUser = new Role();
			bhpTicketsUser.setName("ROLE_BHPTICKETSUSER");
			roleService.save(bhpTicketsUser);
			msg += "[role: " + bhpTicketsUser.getName() + "], ";
		}
		Role bhpSupervisor = roleService.findByName("ROLE_BHPSUPERVISOR");
		if (bhpSupervisor == null) {
			bhpSupervisor = new Role();
			bhpSupervisor.setName("ROLE_BHPSUPERVISOR");
			roleService.save(bhpSupervisor);
			msg += "[role: " + bhpSupervisor.getName() + "], ";
		}
		
		Role hrUaManager = roleService.findByName("ROLE_HRUAMANAGER");
		if (hrUaManager == null) {
			hrUaManager = new Role();
			hrUaManager.setName("ROLE_HRUAMANAGER");
			roleService.save(hrUaManager);
			msg += "[role: " + hrUaManager.getName() + "], ";
		}
		
		Role buyOrdManager = roleService.findByName("ROLE_BUYORDMANAGER");
		if (buyOrdManager == null) {
			buyOrdManager = new Role();
			buyOrdManager.setName("ROLE_BUYORDMANAGER");
			roleService.save(buyOrdManager);
			msg += "[role: " + buyOrdManager.getName() + "], ";
		}
		
		Role ppSales = roleService.findByName("ROLE_PROPROG_SALES");
		if (ppSales == null) {
			ppSales = new Role();
			ppSales.setName("ROLE_PROPROG_SALES");
			roleService.save(ppSales);
			msg += "[role: " + ppSales.getName() + "], ";
		}
		
		Role ppDrawingValidation = roleService.findByName("ROLE_PROPROG_DRAWINGVALIDATION");
		if (ppDrawingValidation == null) {
			ppDrawingValidation = new Role();
			ppDrawingValidation.setName("ROLE_PROPROG_DRAWINGVALIDATION");
			roleService.save(ppDrawingValidation);
			msg += "[role: " + ppDrawingValidation.getName() + "], ";
		}
		
		Role ppSalesOrder = roleService.findByName("ROLE_PROPROG_SALESORDER");
		if (ppSalesOrder == null) {
			ppSalesOrder = new Role();
			ppSalesOrder.setName("ROLE_PROPROG_SALESORDER");
			roleService.save(ppSalesOrder);
			msg += "[role: " + ppSalesOrder.getName() + "], ";
		}
		
		Role ppProductionPlan = roleService.findByName("ROLE_PROPROG_PRODUCTIONPLAN");
		if (ppProductionPlan == null) {
			ppProductionPlan = new Role();
			ppProductionPlan.setName("ROLE_PROPROG_PRODUCTIONPLAN");
			roleService.save(ppProductionPlan);
			msg += "[role: " + ppProductionPlan.getName() + "], ";
		}
		
		Role ppPurchase = roleService.findByName("ROLE_PROPROG_PURCHASE");
		if (ppPurchase == null) {
			ppPurchase = new Role();
			ppPurchase.setName("ROLE_PROPROG_PURCHASE");
			roleService.save(ppPurchase);
			msg += "[role: " + ppPurchase.getName() + "], ";
		}
		
		Role ppTechnology = roleService.findByName("ROLE_PROPROG_TECHNOLOGY");
		if (ppTechnology == null) {
			ppTechnology = new Role();
			ppTechnology.setName("ROLE_PROPROG_TECHNOLOGY");
			roleService.save(ppTechnology);
			msg += "[role: " + ppTechnology.getName() + "], ";
		}
		
		Role ppToolDrawing = roleService.findByName("ROLE_PROPROG_TOOLDRAWING");
		if (ppToolDrawing == null) {
			ppToolDrawing = new Role();
			ppToolDrawing.setName("ROLE_PROPROG_TOOLDRAWING");
			roleService.save(ppToolDrawing);
			msg += "[role: " + ppToolDrawing.getName() + "], ";
		}
		
		Role ppToolCreation = roleService.findByName("ROLE_PROPROG_TOOLCREATION");
		if (ppToolCreation == null) {
			ppToolCreation = new Role();
			ppToolCreation.setName("ROLE_PROPROG_TOOLCREATION");
			roleService.save(ppToolCreation);
			msg += "[role: " + ppToolCreation.getName() + "], ";
		}
		
		Role ppQualityCheck = roleService.findByName("ROLE_PROPROG_QUALITYCHECK");
		if (ppQualityCheck == null) {
			ppQualityCheck = new Role();
			ppQualityCheck.setName("ROLE_PROPROG_QUALITYCHECK");
			roleService.save(ppQualityCheck);
			msg += "[role: " + ppQualityCheck.getName() + "], ";
		}
		
		Role movementsUser = roleService.findByName("ROLE_MOVEMENTSUSER");
		if (movementsUser == null) {
			movementsUser = new Role();
			movementsUser.setName("ROLE_MOVEMENTSUSER");
			roleService.save(movementsUser);
			msg += "[role: " + movementsUser.getName() + "], ";
		}
				

		System.out.println("get users:");
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
		
		if(parametersService.findAll().size()==0){
			QualitySurveyParameter param;
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać roztaw osi");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać roztaw płytek resorowych");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać roztaw wspornikówi");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać średnicę szczęk po przetaczaniu");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać moment skrętu osi (skrętnej)");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Moment dokręcenia nakrętki sworznia hamulca");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Poprawność działania ABS");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Dzwignia zgodna z rysunkiem/kat.Prod.");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Sprawdzić poprawność obrotu Piasty / Bębna");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Czy zamontowano i poprawnie zaplombowano  zawlęczkę");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Zgodność Konfiguracji osi z Rysunkiem technicznym");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Sprawdzić zbieżność");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
			
			param =  new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Smarowanie zgodnie z instrukcją/kartą");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
		}
		
		BhpTicketState bhpState;
		
		if(bhpTicketStateService.findByOrder(10) == null){
			bhpState = new BhpTicketState();
			bhpState.setOrder(10);
			bhpState.setDescription("Nowy");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}
		
		if(bhpTicketStateService.findByOrder(20) == null){
			bhpState = new BhpTicketState();
			bhpState.setOrder(20);
			bhpState.setDescription("Odczytany");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}
		
		if(bhpTicketStateService.findByOrder(25) == null){
			bhpState = new BhpTicketState();
			bhpState.setOrder(25);
			bhpState.setDescription("Ponownie otwarty");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}
			
		if(bhpTicketStateService.findByOrder(30) == null){	
			bhpState = new BhpTicketState();
			bhpState.setOrder(30);
			bhpState.setDescription("Przekazany");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}
			
		if(bhpTicketStateService.findByOrder(40) == null){	
			bhpState = new BhpTicketState();
			bhpState.setOrder(40);
			bhpState.setDescription("Zamknięty");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}
			
		if(bhpTicketStateService.findByOrder(90) == null){			
			bhpState = new BhpTicketState();
			bhpState.setOrder(90);
			bhpState.setDescription("Anulowany");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}	
		
		if(bhpTicketStateService.findByOrder(95) == null){			
			bhpState = new BhpTicketState();
			bhpState.setOrder(95);
			bhpState.setDescription("Archiwum");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		model.addAttribute("msg", msg);
		return "welcome";
	}

}

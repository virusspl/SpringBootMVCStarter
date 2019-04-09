package sbs.controller.various;

import java.util.ArrayList;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.ExcelContents;
import sbs.helpers.ExcelExportHelper;
import sbs.helpers.MathHelper;
import sbs.model.bhptickets.BhpTicketState;
import sbs.model.inventory.InventoryDataType;
import sbs.model.qcheck.QCheckState;
import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.model.qualitysurveys.QualitySurveyParameter;
import sbs.model.tools.ToolsProjectState;
import sbs.model.users.Role;
import sbs.model.users.User;
import sbs.service.bhptickets.BhpTicketStateService;
import sbs.service.bhptickets.BhpTicketsService;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.inventory.InventoryDataTypesService;
import sbs.service.mail.MailService;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.qcheck.QCheckStatesService;
import sbs.service.qsurveys.QSurveyQuestionTypesService;
import sbs.service.qualitysurveys.QualitySurveyParametersService;
import sbs.service.qualitysurveys.QualitySurveysService;
import sbs.service.tools.ToolsProjectStateService;
import sbs.service.users.RoleService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;
import sbs.singleton.LiveFeedSingleton;

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
	ToolsProjectStateService toolsProjectStateService;
	@Autowired
	MailService mailService;
	@Autowired
	QSurveyQuestionTypesService qSurveyQuestionTypesService;
	@Autowired
	QCheckStatesService qCheckStatesService;
	@Autowired
	private LiveFeedSingleton liveFeedSingleton;
	@Autowired
	InventoryDataTypesService inventoryDataTypesService;
	@Autowired 
	MathHelper mathHelper;

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
			mailService.sendEmail("spring@spring.pl", new String[] { "michalak.k@atwsystem.pl" }, new String[] {},
					"Spring mail", "Little test. Without polish znaków :)");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "welcome";
	}

	
	@RequestMapping(value = "/terminal/test")
	public String terminalTest(Model model) {
		model.addAttribute("msg", "Current message from controller");
		model.addAttribute("live", "Live feed from server");
		model.addAttribute("warning", "Warning message during input");
		model.addAttribute("error", "Error message of any kind");
		return "welcome_terminal";
	}
	
	@RequestMapping("/excel")
	public ModelAndView getExcel() {
		// create excel contents
		ExcelContents contents = new ExcelContents();
		// filename, sheetname
		contents.setFileName("excelExport");
		contents.setSheetName("export1");
		// headers
		ArrayList<String> headers = new ArrayList<>();
		headers.add("COL1");
		headers.add("COL2");
		headers.add("COL3");
		headers.add("COL4");
		headers.add("COL5");
		contents.setHeaders(headers);
		// rows
		ArrayList<ArrayList<Object>> rows = new ArrayList<>();
		ArrayList<Object> row;
		
		for(int i = 0; i<10; i++){
			row = new ArrayList<>();
			row.add("01");
			row.add("02");
			row.add("03");
			row.add("04");
			row.add("05");
			rows.add(row);
		}
		
		contents.setValues(rows);
		
		return new ModelAndView(new ExcelExportHelper(), "contents", contents);
	}

	@RequestMapping("/timer")
	public String timerView(Model model){
		model.addAttribute("timerDate", liveFeedSingleton.getTimerMillis());
		return "/various/timer"; 
	}
	
	
	@RequestMapping("/test")
	public String test(Model model, Locale locale) throws InterruptedException {
		//long start = System.currentTimeMillis();
//    	List<X3CoverageData> initialData = jdbcOracleX3Service.getCoverageInitialData("ATW");
//    	Map<String, String> descriptions = jdbcOracleX3Service.getDescriptionsByLanguage(jdbcOracleX3Service.convertLocaleToX3Lang(locale), "ATW");
//		Map<String, Integer> demand = jdbcOracleX3Service.getAcvDemandList("ATW");
//		List<X3UsageDetail> usage = jdbcOracleX3Service.getAcvUsageDetailsListByYear(2017, "ATW");
//		Map<String, X3Supplier> suppliers = jdbcOracleX3Service.getFirstAcvSuppliers("ATW");		
	
		
		//int min = 1;
		//int max = 100;
		/*(ArrayList<Integer> randomArray = new ArrayList<>();
		
		for(int i = 0; i<30; i++){
			Thread.sleep(2100);
			randomArray.add(mathHelper.randomInRange(1, 100));
		}
		model.addAttribute("numbers", randomArray);
		System.out.println("time: " + (System.currentTimeMillis()-start) + " ms");
		*/
		
		//List<X3Product> allProducts = jdbcOracleX3Service.findAllActiveProducts("ATW");
		System.out.println("===");
		//System.out.println(allProducts);
		
		
		return "various/test";

	}

	@RequestMapping("/testprint")
	public String testprint(Model model) {
		return "various/testprint";
	}

	@RequestMapping("/init")
	public String initDatabaseData(Model model, Locale locale) {
		String msg = messageSource.getMessage("action.objects.created", null, locale) + ": ";

		Role adminRole = roleService.findByName("ROLE_ADMIN");
		if (adminRole == null) {
			adminRole = new Role();
			adminRole.setName("ROLE_ADMIN");
			roleService.save(adminRole);
			msg += "[role: " + adminRole.getName() + "], ";
		}
		Role lightAdminRole = roleService.findByName("ROLE_LIGHT_ADMIN");
		if (lightAdminRole == null) {
			lightAdminRole = new Role();
			lightAdminRole.setName("ROLE_LIGHT_ADMIN");
			roleService.save(lightAdminRole);
			msg += "[role: " + lightAdminRole.getName() + "], ";
		}
		Role userRole = roleService.findByName("ROLE_USER");
		if (userRole == null) {
			userRole = new Role();
			userRole.setName("ROLE_USER");
			roleService.save(userRole);
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
		Role bhpTicketsUtrUser = roleService.findByName("ROLE_BHPTICKETSUTRUSER");
		if (bhpTicketsUtrUser == null) {
			bhpTicketsUtrUser = new Role();
			bhpTicketsUtrUser.setName("ROLE_BHPTICKETSUTRUSER");
			roleService.save(bhpTicketsUtrUser);
			msg += "[role: " + bhpTicketsUtrUser.getName() + "], ";
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

		Role pprog = roleService.findByName("ROLE_PROPROG");
		if (pprog == null) {
			pprog = new Role();
			pprog.setName("ROLE_PROPROG");
			roleService.save(pprog);
			msg += "[role: " + pprog.getName() + "], ";
		}

		Role pprogsuper = roleService.findByName("ROLE_PROPROGSUPERVISOR");
		if (pprogsuper == null) {
			pprogsuper = new Role();
			pprogsuper.setName("ROLE_PROPROGSUPERVISOR");
			roleService.save(pprogsuper);
			msg += "[role: " + pprogsuper.getName() + "], ";
		}

		Role movementsUser = roleService.findByName("ROLE_MOVEMENTSUSER");
		if (movementsUser == null) {
			movementsUser = new Role();
			movementsUser.setName("ROLE_MOVEMENTSUSER");
			roleService.save(movementsUser);
			msg += "[role: " + movementsUser.getName() + "], ";
		}

		Role movementsSuperUser = roleService.findByName("ROLE_MOVEMENTSSUPERUSER");
		if (movementsSuperUser == null) {
			movementsSuperUser = new Role();
			movementsSuperUser.setName("ROLE_MOVEMENTSSUPERUSER");
			roleService.save(movementsSuperUser);
			msg += "[role: " + movementsSuperUser.getName() + "], ";
		}
		Role movementsShipUser = roleService.findByName("ROLE_MOVEMENTSSHIPUSER");
		if (movementsShipUser == null) {
			movementsShipUser = new Role();
			movementsShipUser.setName("ROLE_MOVEMENTSSHIPUSER");
			roleService.save(movementsShipUser);
			msg += "[role: " + movementsShipUser.getName() + "], ";
		}
		Role movementsRcpUser = roleService.findByName("ROLE_MOVEMENTSRCPUSER");
		if (movementsRcpUser == null) {
			movementsRcpUser = new Role();
			movementsRcpUser.setName("ROLE_MOVEMENTSRCPUSER");
			roleService.save(movementsRcpUser);
			msg += "[role: " + movementsRcpUser.getName() + "], ";
		}

		Role utrNormalUser = roleService.findByName("ROLE_UTR_NORMALUSER");
		if (utrNormalUser == null) {
			utrNormalUser = new Role();
			utrNormalUser.setName("ROLE_UTR_NORMALUSER");
			roleService.save(utrNormalUser);
			msg += "[role: " + utrNormalUser.getName() + "], ";
		}

		Role rcpManager = roleService.findByName("ROLE_RCPMANAGER");
		if (rcpManager == null) {
			rcpManager = new Role();
			rcpManager.setName("ROLE_RCPMANAGER");
			roleService.save(rcpManager);
			msg += "[role: " + rcpManager.getName() + "], ";
		}
		
		Role consumptionUser = roleService.findByName("ROLE_CONSUMPTIONUSER");
		if (consumptionUser == null) {
			consumptionUser = new Role();
			consumptionUser.setName("ROLE_CONSUMPTIONUSER");
			roleService.save(consumptionUser);
			msg += "[role: " + consumptionUser.getName() + "], ";
		}
		
		Role prodtosaleUser = roleService.findByName("ROLE_PRODTOSALEUSER");
		if (prodtosaleUser == null) {
			prodtosaleUser = new Role();
			prodtosaleUser.setName("ROLE_PRODTOSALEUSER");
			roleService.save(prodtosaleUser);
			msg += "[role: " + prodtosaleUser.getName() + "], ";
		}
		
		Role toolsManager = roleService.findByName("ROLE_TOOLSMANAGER");
		if (toolsManager == null) {
			toolsManager = new Role();
			toolsManager.setName("ROLE_TOOLSMANAGER");
			roleService.save(toolsManager);
			msg += "[role: " + toolsManager.getName() + "], ";
		}
		
		Role toolsProdManager = roleService.findByName("ROLE_TOOLSPRODMANAGER");
		if (toolsProdManager == null) {
			toolsProdManager = new Role();
			toolsProdManager.setName("ROLE_TOOLSPRODMANAGER");
			roleService.save(toolsProdManager);
			msg += "[role: " + toolsProdManager.getName() + "], ";
		}
		
		Role toolsMailingUser = roleService.findByName("ROLE_TOOLSMAILINGUSER");
		if (toolsMailingUser == null) {
			toolsMailingUser = new Role();
			toolsMailingUser.setName("ROLE_TOOLSMAILINGUSER");
			roleService.save(toolsMailingUser);
			msg += "[role: " + toolsMailingUser.getName() + "], ";
		}
		
		Role toolsNormalUser = roleService.findByName("ROLE_TOOLSNORMALUSER");
		if (toolsNormalUser == null) {
			toolsNormalUser = new Role();
			toolsNormalUser.setName("ROLE_TOOLSNORMALUSER");
			roleService.save(toolsNormalUser);
			msg += "[role: " + toolsNormalUser.getName() + "], ";
		}
		
		Role timerUser = roleService.findByName("ROLE_TIMERUSER");
		if (timerUser == null) {
			timerUser = new Role();
			timerUser.setName("ROLE_TIMERUSER");
			roleService.save(timerUser);
			msg += "[role: " + timerUser.getName() + "], ";
		}
		
		Role phonesManager = roleService.findByName("ROLE_PHONESMANAGER");
		if (phonesManager == null) {
			phonesManager = new Role();
			phonesManager.setName("ROLE_PHONESMANAGER");
			roleService.save(phonesManager);
			msg += "[role: " + phonesManager.getName() + "], ";
		}
		
		Role terminalRole = roleService.findByName("ROLE_TERMINAL");
		if (terminalRole == null) {
			terminalRole = new Role();
			terminalRole.setName("ROLE_TERMINAL");
			roleService.save(terminalRole);
			msg += "[role: " + terminalRole.getName() + "], ";
		}
		
		Role componentsRole = roleService.findByName("ROLE_COMPONENTSUSER");
		if (componentsRole == null) {
			componentsRole = new Role();
			componentsRole.setName("ROLE_COMPONENTSUSER");
			roleService.save(componentsRole);
			msg += "[role: " + componentsRole.getName() + "], ";
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

		if (parametersService.findAll().size() == 0) {
			QualitySurveyParameter param;

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać roztaw osi");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać roztaw płytek resorowych");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać roztaw wspornikówi");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać średnicę szczęk po przetaczaniu");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Podać moment skrętu osi (skrętnej)");
			param.setType(QualitySurveyParameter.PARAMETER_TEXT);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Moment dokręcenia nakrętki sworznia hamulca");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Poprawność działania ABS");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Dzwignia zgodna z rysunkiem/kat.Prod.");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Sprawdzić poprawność obrotu Piasty / Bębna");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Czy zamontowano i poprawnie zaplombowano  zawlęczkę");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Zgodność Konfiguracji osi z Rysunkiem technicznym");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Sprawdzić zbieżność");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";

			param = new QualitySurveyParameter();
			param.setActive(true);
			param.setTitle("Smarowanie zgodnie z instrukcją/kartą");
			param.setType(QualitySurveyParameter.PARAMETER_BOOLEAN);
			parametersService.save(param);
			msg += "[qs_param: " + param.getTitle() + "], ";
		}

		BhpTicketState bhpState;

		if (bhpTicketStateService.findByOrder(10) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(10);
			bhpState.setDescription("Nowy");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		if (bhpTicketStateService.findByOrder(20) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(20);
			bhpState.setDescription("Odczytany");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		if (bhpTicketStateService.findByOrder(25) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(25);
			bhpState.setDescription("Ponownie otwarty");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		if (bhpTicketStateService.findByOrder(30) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(30);
			bhpState.setDescription("Przekazany");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}
		
		if (bhpTicketStateService.findByOrder(32) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(32);
			bhpState.setDescription("Przekazany do UTR");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		if (bhpTicketStateService.findByOrder(35) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(35);
			bhpState.setDescription("Komentarz UTR");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		if (bhpTicketStateService.findByOrder(40) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(40);
			bhpState.setDescription("Zamknięty");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		if (bhpTicketStateService.findByOrder(90) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(90);
			bhpState.setDescription("Anulowany");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		if (bhpTicketStateService.findByOrder(95) == null) {
			bhpState = new BhpTicketState();
			bhpState.setOrder(95);
			bhpState.setDescription("Archiwum");
			bhpTicketStateService.save(bhpState);
			msg += "[bts_param: " + bhpState.getOrder() + " " + bhpState.getDescription() + "], ";
		}

		ToolsProjectState toolsState;

		if (toolsProjectStateService.findByOrder(10) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(10);
			toolsState.setDescription("Do przypisania");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(20) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(20);
			toolsState.setDescription("Przypisany");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(30) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(30);
			toolsState.setDescription("Wstrzymany");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(40) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(40);
			toolsState.setDescription("Do zatwierdzenia");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(50) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(50);
			toolsState.setDescription("Zatwierdzony");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(60) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(60);
			toolsState.setDescription("Zlecony w ADR");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(61) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(61);
			toolsState.setDescription("Zlecony dostawcy");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(70) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(70);
			toolsState.setDescription("W produkcji");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(80) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(80);
			toolsState.setDescription("Wyprodukowany");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		if (toolsProjectStateService.findByOrder(90) == null) {
			toolsState = new ToolsProjectState();
			toolsState.setOrder(90);
			toolsState.setDescription("Anulowany");
			toolsProjectStateService.save(toolsState);
			msg += "[tps_param: " + toolsState.getOrder() + " " + toolsState.getDescription() + "], ";
		}
		
		QSurveyQuestionType qSurveyQuestionType;
		
		if (qSurveyQuestionTypesService.findByCode("qsurveys.type.parameter") == null) {
			qSurveyQuestionType = new QSurveyQuestionType();
			qSurveyQuestionType.setCode("qsurveys.type.parameter");
			qSurveyQuestionType.setTitle("parameter");
			qSurveyQuestionTypesService.save(qSurveyQuestionType);
			msg += "[qsurvey_question_type: " + qSurveyQuestionType.getCode() + " " + qSurveyQuestionType.getTitle() + "], ";
		}
		
		if (qSurveyQuestionTypesService.findByCode("qsurveys.type.boolean") == null) {
			qSurveyQuestionType = new QSurveyQuestionType();
			qSurveyQuestionType.setCode("qsurveys.type.boolean");
			qSurveyQuestionType.setTitle("boolean");
			qSurveyQuestionTypesService.save(qSurveyQuestionType);
			msg += "[qsurvey_question_type: " + qSurveyQuestionType.getCode() + " " + qSurveyQuestionType.getTitle() + "], ";
		}	
		
		QCheckState qCheckState;
		
		if (qCheckStatesService.findByOrder(10) == null) {
			qCheckState = new QCheckState();
			qCheckState.setOrder(10);
			qCheckState.setTitle("new");
			qCheckState.setCode("qcheck.state.new");
			qCheckStatesService.save(qCheckState);
			msg += "[qcheckstate: " + qCheckState.getOrder() + " " + qCheckState.getTitle() + "], ";
		}
		if (qCheckStatesService.findByOrder(20) == null) {
			qCheckState = new QCheckState();
			qCheckState.setOrder(20);
			qCheckState.setTitle("in progress");
			qCheckState.setCode("qcheck.state.inprogress");
			qCheckStatesService.save(qCheckState);
			msg += "[qcheckstate: " + qCheckState.getOrder() + " " + qCheckState.getTitle() + "], ";
		}
		if (qCheckStatesService.findByOrder(30) == null) {
			qCheckState = new QCheckState();
			qCheckState.setOrder(30);
			qCheckState.setTitle("to correct");
			qCheckState.setCode("qcheck.state.tocorrect");
			qCheckStatesService.save(qCheckState);
			msg += "[qcheckstate: " + qCheckState.getOrder() + " " + qCheckState.getTitle() + "], ";
		}
		if (qCheckStatesService.findByOrder(40) == null) {
			qCheckState = new QCheckState();
			qCheckState.setOrder(40);
			qCheckState.setTitle("checked");
			qCheckState.setCode("qcheck.state.checked");
			qCheckStatesService.save(qCheckState);
			msg += "[qcheckstate: " + qCheckState.getOrder() + " " + qCheckState.getTitle() + "], ";
		}
		if (qCheckStatesService.findByOrder(90) == null) {
			qCheckState = new QCheckState();
			qCheckState.setOrder(90);
			qCheckState.setTitle("rejected");
			qCheckState.setCode("qcheck.state.rejected");
			qCheckStatesService.save(qCheckState);
			msg += "[qcheckstate: " + qCheckState.getOrder() + " " + qCheckState.getTitle() + "], ";
		}
				 
		
		InventoryDataType invDataType;
		// i18n codes
		String[] types = {
				"inventory.type.code",
				"inventory.type.address",
				"inventory.type.location",
				"inventory.type.label",
				"inventory.type.order.sale",
				"inventory.type.order.purchase",
				"inventory.type.packagetype",
				"inventory.type.quantity",
				"inventory.type.freestring1",
				"inventory.type.freestring2",
				"inventory.type.freedouble"
				};
		for(String type: types){
			if(inventoryDataTypesService.findByColumnTypeCode(type) == null){
				invDataType = new InventoryDataType();
				invDataType.setColumnTypeCode(type);
				inventoryDataTypesService.save(invDataType);
				msg += "[inv_datatype: " + invDataType.getColumnTypeCode() + "], ";
			}			
		}

		
		model.addAttribute("msg", msg);
		return "welcome";
	}

}

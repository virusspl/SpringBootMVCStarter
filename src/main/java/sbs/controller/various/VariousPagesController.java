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
import sbs.model.downtimes.DowntimeResponseType;
import sbs.model.downtimes.DowntimeType;
import sbs.model.inventory.InventoryDataType;
import sbs.model.qcheck.QCheckState;
import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.model.shipcust.ShipCustLineState;
import sbs.model.shipcust.ShipCustState;
import sbs.model.shipments.ShipmentState;
import sbs.model.tools.ToolsProjectState;
import sbs.model.users.Role;
import sbs.model.users.User;
import sbs.service.bhptickets.BhpTicketStateService;
import sbs.service.bhptickets.BhpTicketsService;
import sbs.service.downtimes.DowntimeResponseTypesService;
import sbs.service.downtimes.DowntimeTypesService;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.inventory.InventoryDataTypesService;
import sbs.service.mail.MailService;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.qcheck.QCheckStatesService;
import sbs.service.qsurveys.QSurveyQuestionTypesService;
import sbs.service.redmine.RedmineService;
import sbs.service.shipcust.CustomShipmentLineStatesService;
import sbs.service.shipcust.CustomShipmentStatesService;
import sbs.service.shipments.ShipmentStatesService;
import sbs.service.system.MemoryService;
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
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcAdrOptimaService jdbcAdrOptimaService;
	@Autowired
	JdbcOracleGeodeService jdbcOracleGeodeService;
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
	ShipmentStatesService shipmentStatesService;
	@Autowired
	private LiveFeedSingleton liveFeedSingleton;
	@Autowired
	InventoryDataTypesService inventoryDataTypesService;
	@Autowired
	DowntimeTypesService downtimeTypesService;
	@Autowired
	DowntimeResponseTypesService downtimeResponseTypesService;
	@Autowired 
	MathHelper mathHelper;
	@Autowired
	RedmineService redmineService;
	@Autowired
	CustomShipmentStatesService customShipmentStatesService;
	@Autowired
	CustomShipmentLineStatesService customShipmentLineStatesService;
	@Autowired
	MemoryService memoryService;
	
	@RequestMapping("/test")
	public String test(Model model, Locale locale) throws InterruptedException {

		
		//memoryService.debugListMemoryPoolBeans();
		
		/*ArrayList<String> list = new ArrayList<>();
		try {
			int stop = 0;
			while(stop<1) {
				list.add("!@#$%^&*()!@#$%^&*()_!@#$%^&*()_+!@#$%^&*()!@#$%^&*())!@#$%^&*()))))_");
			}
			
		}
		catch (OutOfMemoryError er) {
			System.out.println("out of memory!!");
		}
		//String result = x3Service.updateStandardCostsTable("WPS");
		//System.out.println(result);
		
		/*String sql = "insert into foo (id) values (hextoraw(?))";
		//PreparedStatement pstmt = connection.prepareStatement(sql);
		UUID uid = UUID.randomUUID();
		//pstmt.setString(1, uid.toString().replaceAll("-", ""));
		System.out.println(uid);
		System.out.println(uid.toString().replaceAll("-", ""));
		*/
		return "various/test";
	}
	
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
		
	@RequestMapping("/err")
	public String errorView(Model model){
		return "err";
	}
	
	@RequestMapping(value = "/terminal/test")
	public String terminalTest(Model model) {
		model.addAttribute("msg", "Current message from controller");
		model.addAttribute("live", "Live feed from server");
		model.addAttribute("warning", "Warning message during input");
		model.addAttribute("error", "Error message of any kind");
		return "welcome_terminal";
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
		
		Role qcheckUser = roleService.findByName("ROLE_QCHECKUSER");
		if (qcheckUser == null) {
			qcheckUser = new Role();
			qcheckUser.setName("ROLE_QCHECKUSER");
			roleService.save(qcheckUser);
			msg += "[role: " + qcheckUser.getName() + "], ";
		}
		
		Role qcheckManager = roleService.findByName("ROLE_QCHECKMANAGER");
		if (qcheckManager == null) {
			qcheckManager = new Role();
			qcheckManager.setName("ROLE_QCHECKMANAGER");
			roleService.save(qcheckManager);
			msg += "[role: " + qcheckManager.getName() + "], ";
		}
		
		Role cebsManager = roleService.findByName("ROLE_CEBSMANAGER");
		if (cebsManager == null) {
			cebsManager = new Role();
			cebsManager.setName("ROLE_CEBSMANAGER");
			roleService.save(cebsManager);
			msg += "[role: " + cebsManager.getName() + "], ";
		}
		
		Role cebsUser = roleService.findByName("ROLE_CEBSUSER");
		if (cebsUser == null) {
			cebsUser = new Role();
			cebsUser.setName("ROLE_CEBSUSER");
			roleService.save(cebsUser);
			msg += "[role: " + cebsUser.getName() + "], ";
		}
		
		Role shipmentsManager = roleService.findByName("ROLE_SHIPMENTSMANAGER");
		if (shipmentsManager == null) {
			shipmentsManager = new Role();
			shipmentsManager.setName("ROLE_SHIPMENTSMANAGER");
			roleService.save(shipmentsManager);
			msg += "[role: " + shipmentsManager.getName() + "], ";
		}
		
		Role shipmentsTerminal = roleService.findByName("ROLE_SHIPMENTSTERMINAL");
		if (shipmentsTerminal == null) {
			shipmentsTerminal = new Role();
			shipmentsTerminal.setName("ROLE_SHIPMENTSTERMINAL");
			roleService.save(shipmentsTerminal);
			msg += "[role: " + shipmentsTerminal.getName() + "], ";
		}
		
		Role inventoryTerminal = roleService.findByName("ROLE_INVENTORYTERMINAL");
		if (inventoryTerminal == null) {
			inventoryTerminal = new Role();
			inventoryTerminal.setName("ROLE_INVENTORYTERMINAL");
			roleService.save(inventoryTerminal);
			msg += "[role: " + inventoryTerminal.getName() + "], ";
		}
		
		Role prodOriginUser = roleService.findByName("ROLE_PRODORIGINUSER");
		if (prodOriginUser == null) {
			prodOriginUser = new Role();
			prodOriginUser.setName("ROLE_PRODORIGINUSER");
			roleService.save(prodOriginUser);
			msg += "[role: " + prodOriginUser.getName() + "], ";
		}
		
		Role saleShipUser = roleService.findByName("ROLE_SALESHIPUSER");
		if (saleShipUser == null) {
			saleShipUser = new Role();
			saleShipUser.setName("ROLE_SALESHIPUSER");
			roleService.save(saleShipUser);
			msg += "[role: " + saleShipUser.getName() + "], ";
		}
		
		Role compReqManager = roleService.findByName("ROLE_COMPREQMANAGER");
		if (compReqManager == null) {
			compReqManager = new Role();
			compReqManager.setName("ROLE_COMPREQMANAGER");
			roleService.save(compReqManager);
			msg += "[role: " + compReqManager.getName() + "], ";
		}
		
		Role industryManager = roleService.findByName("ROLE_INDUSTRYMANAGER");
		if (industryManager == null) {
			industryManager = new Role();
			industryManager.setName("ROLE_INDUSTRYMANAGER");
			roleService.save(industryManager);
			msg += "[role: " + industryManager.getName() + "], ";
		}
		
		Role environment = roleService.findByName("ROLE_ENVIRONMENTUSER");
		if (environment == null) {
			environment = new Role();
			environment.setName("ROLE_ENVIRONMENTUSER");
			roleService.save(environment);
			msg += "[role: " + environment.getName() + "], ";
		}
		
		Role downtimeResponsible = roleService.findByName("ROLE_DTRESPONSIBLE");
		if (downtimeResponsible == null) {
			downtimeResponsible = new Role();
			downtimeResponsible.setName("ROLE_DTRESPONSIBLE");
			roleService.save(downtimeResponsible);
			msg += "[role: " + downtimeResponsible.getName() + "], ";
		}
		
		Role downtimeNotifier = roleService.findByName("ROLE_DTNOTIFIER");
		if (downtimeNotifier == null) {
			downtimeNotifier = new Role();
			downtimeNotifier.setName("ROLE_DTNOTIFIER");
			roleService.save(downtimeNotifier);
			msg += "[role: " + downtimeNotifier.getName() + "], ";
		}
		
		Role downtimeSupervisor = roleService.findByName("ROLE_DTSUPERVISOR");
		if (downtimeSupervisor == null) {
			downtimeSupervisor = new Role();
			downtimeSupervisor.setName("ROLE_DTSUPERVISOR");
			roleService.save(downtimeSupervisor);
			msg += "[role: " + downtimeSupervisor.getName() + "], ";
		}
		
		Role avgPricesUser = roleService.findByName("ROLE_AVGPRICESUSER");
		if (avgPricesUser == null) {
			avgPricesUser = new Role();
			avgPricesUser.setName("ROLE_AVGPRICESUSER");
			roleService.save(avgPricesUser);
			msg += "[role: " + avgPricesUser.getName() + "], ";
		}
		
		Role stockSumUser = roleService.findByName("ROLE_STOCKSUMUSER");
		if (stockSumUser == null) {
			stockSumUser = new Role();
			stockSumUser.setName("ROLE_STOCKSUMUSER");
			roleService.save(stockSumUser);
			msg += "[role: " + stockSumUser.getName() + "], ";
		}
		
		Role rcpToSaleUser = roleService.findByName("ROLE_RCPTOSALEUSER");
		if (rcpToSaleUser == null) {
			rcpToSaleUser = new Role();
			rcpToSaleUser.setName("ROLE_RCPTOSALEUSER");
			roleService.save(rcpToSaleUser);
			msg += "[role: " + rcpToSaleUser.getName() + "], ";
		}
		
		Role shipCustSales = roleService.findByName("ROLE_SHIPCUST_SALES");
		if (shipCustSales == null) {
			shipCustSales = new Role();
			shipCustSales.setName("ROLE_SHIPCUST_SALES");
			roleService.save(shipCustSales);
			msg += "[role: " + shipCustSales.getName() + "], ";
		}
		
		Role shipCustSpare = roleService.findByName("ROLE_SHIPCUST_SPARE");
		if (shipCustSpare == null) {
			shipCustSpare = new Role();
			shipCustSpare.setName("ROLE_SHIPCUST_SPARE");
			roleService.save(shipCustSpare);
			msg += "[role: " + shipCustSpare.getName() + "], ";
		}
		
		Role shipCustAcq = roleService.findByName("ROLE_SHIPCUST_ACQ");
		if (shipCustAcq == null) {
			shipCustAcq = new Role();
			shipCustAcq.setName("ROLE_SHIPCUST_ACQ");
			roleService.save(shipCustAcq);
			msg += "[role: " + shipCustAcq.getName() + "], ";
		}
		
		Role shipCustShip = roleService.findByName("ROLE_SHIPCUST_SHIP");
		if (shipCustShip == null) {
			shipCustShip = new Role();
			shipCustShip.setName("ROLE_SHIPCUST_SHIP");
			roleService.save(shipCustShip);
			msg += "[role: " + shipCustShip.getName() + "], ";
		}
		
		Role histock = roleService.findByName("ROLE_HISTOCKUSER");
		if (histock == null) {
			histock = new Role();
			histock.setName("ROLE_HISTOCKUSER");
			roleService.save(histock);
			msg += "[role: " + histock.getName() + "], ";
		}
		
		// ROLE END

		User admin = userService.findByUsername("Admin");
		User user = userService.findByUsername("User");
		User krzysiek = userService.findByUsername("Krzysiek");
		if (admin == null) {
			// object
			admin = new User();
			admin.setUsername("Admin");
			admin.setPassword("$2a$04$7gatGBXoTWA.rLmFONVz/Oepajcpp7FTOwaAiFjEQOx/BPMm/gJL6");
			admin.setName("Admin Name");
			admin.setEmail("admin@sjava.com");
			admin.setRcpNumber("");
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
		if (krzysiek == null) {
			// object
			krzysiek = new User();
			krzysiek.setUsername("Krzysiek");
			krzysiek.setPassword("$2a$04$UZWCi1I779DTvZzdYI/YG.oRidHjWNsxQcW9I7QqapOrYE8tXelu6");
			krzysiek.setName("Krzysztof Michalak");
			krzysiek.setEmail("michalak.k@atwsystem.pl");
			krzysiek.setRcpNumber("");
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
			user.setRcpNumber("");
			user.setActive(false);
			// many to many
			user.getRoles().add(userRole);
			userRole.getUsers().add(user);
			// save
			userService.save(user);
			msg += "[user: " + user.getUsername() + "], ";
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
			qCheckState.setTitle("tocheck");
			qCheckState.setCode("qcheck.state.tocheck");
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
			qCheckState.setTitle("suspended");
			qCheckState.setCode("qcheck.state.suspended");
			qCheckStatesService.save(qCheckState);
			msg += "[qcheckstate: " + qCheckState.getOrder() + " " + qCheckState.getTitle() + "], ";
		}
		if (qCheckStatesService.findByOrder(40) == null) {
			qCheckState = new QCheckState();
			qCheckState.setOrder(40);
			qCheckState.setTitle("to correct");
			qCheckState.setCode("qcheck.state.tocorrect");
			qCheckStatesService.save(qCheckState);
			msg += "[qcheckstate: " + qCheckState.getOrder() + " " + qCheckState.getTitle() + "], ";
		}
		if (qCheckStatesService.findByOrder(50) == null) {
			qCheckState = new QCheckState();
			qCheckState.setOrder(50);
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
		
		ShipmentState shipmentState;
		
		if (shipmentStatesService.findByOrder(10) == null) {
			shipmentState = new ShipmentState();
			shipmentState.setOrder(10);
			shipmentState.setInternalTitle("new");
			shipmentState.setCode("shipments.state.new");
			shipmentStatesService.save(shipmentState);
			msg += "[shipmentState: " + shipmentState.getOrder() + " " + shipmentState.getInternalTitle() + "], ";
		}
		if (shipmentStatesService.findByOrder(20) == null) {
			shipmentState = new ShipmentState();
			shipmentState.setOrder(20);
			shipmentState.setInternalTitle("in progress");
			shipmentState.setCode("shipments.state.inprogress");
			shipmentStatesService.save(shipmentState);
			msg += "[shipmentState: " + shipmentState.getOrder() + " " + shipmentState.getInternalTitle() + "], ";
		}
		if (shipmentStatesService.findByOrder(30) == null) {
			shipmentState = new ShipmentState();
			shipmentState.setOrder(30);
			shipmentState.setInternalTitle("completed");
			shipmentState.setCode("shipments.state.completed");
			shipmentStatesService.save(shipmentState);
			msg += "[shipmentState: " + shipmentState.getOrder() + " " + shipmentState.getInternalTitle() + "], ";
		}
		if (shipmentStatesService.findByOrder(40) == null) {
			shipmentState = new ShipmentState();
			shipmentState.setOrder(40);
			shipmentState.setInternalTitle("shipped");
			shipmentState.setCode("shipments.state.shipped");
			shipmentStatesService.save(shipmentState);
			msg += "[shipmentState: " + shipmentState.getOrder() + " " + shipmentState.getInternalTitle() + "], ";
		}
		if (shipmentStatesService.findByOrder(90) == null) {
			shipmentState = new ShipmentState();
			shipmentState.setOrder(90);
			shipmentState.setInternalTitle("cancelled");
			shipmentState.setCode("shipments.state.cancelled");
			shipmentStatesService.save(shipmentState);
			msg += "[shipmentState: " + shipmentState.getOrder() + " " + shipmentState.getInternalTitle() + "], ";
		}
		
		DowntimeType downtimeType;
		
		if (downtimeTypesService.findByOrder(10) == null) {
			downtimeType = new DowntimeType();
			downtimeType.setOrder(10);
			downtimeType.setInternalTitle("fault");
			downtimeType.setCode("downtimes.type.fault");
			downtimeTypesService.save(downtimeType);
			msg += "[downtimeType: " + downtimeType.getOrder() + " " + downtimeType.getInternalTitle() + "], ";
		}

		if (downtimeTypesService.findByOrder(20) == null) {
			downtimeType = new DowntimeType();
			downtimeType.setOrder(20);
			downtimeType.setInternalTitle("material");
			downtimeType.setCode("downtimes.type.material");
			downtimeTypesService.save(downtimeType);
			msg += "[downtimeType: " + downtimeType.getOrder() + " " + downtimeType.getInternalTitle() + "], ";
		}

		if (downtimeTypesService.findByOrder(30) == null) {
			downtimeType = new DowntimeType();
			downtimeType.setOrder(30);
			downtimeType.setInternalTitle("quality");
			downtimeType.setCode("downtimes.type.quality");
			downtimeTypesService.save(downtimeType);
			msg += "[downtimeType: " + downtimeType.getOrder() + " " + downtimeType.getInternalTitle() + "], ";
		}

		if (downtimeTypesService.findByOrder(40) == null) {
			downtimeType = new DowntimeType();
			downtimeType.setOrder(40);
			downtimeType.setInternalTitle("safety");
			downtimeType.setCode("downtimes.type.safety");
			downtimeTypesService.save(downtimeType);
			msg += "[downtimeType: " + downtimeType.getOrder() + " " + downtimeType.getInternalTitle() + "], ";
		}

		if (downtimeTypesService.findByOrder(50) == null) {
			downtimeType = new DowntimeType();
			downtimeType.setOrder(50);
			downtimeType.setInternalTitle("other");
			downtimeType.setCode("downtimes.type.other");
			downtimeTypesService.save(downtimeType);
			msg += "[downtimeType: " + downtimeType.getOrder() + " " + downtimeType.getInternalTitle() + "], ";
		}
		
		DowntimeResponseType downtimeResponseType;
		
		if (downtimeResponseTypesService.findByOrder(10) == null) {
			downtimeResponseType = new DowntimeResponseType();
			downtimeResponseType.setOrder(10);
			downtimeResponseType.setInternalTitle("empty");
			downtimeResponseType.setCode("downtimes.response.empty");
			downtimeResponseTypesService.save(downtimeResponseType);
			msg += "[downtimeResponseType: " + downtimeResponseType.getOrder() + " " + downtimeResponseType.getInternalTitle() + "], ";
		}	 
		
		if (downtimeResponseTypesService.findByOrder(20) == null) {
			downtimeResponseType = new DowntimeResponseType();
			downtimeResponseType.setOrder(20);
			downtimeResponseType.setInternalTitle("accepted");
			downtimeResponseType.setCode("downtimes.response.accepted");
			downtimeResponseTypesService.save(downtimeResponseType);
			msg += "[downtimeResponseType: " + downtimeResponseType.getOrder() + " " + downtimeResponseType.getInternalTitle() + "], ";
		}	 
		
		if (downtimeResponseTypesService.findByOrder(30) == null) {
			downtimeResponseType = new DowntimeResponseType();
			downtimeResponseType.setOrder(30);
			downtimeResponseType.setInternalTitle("rejected");
			downtimeResponseType.setCode("downtimes.response.rejected");
			downtimeResponseTypesService.save(downtimeResponseType);
			msg += "[downtimeResponseType: " + downtimeResponseType.getOrder() + " " + downtimeResponseType.getInternalTitle() + "], ";
		}	 
		
		if (downtimeResponseTypesService.findByOrder(40) == null) {
			downtimeResponseType = new DowntimeResponseType();
			downtimeResponseType.setOrder(40);
			downtimeResponseType.setInternalTitle("forwarded");
			downtimeResponseType.setCode("downtimes.response.forwarded");
			downtimeResponseTypesService.save(downtimeResponseType);
			msg += "[downtimeResponseType: " + downtimeResponseType.getOrder() + " " + downtimeResponseType.getInternalTitle() + "], ";
		}	 
		
		ShipCustState shipCustState;
		
		if (customShipmentStatesService.findByOrder(10) == null) {
			shipCustState = new ShipCustState();
			shipCustState.setOrder(10);
			shipCustState.setTitle("waiting");
			shipCustState.setCode("shipcust.state.waiting");
			customShipmentStatesService.save(shipCustState);
			msg += "[shipCustState: " + shipCustState.getOrder() + " " + shipCustState.getTitle() + "], ";
		}	
		if (customShipmentStatesService.findByOrder(20) == null) {
			shipCustState = new ShipCustState();
			shipCustState.setOrder(20);
			shipCustState.setTitle("partially");
			shipCustState.setCode("shipcust.state.part");
			customShipmentStatesService.save(shipCustState);
			msg += "[shipCustState: " + shipCustState.getOrder() + " " + shipCustState.getTitle() + "], ";
		}	
		if (customShipmentStatesService.findByOrder(30) == null) {
			shipCustState = new ShipCustState();
			shipCustState.setOrder(30);
			shipCustState.setTitle("closed");
			shipCustState.setCode("shipcust.state.closed");
			customShipmentStatesService.save(shipCustState);
			msg += "[shipCustState: " + shipCustState.getOrder() + " " + shipCustState.getTitle() + "], ";
		}	
		if (customShipmentStatesService.findByOrder(40) == null) {
			shipCustState = new ShipCustState();
			shipCustState.setOrder(40);
			shipCustState.setTitle("cancelled");
			shipCustState.setCode("shipcust.state.cancelled");
			customShipmentStatesService.save(shipCustState);
			msg += "[shipCustState: " + shipCustState.getOrder() + " " + shipCustState.getTitle() + "], ";
		}	

		ShipCustLineState shipCustLineState;
		
		if (customShipmentLineStatesService.findByOrder(5) == null) {
			shipCustLineState = new ShipCustLineState();
			shipCustLineState.setOrder(5);
			shipCustLineState.setTitle("spare");
			shipCustLineState.setCode("shipcust.line.state.spare");
			customShipmentLineStatesService.save(shipCustLineState);
			msg += "[shipCustLineState: " + shipCustLineState.getOrder() + " " + shipCustLineState.getTitle() + "], ";
		}
		
		if (customShipmentLineStatesService.findByOrder(10) == null) {
			shipCustLineState = new ShipCustLineState();
			shipCustLineState.setOrder(10);
			shipCustLineState.setTitle("waiting");
			shipCustLineState.setCode("shipcust.line.state.waiting");
			customShipmentLineStatesService.save(shipCustLineState);
			msg += "[shipCustLineState: " + shipCustLineState.getOrder() + " " + shipCustLineState.getTitle() + "], ";
		}
		if (customShipmentLineStatesService.findByOrder(20) == null) {
			shipCustLineState = new ShipCustLineState();
			shipCustLineState.setOrder(20);
			shipCustLineState.setTitle("lack");
			shipCustLineState.setCode("shipcust.line.state.lack");
			customShipmentLineStatesService.save(shipCustLineState);
			msg += "[shipCustLineState: " + shipCustLineState.getOrder() + " " + shipCustLineState.getTitle() + "], ";
		}
		if (customShipmentLineStatesService.findByOrder(30) == null) {
			shipCustLineState = new ShipCustLineState();
			shipCustLineState.setOrder(30);
			shipCustLineState.setTitle("ready");
			shipCustLineState.setCode("shipcust.line.state.ready");
			customShipmentLineStatesService.save(shipCustLineState);
			msg += "[shipCustLineState: " + shipCustLineState.getOrder() + " " + shipCustLineState.getTitle() + "], ";
		}
		if (customShipmentLineStatesService.findByOrder(40) == null) {
			shipCustLineState = new ShipCustLineState();
			shipCustLineState.setOrder(40);
			shipCustLineState.setTitle("shipped");
			shipCustLineState.setCode("shipcust.line.state.shipped");
			customShipmentLineStatesService.save(shipCustLineState);
			msg += "[shipCustLineState: " + shipCustLineState.getOrder() + " " + shipCustLineState.getTitle() + "], ";
		}
		if (customShipmentLineStatesService.findByOrder(50) == null) {
			shipCustLineState = new ShipCustLineState();
			shipCustLineState.setOrder(50);
			shipCustLineState.setTitle("notshipped");
			shipCustLineState.setCode("shipcust.line.state.notshipped");
			customShipmentLineStatesService.save(shipCustLineState);
			msg += "[shipCustLineState: " + shipCustLineState.getOrder() + " " + shipCustLineState.getTitle() + "], ";
		}
		if (customShipmentLineStatesService.findByOrder(60) == null) {
			shipCustLineState = new ShipCustLineState();
			shipCustLineState.setOrder(60);
			shipCustLineState.setTitle("cancelled");
			shipCustLineState.setCode("shipcust.line.state.cancelled");
			customShipmentLineStatesService.save(shipCustLineState);
			msg += "[shipCustLineState: " + shipCustLineState.getOrder() + " " + shipCustLineState.getTitle() + "], ";
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

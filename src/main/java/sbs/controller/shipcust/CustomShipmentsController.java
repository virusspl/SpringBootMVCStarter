package sbs.controller.shipcust;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javassist.NotFoundException;
import sbs.helpers.TextHelper;
import sbs.model.shipcust.CustomShipment;
import sbs.model.shipcust.CustomShipmentLine;
import sbs.model.shipcust.ShipCustLineState;
import sbs.model.shipcust.ShipCustState;
import sbs.model.shipcust.ShipCustTransport;
import sbs.model.users.User;
import sbs.model.x3.X3Client;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3SalesOrder;
import sbs.service.mail.MailService;
import sbs.service.shipcust.CustomShipmentLineStatesService;
import sbs.service.shipcust.CustomShipmentLinesService;
import sbs.service.shipcust.CustomShipmentStatesService;
import sbs.service.shipcust.CustomShipmentTransportService;
import sbs.service.shipcust.CustomShipmentsService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("shipcust")
public class CustomShipmentsController {

	private static final int MAIL_NEW_ORDER = 1;
	private static final int MAIL_NEW_SPARE_LINE = 2;
	private static final int MAIL_NEW_ACQ_LINE = 3;
	private static final int MAIL_LINE_SERVED = 4;
	private static final int MAIL_ORDER_CLOSED = 5;

	@Autowired
	CustomShipmentsService shipmentsService;
	@Autowired
	CustomShipmentStatesService shipmentStatesService;
	@Autowired
	CustomShipmentLinesService shipmentLinesService;
	@Autowired
	CustomShipmentLineStatesService shipmentLineStatesService;
	@Autowired
	CustomShipmentTransportService transportService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	MessageSource messageSource;
	@Autowired
	UserService userService;
	@Autowired
	TextHelper textHelper;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;

	public CustomShipmentsController() {

	}

	@RequestMapping(value = "/dispatch")
	public String dispatch() {
		User user = userService.getAuthenticatedUser();
		if (user.hasRole("ROLE_ADMIN")) {
			return "shipcust/dispatch";
		} else if (user.hasRole("ROLE_SHIPCUST_SALES")) {
			return "redirect:/shipcust/sales";
		} else if (user.hasRole("ROLE_SHIPCUST_SPARE")) {
			return "redirect:/shipcust/spare";
		} else if (user.hasRole("ROLE_SHIPCUST_ACQ")) {
			return "redirect:/shipcust/acq";
		} else if (user.hasRole("ROLE_SHIPCUST_SHIP")) {
			return "redirect:/shipcust/ship";
		}
		return "shipcust/dispatch";
	}

	@RequestMapping(value = "/archive")
	public String dispatchArchive(Model model) {
		List<CustomShipment> orders = shipmentsService.findAllClosed();
		model.addAttribute("orders", orders);
		model.addAttribute("subtitle", "general.archive");
		return "shipcust/list";
	}

	@RequestMapping(value = "/sales")
	public String dispatchSales(Model model) {
		List<CustomShipment> orders = shipmentsService.findAllPending();
		model.addAttribute("orders", orders);
		model.addAttribute("subtitle", "shipcust.sales");
		return "shipcust/list";
	}

	@RequestMapping(value = "/ship")
	public String dispatchShip(Model model) {
		List<CustomShipment> orders = shipmentsService.findAllPending();
		model.addAttribute("orders", orders);
		model.addAttribute("subtitle", "shipcust.ship");
		return "shipcust/list";
	}

	@RequestMapping(value = "/spare")
	@Transactional
	public String dispatchSpare(Model model) {
		List<CustomShipmentLine> lines = shipmentLinesService
				.findAllPendingSpare(CustomShipmentLinesService.SPARE_PROD);
		model.addAttribute("spare", lines);
		model.addAttribute("subtitle", "shipcust.spare");
		return "shipcust/list";
	}

	@RequestMapping(value = "/acq")
	@Transactional
	public String dispatchAcq(Model model) {
		List<CustomShipmentLine> lines = shipmentLinesService.findAllPendingSpare(CustomShipmentLinesService.SPARE_ACQ);
		model.addAttribute("spare", lines);
		model.addAttribute("subtitle", "shipcust.acq");
		return "shipcust/list";
	}

	@RequestMapping(value = "/sales/transport/manage")
	public String showManageTransport(Model model) {
		model.addAttribute("transportList", transportService.findAll());
		return "shipcust/transportmanage";
	}

	@RequestMapping(value = "/sales/transport/switch/{id}")
	@Transactional
	public String switchTransport(@PathVariable("id") int id, Model model) throws NotFoundException {
		ShipCustTransport tr = transportService.findById(id);
		if (tr == null) {
			throw new NotFoundException("Transport not found: #" + id);
		}
		tr.setActive(!tr.isActive());
		transportService.update(tr);
		return "redirect:/shipcust/sales/transport/manage";
	}

	@RequestMapping(value = "/sales/transport/create")
	public String showCreateTransport(Model model) {
		model.addAttribute("formCreateTransport", new FormCreateTransport());
		return "shipcust/transportcreate";
	}

	@RequestMapping(value = "/sales/transport/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String saveTransport(@Valid FormCreateTransport formCreateTransport, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		// validate
		if (bindingResult.hasErrors()) {
			return "shipcust/transportcreate";
		}

		ShipCustTransport transport = new ShipCustTransport();
		transport.setActive(true);
		transport.setName(formCreateTransport.getTransport().toUpperCase().trim());
		transportService.save(transport);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale) + " "
				+ transport.getName() + " #" + transport.getId());
		return "redirect:/shipcust/sales/transport/manage";
	}

	@RequestMapping(value = "/sales/order/create")
	public String showCreateOrder(Model model) {
		model.addAttribute("formCreateCustomShipment", new FormCreateCustomShipment());
		model.addAttribute("transportList", transportService.findAllActive());
		return "shipcust/shipmentcreate";
	}

	@RequestMapping(value = "/sales/order/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String saveShipmentOrder(@Valid FormCreateCustomShipment formCreateCustomShipment,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException, UnknownHostException, MessagingException {

		// validate
		if (bindingResult.hasErrors()) {
			model.addAttribute("transportList", transportService.findAllActive());
			return "shipcust/shipmentcreate";
		}

		ShipCustTransport transport = transportService.findById(formCreateCustomShipment.getTransportId());
		if (transport == null) {
			throw new NotFoundException("Unknown transport type: " + formCreateCustomShipment.getTransportId());
		}

		X3Client client = x3Service.findClientByCode("ATW", formCreateCustomShipment.getClientCode().trim());
		if (client == null) {
			model.addAttribute("transportList", transportService.findAllActive());
			bindingResult.rejectValue("clientCode", "error.client.not.found", "ERROR");
			return "shipcust/shipmentcreate";
		}

		ShipCustState waitingState = shipmentStatesService.findByOrder(10);
		if (waitingState == null) {
			throw new NotFoundException("Unknown custom shipment state: " + 10);
		}

		CustomShipment ship = new CustomShipment();
		ship.setCreator(userService.getAuthenticatedUser());
		ship.setState(waitingState);
		ship.setTransport(transport);
		ship.setClientCode(client.getCode());
		ship.setClientName(client.getName());
		ship.setComment(formCreateCustomShipment.getComment().trim());
		ship.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		ship.setUpdateDate(ship.getCreationDate());
		ship.setStartDate(new Timestamp(formCreateCustomShipment.getStartDate().getTime()));
		ship.setEndDate(new Timestamp(formCreateCustomShipment.getEndDate().getTime()));
		ship.setNotifiedByMail(false);

		shipmentsService.save(ship);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/shipcust/show/order/" + ship.getId();
	}

	@RequestMapping(value = "/sales/order/notify/{id}")
	@Transactional
	public String sendNotification(@PathVariable("id") int id, Model model, Locale locale,
			RedirectAttributes redirectAttrs) throws NotFoundException, UnknownHostException, MessagingException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}
		shipment.setNotifiedByMail(true);
		shipmentsService.update(shipment);

		this.sendMail(shipment.getId(), MAIL_NEW_ORDER, locale);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("email.sent", null, locale));
		return "redirect:/shipcust/show/order/" + shipment.getId();
	}

	@RequestMapping(value = "/sales/order/cancel/{id}")
	@Transactional
	public String cancelOrder(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		// return if closed or cancelled
		if (shipment.getState().getOrder() >= 30) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("shipcust.error.cantmodify", null, locale) + ": "
							+ shipment.getState().getTitle());
			return "redirect:/shipcust/show/order/" + shipment.getId();
		}

		ShipCustLineState stateLine = shipmentLineStatesService.findByOrder(60);
		if (stateLine == null) {
			throw new NotFoundException("Unknown custom shipment line state: " + 60);
		}

		ShipCustState stateOrder = shipmentStatesService.findByOrder(40);
		if (stateOrder == null) {
			throw new NotFoundException("Unknown custom shipment state: " + 40);
		}

		String updateUser = userService.getAuthenticatedUser().getName();
		Timestamp updateDate = new Timestamp((new java.util.Date()).getTime());

		shipment.setState(stateOrder);
		for (CustomShipmentLine line : shipment.getLines()) {
			if (line.getState().getOrder() < 40) {
				line.setState(stateLine);
				line.setSalesActionDate(updateDate);
				line.setSalesActionUserName(updateUser);
				shipmentLinesService.update(line);
			}
		}
		shipmentsService.update(shipment);

		redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("action.cancelled", null, locale) + ": #"
				+ shipment.getId() + " [" + shipment.getClientName() + "]");
		return "redirect:/shipcust/show/order/" + shipment.getId();
	}

	@RequestMapping(value = "/show/order/{id}")
	@Transactional
	public String showOrder(@PathVariable("id") int id, Model model) throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		model.addAttribute("sh", shipment);
		Map<Integer, CustomShipmentLine> linesMap = new TreeMap<>();
		for (CustomShipmentLine line : shipment.getLines()) {
			linesMap.put(line.getId(), line);
		}

		model.addAttribute("lines", linesMap.values());

		return "shipcust/shipmentshow";
	}

	@RequestMapping(value = "/print/order/{id}")
	@Transactional
	public String printOrder(@PathVariable("id") int id, Model model) throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		model.addAttribute("sh", shipment);
		Map<Integer, CustomShipmentLine> linesMap = new TreeMap<>();
		for (CustomShipmentLine line : shipment.getLines()) {
			linesMap.put(line.getId(), line);
		}

		model.addAttribute("lines", linesMap.values());

		return "shipcust/shipmentprint";
	}

	@RequestMapping(value = "/sales/line/create/{id}")
	public String showCreateLine(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs,
			Locale locale) throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		// return if closed or cancelled
		if (shipment.getState().getOrder() >= 30) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("shipcust.error.cantmodify", null, locale) + ": "
							+ shipment.getState().getTitle());
			return "redirect:/shipcust/show/order/" + shipment.getId();
		}

		FormCreateCustomShipmentLine formCreateCustomShipmentLine = new FormCreateCustomShipmentLine(shipment);
		model.addAttribute("formCreateCustomShipmentLine", formCreateCustomShipmentLine);

		return "shipcust/linecreate";
	}

	@RequestMapping(value = "/sales/line/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String saveShipmentLine(@Valid FormCreateCustomShipmentLine formCreateCustomShipmentLine,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException, UnknownHostException, MessagingException {

		// VALIDATE
		// short name :)
		FormCreateCustomShipmentLine form = formCreateCustomShipmentLine;
		// validate
		if (bindingResult.hasErrors()) {
			return "shipcust/linecreate";
		}

		X3Product product = x3Service.findProductByCode("ATW", form.getProductCode().trim());
		if (product == null) {
			bindingResult.rejectValue("productCode", "error.no.such.product", "ERROR");
		}

		X3SalesOrder order = x3Service.findSalesOrderByNumber("ATW", form.getSalesOrder().trim());
		if (order == null) {
			bindingResult.rejectValue("salesOrder", "error.no.such.order", "ERROR");
		}

		if (bindingResult.hasErrors()) {
			return "shipcust/linecreate";
		}

		// LINKED OBJECTS
		CustomShipment shipment = shipmentsService.findById(form.getShipmentId());
		if (shipment == null) {
			throw new NotFoundException("Unknown custom shipment: " + form.getShipmentId());
		}

		int stateOrder = form.isSpare() ? 5 : 10;
		ShipCustLineState state = shipmentLineStatesService.findByOrder(stateOrder);
		if (state == null) {
			throw new NotFoundException("Unknown custom shipment state: " + stateOrder);
		}

		// CREATE LINE
		CustomShipmentLine line = new CustomShipmentLine();
		line.setShipment(shipment);
		line.setState(state);

		line.setProductCode(product.getCode());
		line.setProductDescription(product.getDescription());
		line.setProductCategory(product.getCategory());

		line.setQuantityDemanded(form.getQuantityDemanded());
		line.setQuantityShipped(0);

		line.setSalesOrder(order.getSalesNumber());
		line.setWaybill("");
		line.setAddition(form.isAddition());
		line.setCertificate(form.isCertificate());
		line.setSpare(form.isSpare());

		line.setSalesComment(form.getComment().trim());
		line.setSpareComment("");
		line.setShipmentComment("");

		line.setSalesActionDate(new Timestamp((new java.util.Date()).getTime()));
		line.setSalesActionUserName(userService.getAuthenticatedUser().getName());

		shipmentLinesService.save(line);
		shipment.getLines().add(line);

		if (line.isSpare()) {
			if (line.getProductCategory().equalsIgnoreCase("ACV")) {
				this.sendMail(line.getId(), MAIL_NEW_ACQ_LINE, locale);
			} else {
				this.sendMail(line.getId(), MAIL_NEW_SPARE_LINE, locale);
			}
		}

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale) + ": "
				+ line.getProductCode() + " - " + line.getProductDescription());

		return "redirect:/shipcust/show/order/" + shipment.getId();

	}

	@RequestMapping(value = "/show/line/{id}")
	@Transactional
	public String showLine(@PathVariable("id") int id, Model model) throws NotFoundException {
		CustomShipmentLine line = shipmentLinesService.findById(id);
		if (line == null) {
			throw new NotFoundException("Unknown shipment line: #" + id);
		}
		Hibernate.initialize(line.getShipment());
		model.addAttribute("line", line);
		return "shipcust/lineshow";
	}

	@RequestMapping(value = "/sales/line/cancel/{id}")
	@Transactional
	public String lineCancel(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {

		// get line
		CustomShipmentLine line = shipmentLinesService.findById(id);
		if (line == null) {
			throw new NotFoundException("Unknown shipment line: #" + id);
		}

		// allow only for pending
		if (line.getState().getOrder() < 40) {
			ShipCustLineState stateLineCancel = shipmentLineStatesService.findByOrder(60);
			if (stateLineCancel == null) {
				throw new NotFoundException("Unknown custom shipment line state: " + 60);
			}
			line.setState(stateLineCancel);
			line.setSalesActionDate(new Timestamp((new java.util.Date()).getTime()));
			line.setSalesActionUserName(userService.getAuthenticatedUser().getName());
			shipmentLinesService.update(line);
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		} else {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("shipcust.error.cantmodify", null, locale) + ": "
							+ line.getState().getTitle());
		}
		return "redirect:/shipcust/show/line/" + line.getId();
	}

	@RequestMapping(value = "/sales/line/delete/{id}")
	@Transactional
	public String lineDelete(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {

		// get line
		CustomShipmentLine line = shipmentLinesService.findById(id);
		if (line == null) {
			throw new NotFoundException("Unknown shipment line: #" + id);
		}

		// allow only for not started
		if (line.getState().getOrder() < 20) {
			int shipId = line.getShipment().getId();
			shipmentLinesService.remove(line);
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.deleted", null, locale));
			return "redirect:/shipcust/show/order/" + shipId;
		} else {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("shipcust.error.cantmodify", null, locale) + ": "
							+ line.getState().getTitle());
			return "redirect:/shipcust/show/line/" + line.getId();
		}
	}

	@RequestMapping(value = "/line/lack/{id}")
	@Transactional
	public String lineLack(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException, UnknownHostException, MessagingException {
		return serveSpareAction(id, model, redirectAttrs, locale, 20);
	}

	@RequestMapping(value = "/line/ready/{id}")
	@Transactional
	public String lineReady(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException, UnknownHostException, MessagingException {
		return serveSpareAction(id, model, redirectAttrs, locale, 30);
	}

	private String serveSpareAction(int id, Model model, RedirectAttributes redirectAttrs, Locale locale,
			int stateOrder) throws NotFoundException, UnknownHostException, MessagingException {

		// get line
		CustomShipmentLine line = shipmentLinesService.findById(id);
		if (line == null) {
			throw new NotFoundException("Unknown shipment line: #" + id);
		}

		// allow only for spare state
		if (line.getState().getOrder() == 5) {
			// check if code type is correct for user role
			if ((line.getProductCategory().equalsIgnoreCase("ACV")
					&& !userService.getAuthenticatedUser().hasRole("ROLE_SHIPCUST_ACQ"))
					|| (!line.getProductCategory().equalsIgnoreCase("ACV")
							&& !userService.getAuthenticatedUser().hasRole("ROLE_SHIPCUST_SPARE"))) {
				redirectAttrs.addFlashAttribute("warning",
						messageSource.getMessage("shipcust.error.cantmodify", null, locale) + ": "
								+ line.getProductCategory() + " vs. USER_ROLE");
				return "redirect:/shipcust/show/line/" + line.getId();
			}

			ShipCustLineState stateLine = shipmentLineStatesService.findByOrder(stateOrder);
			if (stateLine == null) {
				throw new NotFoundException("Unknown custom shipment line state: " + stateOrder);
			}
			line.setState(stateLine);
			line.setSpareActionDate(new Timestamp((new java.util.Date()).getTime()));
			line.setSpareActionUserName(userService.getAuthenticatedUser().getName());
			shipmentLinesService.update(line);
			this.sendMail(line.getShipment().getId(), MAIL_LINE_SERVED, locale);
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		} else {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("shipcust.error.cantmodify", null, locale) + ": "
							+ line.getState().getTitle());
			return "redirect:/shipcust/show/line/" + line.getId();
		}

		return "redirect:/shipcust/dispatch";
	}

	@RequestMapping(value = "/ship/line/manage/{id}")
	@Transactional
	public String showLineManageForShipments(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs,
			Locale locale) throws NotFoundException {

		// get line
		CustomShipmentLine line = shipmentLinesService.findById(id);
		if (line == null) {
			throw new NotFoundException("Unknown shipment line: #" + id);
		}

		// return if not waiting or ready (from warehouse)
		if (line.getState().getOrder() != 10 && line.getState().getOrder() != 20 && line.getState().getOrder() != 30) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("shipcust.error.cantmodify", null, locale) + ": "
							+ line.getState().getTitle());
			return "redirect:/shipcust/show/line/" + line.getId();
		}

		FormShipmentLineManage formShipmentLineManage = new FormShipmentLineManage(line, line.getShipment().getId());
		model.addAttribute("formShipmentLineManage", formShipmentLineManage);

		return "shipcust/linemanage";
	}
	
	@RequestMapping(value = "/ship/order/manage/{id}")
	@Transactional
	public String showOrdereManageForShipments(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs,
			Locale locale) throws NotFoundException {
		
		// get shipment
		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		// get pending lines
		Map<Integer, CustomShipmentLine> linesMap = getPendingLines(shipment);
		
		// if empty list return to main with warning
		if(linesMap.isEmpty()) {
			redirectAttrs.addFlashAttribute("error",
					messageSource.getMessage("shipcust.warning.nothingtomanage", null, locale)) ;
			return "redirect:/shipcust/show/order/" + shipment.getId();
		}

		// add model variables
		model.addAttribute("sh", shipment);
		model.addAttribute("lines", linesMap.values());
		
		return "shipcust/ordermanage";
	}
	
	
	@RequestMapping(value = "/ship/order/confirm/{id}")
	@Transactional
	public String showOrderMassConfirm(@PathVariable("id") int id, Model model) throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		model.addAttribute("formShipmentMassConfirm", new FormShipmentMassConfirm(id));

		return "shipcust/ordermassconfirm";
	}
	
	@RequestMapping(value = "/ship/order/cancel/{id}")
	@Transactional
	public String showOrderMassCancel(@PathVariable("id") int id, Model model) throws NotFoundException {
		
		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}
		
		model.addAttribute("formShipmentMassCancel", new FormShipmentMassCancel(id));
		
		return "shipcust/ordermasscancel";
	}
	
	

	@RequestMapping(value = "/ship/line/manage", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String saveShipLineAction(@Valid FormShipmentLineManage formShipmentLineManage, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException, UnknownHostException, MessagingException {

		// validate
		if (bindingResult.hasErrors()) {
			return "shipcust/linemanage";
		}

		// get line
		CustomShipmentLine line = shipmentLinesService.findById(formShipmentLineManage.getLineId());
		if (line == null) {
			throw new NotFoundException("Unknown shipment line: #" + formShipmentLineManage.getLineId());
		}

		// get shipped line state
		ShipCustLineState state = shipmentLineStatesService.findByOrder(40);
		if (state == null) {
			throw new NotFoundException("Unknown shipment line state:" + 40);
		}

		line.setState(state);

		line.setShipmentActionDate(new Timestamp((new java.util.Date()).getTime()));
		line.setShipmentActionUserName(userService.getAuthenticatedUser().getName());
		line.setShipmentComment(formShipmentLineManage.getComment().trim());

		line.setQuantityShipped(formShipmentLineManage.getShipped());
		line.setWaybill(formShipmentLineManage.getWaybill().trim().toUpperCase());
		shipmentLinesService.update(line);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		// calculate shipment main state and give redirect + message
		return updateMainShipmentOrderState(line.getShipment(), redirectAttrs, locale);

	}
	
	@RequestMapping(value = "/ship/order/manage", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String saveShipOrderMassAction(@Valid FormShipmentMassConfirm formShipmentMassConfirm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model)
					throws NotFoundException, UnknownHostException, MessagingException {
		
		// validate
		if (bindingResult.hasErrors()) {
			return "shipcust/ordermassconfirm";
		}
		
		// get shipment order
		CustomShipment shipment = shipmentsService.findById(formShipmentMassConfirm.getOrderId());
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + formShipmentMassConfirm.getOrderId());
		}
		
		// get pending lines
		Map<Integer, CustomShipmentLine> linesMap = getPendingLines(shipment);

	
		// get shipped line state
		ShipCustLineState state = shipmentLineStatesService.findByOrder(40);
		if (state == null) {
			throw new NotFoundException("Unknown shipment line state:" + 40);
		}
		Timestamp updTime = new Timestamp((new java.util.Date()).getTime());
		String updUser = userService.getAuthenticatedUser().getName();
		
		for(CustomShipmentLine line: linesMap.values()) {
			line.setState(state);
			line.setShipmentActionDate(updTime);
			line.setShipmentActionUserName(updUser);
			line.setQuantityShipped(line.getQuantityDemanded());
			line.setShipmentComment(formShipmentMassConfirm.getComment().trim());
			line.setWaybill(formShipmentMassConfirm.getWaybill().trim().toUpperCase());
			shipmentLinesService.update(line);
		}
		
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		// calculate shipment main state and give redirect + message
		return updateMainShipmentOrderState(shipment, redirectAttrs, locale);
		
	}

	private Map<Integer, CustomShipmentLine> getPendingLines(CustomShipment shipment) {
		Map<Integer, CustomShipmentLine> linesMap = new TreeMap<>();
		for (CustomShipmentLine line : shipment.getLines()) {
			if(line.getState().getOrder() == 10 || line.getState().getOrder( )== 20 || line.getState().getOrder( )== 30 ) {
				linesMap.put(line.getId(), line);
			}
		}
		return linesMap;
	}

	@RequestMapping(value = "/ship/line/manage", params = { "cancel" }, method = RequestMethod.POST)
	@Transactional
	public String cancelShipLineAction(@Valid FormShipmentLineManage formShipmentLineManage,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException, UnknownHostException, MessagingException {

		// get line
		CustomShipmentLine line = shipmentLinesService.findById(formShipmentLineManage.getLineId());
		if (line == null) {
			throw new NotFoundException("Unknown shipment line: #" + formShipmentLineManage.getLineId());
		}

		// get shipped line state
		ShipCustLineState state = shipmentLineStatesService.findByOrder(50);
		if (state == null) {
			throw new NotFoundException("Unknown shipment line state:" + 50);
		}

		line.setState(state);

		line.setShipmentActionDate(new Timestamp((new java.util.Date()).getTime()));
		line.setShipmentActionUserName(userService.getAuthenticatedUser().getName());
		line.setShipmentComment(formShipmentLineManage.getComment().trim());

		line.setQuantityShipped(0);
		line.setWaybill("");
		shipmentLinesService.update(line);

		// calculate shipment main state and give redirect + message
		return updateMainShipmentOrderState(line.getShipment(), redirectAttrs, locale);

	}
	
	@RequestMapping(value = "/ship/order/manage", params = { "cancel" }, method = RequestMethod.POST)
	@Transactional
	public String cancelShipOrderMassAction(@Valid FormShipmentMassCancel formShipmentMassCancel,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException, UnknownHostException, MessagingException {

		// validate
		if (bindingResult.hasErrors()) {
			return "shipcust/ordermasscancel";
		}
		
		// get shipment order
		CustomShipment shipment = shipmentsService.findById(formShipmentMassCancel.getOrderId());
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + formShipmentMassCancel.getOrderId());
		}
		
		// get pending lines
		Map<Integer, CustomShipmentLine> linesMap = getPendingLines(shipment);

	
		// get shipped line state
		ShipCustLineState state = shipmentLineStatesService.findByOrder(50);
		if (state == null) {
			throw new NotFoundException("Unknown shipment line state:" + 50);
		}
		Timestamp updTime = new Timestamp((new java.util.Date()).getTime());
		String updUser = userService.getAuthenticatedUser().getName();
		
		for(CustomShipmentLine line: linesMap.values()) {
			line.setState(state);
			line.setShipmentActionDate(updTime);
			line.setShipmentActionUserName(updUser);
			line.setShipmentComment(formShipmentMassCancel.getComment().trim());
			line.setQuantityShipped(0);
			line.setWaybill("");
			shipmentLinesService.update(line);
		}
		
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		// calculate shipment main state and give redirect + message
		return updateMainShipmentOrderState(shipment, redirectAttrs, locale);

	}

	private String updateMainShipmentOrderState(CustomShipment shipment, RedirectAttributes redirectAttrs,
			Locale locale) throws UnknownHostException, MessagingException {
		boolean finished = true;
		for (CustomShipmentLine line : shipment.getLines()) {
			if (line.getState().getOrder() < 40) {
				finished = false;
				break;
			}
		}

		Timestamp date = new Timestamp((new java.util.Date()).getTime());
		ShipCustState state;

		if (finished) {
			// closed state
			state = shipmentStatesService.findByOrder(30);
			shipment.setState(state);
			shipment.setUpdateDate(date);
			shipment.setCloseDate(date);
			shipmentsService.update(shipment);
			this.sendMail(shipment.getId(), MAIL_ORDER_CLOSED, locale);
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("shipcust.action.finished", null, locale));
			return "redirect:/shipcust/show/order/" + shipment.getId();
		} else {
			// partial state
			state = shipmentStatesService.findByOrder(20);
			shipment.setState(state);
			shipment.setUpdateDate(date);
			shipmentsService.update(shipment);
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
			return "redirect:/shipcust/show/order/" + shipment.getId();
		}

	}

	private void sendMail(int shipOrLineId, int type, Locale locale) throws UnknownHostException, MessagingException {

		Context context = new Context();
		List<User> users = new ArrayList<>();
		String title;

		switch (type) {
		case MAIL_NEW_ORDER:
			context.setVariable("shipmentId", shipOrLineId);
			users = userService.findByAnyRole(new String[] { "ROLE_SHIPCUST_SHIP" });
			title = messageSource.getMessage("shipcust.mail.title.neworder", null, locale);
			break;
		case MAIL_NEW_SPARE_LINE:
			context.setVariable("lineId", shipOrLineId);
			users = userService.findByAnyRole(new String[] { "ROLE_SHIPCUST_SPARE" });
			title = messageSource.getMessage("shipcust.mail.title.newspare", null, locale);
			break;
		case MAIL_NEW_ACQ_LINE:
			context.setVariable("lineId", shipOrLineId);
			users = userService.findByAnyRole(new String[] { "ROLE_SHIPCUST_ACQ" });
			title = messageSource.getMessage("shipcust.mail.title.newacq", null, locale);
			break;
		case MAIL_LINE_SERVED:
			context.setVariable("shipmentId", shipOrLineId);
			users = userService.findByAnyRole(new String[] { "ROLE_SHIPCUST_SHIP" });
			title = messageSource.getMessage("shipcust.mail.title.lineserved", null, locale);
			break;
		case MAIL_ORDER_CLOSED:
			context.setVariable("shipmentId", shipOrLineId);
			users = userService.findByAnyRole(new String[] { "ROLE_SHIPCUST_SALES" });
			title = messageSource.getMessage("shipcust.mail.title.orderclosed", null, locale);
			break;
		default:
			title = messageSource.getMessage("shipcust.order", null, locale);
			break;
		}

		ArrayList<String> mailTo = new ArrayList<>();
		for (User user : users) {
			mailTo.add(user.getEmail());
		}

		// context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("host", InetAddress.getLocalHost().getHostName());
		context.setVariable("title", title);
		context.setVariable("message", messageSource.getMessage("shipcust.mail.message", null, locale));

		String body = templateEngine.process("shipcust/mailtemplate", context);
		if (mailTo.size() > 0) {
			mailService.sendEmail("webapp@atwsystem.pl", mailTo.toArray(new String[0]), new String[0],
					messageSource.getMessage("shipcust.title", null, locale) + " - " + title, body);
		}
	}

}

package sbs.controller.shipcust;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

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
		List<CustomShipmentLine> lines = shipmentLinesService.findAllPendingSpare(CustomShipmentLinesService.SPARE_PROD);
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
			throws NotFoundException {

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

		shipmentsService.save(ship);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/shipcust/show/order/" + ship.getId();
	}

	@RequestMapping(value = "/sales/order/cancel/{id}")
	@Transactional
	public String cancelOrder(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		ShipCustLineState stateLine = shipmentLineStatesService.findByOrder(60);
		if (stateLine == null) {
			throw new NotFoundException("Unknown custom shipment line state: " + 60);
		}

		ShipCustState stateOrder = shipmentStatesService.findByOrder(40);
		if (stateOrder == null) {
			throw new NotFoundException("Unknown custom shipment state: " + 40);
		}

		shipment.setState(stateOrder);
		for (CustomShipmentLine line : shipment.getLines()) {
			if (line.getState().getOrder() < 40) {
				line.setState(stateLine);
				shipmentLinesService.update(line);
			}
		}
		shipmentsService.update(shipment);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.cancelled", null, locale) 
				+ ": #" + shipment.getId() + "[" + shipment.getClientName() + "]");
		return "redirect:/shipcust/sales/transport/manage";
	}

	@RequestMapping(value = "/show/order/{id}")
	@Transactional
	public String showOrder(@PathVariable("id") int id, Model model) throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		model.addAttribute("sh", shipment);
		Hibernate.initialize(shipment.getLines());
		model.addAttribute("lines", shipment.getLines());

		return "shipcust/shipmentshow";
	}

	@RequestMapping(value = "/sales/line/create/{id}")
	public String showCreateLine(@PathVariable("id") int id, Model model) throws NotFoundException {

		CustomShipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException("Unknown shipment order: #" + id);
		}

		FormCreateCustomShipmentLine formCreateCustomShipmentLine = new FormCreateCustomShipmentLine(shipment);
		model.addAttribute("formCreateCustomShipmentLine", formCreateCustomShipmentLine);

		return "shipcust/linecreate";
	}

	@RequestMapping(value = "/sales/line/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String saveShipmentLine(@Valid FormCreateCustomShipmentLine formCreateCustomShipmentLine,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException {

		// VALIDATE
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
	
}

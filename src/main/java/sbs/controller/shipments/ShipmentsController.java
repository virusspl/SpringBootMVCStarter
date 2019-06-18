package sbs.controller.shipments;

import java.sql.Timestamp;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.controller.qcheck.ResponseForm;
import sbs.model.shipments.Shipment;
import sbs.model.shipments.ShipmentState;
import sbs.model.users.User;
import sbs.model.x3.X3Client;
import sbs.service.shipments.ShipmentLinesService;
import sbs.service.shipments.ShipmentStatesService;
import sbs.service.shipments.ShipmentsService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;


@Controller
public class ShipmentsController {

	@Autowired
	UserService userService;
	@Autowired
	ShipmentsService shipmentsService;
	@Autowired
	ShipmentLinesService shipmentLinesService;
	@Autowired
	ShipmentStatesService shipmentStatesService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;

	public ShipmentsController() {

	}

	@RequestMapping("/shipments/dispatch")
	public String dispatch() {
		return "shipments/dispatch";
	}

	@RequestMapping("/shipments/list/{company}")
	public String current(@PathVariable("company") String company, Model model, Locale locale) {
		model.addAttribute("title", messageSource.getMessage("shipments", null, locale) + " - "
				+ messageSource.getMessage("general.pending", null, locale) + " - " + company);
		model.addAttribute("list", shipmentsService.findPending(company));
		return "shipments/list";
	}

	@RequestMapping("/shipments/archive/{company}")
	public String archive(@PathVariable("company") String company, Model model, Locale locale) {
		model.addAttribute("title", messageSource.getMessage("shipments", null, locale) + " - "
				+ messageSource.getMessage("general.archive", null, locale) + " - " + company);
		model.addAttribute("list", shipmentsService.findShipped(company));
		return "shipments/list";
	}

	@RequestMapping(value = "/shipments/create")
	public String create(Model model) {
		model.addAttribute("shipmentCreateForm", new ShipmentCreateForm("WPS", new java.util.Date()));
		System.out.println("wtf?");
		return "shipments/create";
	}

	@RequestMapping(value = "/shipments/create", method = RequestMethod.POST)
	@Transactional
	public String createShipment(@Valid ShipmentCreateForm shipmentCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			return "shipments/create";
		}

		X3Client x3client = x3Service.findClientByCode(shipmentCreateForm.getCompany(), shipmentCreateForm.getClient());
		if (x3client == null) {
			bindingResult.rejectValue("client", "error.client.not.found", "ERROR");
			return "shipments/create";
		}

		// create object
		Shipment shipment = new Shipment();

		// creator
		User creator = userService.getAuthenticatedUser();
		shipment.setCreator(creator);
		// state
		ShipmentState stateNew = shipmentStatesService.findByOrder(10);
		shipment.setState(stateNew);
		// creation date
		Timestamp credat = new Timestamp(new java.util.Date().getTime());
		shipment.setCreationDate(credat);
		// preparation date
		shipment.setPreparationDate(new Timestamp(shipmentCreateForm.getDate().getTime()));
		// update date
		shipment.setUpdateDate(credat);
		// company
		shipment.setCompany(shipmentCreateForm.getCompany());
		// CLIENT DATA
		shipment.setClientCode(x3client.getCode());
		shipment.setClientName(x3client.getName());
		shipment.setClientCountry(x3client.getCountry());
		// description
		shipment.setDescription(shipmentCreateForm.getDescription());

		// save
		shipmentsService.save(shipment);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/shipments/show/" + shipment.getId();

	}

	@RequestMapping(value = "/shipments/show/{id}")
	@Transactional
	public String show(@PathVariable("id") int id, ResponseForm responseForm, Model model, Locale locale)
			throws NotFoundException {

		Shipment shipment = shipmentsService.findById(id);
		if (shipment == null) {
			throw new NotFoundException(messageSource.getMessage("general.notfound", null, locale) + " #" + id);
		}
		model.addAttribute("shipment", shipment);
		model.addAttribute("lines", shipmentLinesService.findLinesByShipmentId(shipment.getId()));

		return "shipments/show";
	}

	@RequestMapping(value = "/shipments/action", params = { "completed" }, method = RequestMethod.POST)
	@Transactional
	public String setCompleted(@RequestParam String completed, RedirectAttributes redirectAttrs, Locale locale,
			Model model) throws NotFoundException {
		Shipment shipment = shipmentsService.findById(Integer.parseInt(completed));
		if (shipment == null) {
			throw new NotFoundException("Shipment not found");
		}
		ShipmentState state = shipmentStatesService.findByOrder(30);
		shipment.setState(state);
		shipmentsService.update(shipment);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage(state.getCode(), null, locale));
		return "redirect:/shipments/show/" + shipment.getId();
	}

	@RequestMapping(value = "/shipments/action", params = { "shipped" }, method = RequestMethod.POST)
	@Transactional
	public String setShipped(@RequestParam String shipped, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException {
		Shipment shipment = shipmentsService.findById(Integer.parseInt(shipped));
		if (shipment == null) {
			throw new NotFoundException("Shipment not found");
		}
		ShipmentState state = shipmentStatesService.findByOrder(40);
		shipment.setState(state);
		shipmentsService.update(shipment);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage(state.getCode(), null, locale));
		return "redirect:/shipments/show/" + shipment.getId();
	}

	@RequestMapping(value = "/shipments/action", params = { "cancelled" }, method = RequestMethod.POST)
	@Transactional
	public String setCancelled(@RequestParam String cancelled, RedirectAttributes redirectAttrs, Locale locale,
			Model model) throws NotFoundException {
		Shipment shipment = shipmentsService.findById(Integer.parseInt(cancelled));
		if (shipment == null) {
			throw new NotFoundException("Shipment not found");
		}
		ShipmentState state = shipmentStatesService.findByOrder(90);
		shipment.setState(state);
		shipmentsService.update(shipment);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage(state.getCode(), null, locale));
		return "redirect:/shipments/show/" + shipment.getId();
	}

	@RequestMapping(value = "/terminal/shipments")
	public String terminalView(Model model, Locale locale) {
		ShipmentTerminalForm shipmentTerminalForm = new ShipmentTerminalForm();
		shipmentTerminalForm
				.setCurrentColumnName(messageSource.getMessage("shipments.terminal.choose.shipment", null, locale));
		model.addAttribute("shipmentTerminalForm", shipmentTerminalForm);
		return "shipments/shipments_terminal";
	}

	@RequestMapping(value = "/shipments/processentry")
	public String terminalView(ShipmentTerminalForm shipmentTerminalForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		ShipmentTerminalForm sf = shipmentTerminalForm;
		System.out.println(sf);
		
		switch (shipmentTerminalForm.getCurrentStep()) {
		case ShipmentTerminalForm.STEP_CHOOSE_SHIPMENT:
			Shipment shipment = shipmentsService.findById(Integer.parseInt(sf.getCurrentValue()));
			sf.setCurrentShipment(shipment.getId());
			sf.setCurrentStep(sf.getStepChooseAction());

			model.addAttribute("msg",
					shipment.getClientName() + "(" + shipment.getClientCode() + ")" );
			break;

		}

		return "shipments/shipments_terminal";
	}

}

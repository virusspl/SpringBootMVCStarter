package sbs.controller.shipments;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.service.shipments.ShipmentLinesService;
import sbs.service.shipments.ShipmentStatesService;
import sbs.service.shipments.ShipmentsService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("/shipments")
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
	JdbcOracleX3Service jdbcOracleX3Service;
	

	public ShipmentsController() {
	
	}

	@RequestMapping("/dispatch")
	public String dispatch() {
		return "shipments/dispatch";
	}
	
	@RequestMapping("/list/{company}")
	public String current(@PathVariable("company") String company, Model model) {
		System.out.println("list "+company);
		model.addAttribute("list", shipmentsService.findPending(company));
		return "shipments/list";
	}
	
	@RequestMapping("/archive/{company}")
	public String archive(@PathVariable("company") String company, Model model) {
		System.out.println("archive "+company);
		model.addAttribute("list", shipmentsService.findShipped(company));
		return "shipments/list";
	}

	@RequestMapping(value = "/create")
	@Transactional
	public String create(Model model) {
		model.addAttribute("shipmentCreateForm", new ShipmentCreateForm());
		return "shipments/create";
	}


}

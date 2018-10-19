package sbs.controller.inventory;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.controller.bhptickets.TicketCreateForm;
import sbs.controller.buyorders.ResponseForm;
import sbs.helpers.TextHelper;
import sbs.model.bhptickets.BhpTicket;
import sbs.model.bhptickets.BhpTicketState;
import sbs.model.buyorders.BuyOrder;
import sbs.model.inventory.Inventory;
import sbs.model.users.User;
import sbs.service.inventory.InventoryService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("inventory")
public class InventoryController {
	
	@Autowired
	UserService userService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;
	@Autowired
	InventoryService inventoryService;

	@RequestMapping(value = "/list")
	public String list(Model model) {
		model.addAttribute("inventories", inventoryService.findAllActiveInventories());
		return "inventory/list";
	}
	
	@RequestMapping(value = "/inactivelist")
	public String inactivelist(Model model) {
		model.addAttribute("inventories", inventoryService.findAllInactiveInventories());
		return "inventory/list";
	}
	
	@RequestMapping(value = "/createinventory")
	@Transactional
	public String createInventoryView(Model model) {
		model.addAttribute("inventoryCreateForm", new InventoryCreateForm());
		return "inventory/createinventory";
	}
	
	@RequestMapping(value = "/createinventory", method = RequestMethod.POST)
	@Transactional
	public String createInventory(@Valid InventoryCreateForm inventoryCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			return "inventory/createinventory";
		}

		// create project object
		Inventory inventory = new Inventory();

		// auto fields
		User creator = userService.getAuthenticatedUser();
		creator.getCreatedInventories().add(inventory);
		inventory.setCreator(creator);
		
		Timestamp credat = new Timestamp(new java.util.Date().getTime());
		inventory.setCreationDate(credat);
		
		inventory.setActive(false);
		inventory.setNextLine(1);
		
		// form fields
		inventory.setTitle(inventoryCreateForm.getTitle());
		inventory.setDescription(inventoryCreateForm.getDescription());
		inventory.setInventoryDate(new Timestamp(inventoryCreateForm.getInventoryDate().getTime()));

		// save
		inventoryService.save(inventory);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/inventory/editinventory/" + inventory.getId();
	}	
	
	@RequestMapping("/editinventory/{id}")
	@Transactional
	public String editInventoryView(@PathVariable("id") int id, Model model) throws NotFoundException {
		Inventory inventory = inventoryService.findById(id);
		if (inventory == null) {
			throw new NotFoundException("Inventory not found");
		}
		InventoryCreateForm inventoryCreateForm = new InventoryCreateForm();
		inventoryCreateForm.setId(inventory.getId());
		inventoryCreateForm.setActive(inventory.getActive());
		inventoryCreateForm.setTitle(inventory.getTitle());
		inventoryCreateForm.setDescription(inventory.getDescription());
		inventoryCreateForm.setInventoryDate(inventory.getInventoryDate());
		inventoryCreateForm.setNextLine(inventory.getNextLine());
		
		model.addAttribute("inventoryCreateForm", inventoryCreateForm);
		model.addAttribute("columns", inventory.getColumns());
		
		return "inventory/editinventory";
	}
	
	@RequestMapping(value = "/editinventory", method = RequestMethod.POST)
	@Transactional
	public String editInventory(@Valid InventoryCreateForm inventoryCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		
		System.out.println("return:" + inventoryCreateForm);
		
		Inventory inventory = inventoryService.findById(inventoryCreateForm.getId());
		if (inventory == null) {
			throw new NotFoundException("Inventory not found");
		}
		
		// validate
		if (bindingResult.hasErrors()) {
			return "inventory/editinventory";
		}
	
		inventory.setActive(inventoryCreateForm.getActive());
		inventory.setTitle(inventoryCreateForm.getTitle());
		inventory.setDescription(inventoryCreateForm.getDescription());
		System.out.println("date " + inventoryCreateForm.getInventoryDate());
		System.out.println("DATE: " + new Timestamp(inventoryCreateForm.getInventoryDate().getTime()));
		inventory.setInventoryDate(new Timestamp(inventoryCreateForm.getInventoryDate().getTime()));
		
		System.out.println("UPDATING: " + inventory.getId());
		inventoryService.update(inventory);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		
		return "redirect:/inventory/editinventory/"+inventory.getId();
	}


}

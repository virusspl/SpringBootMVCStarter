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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.helpers.TextHelper;
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


}

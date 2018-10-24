package sbs.controller.inventory;

import java.sql.Timestamp;
import java.util.List;
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
import sbs.helpers.TextHelper;
import sbs.model.inventory.Inventory;
import sbs.model.inventory.InventoryColumn;
import sbs.model.inventory.InventoryDataType;
import sbs.model.users.User;
import sbs.service.inventory.InventoryColumnsService;
import sbs.service.inventory.InventoryDataTypesService;
import sbs.service.inventory.InventoryService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
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
	@Autowired
	InventoryDataTypesService inventoryDataTypesService;
	@Autowired
	InventoryColumnsService inventoryColumnsService;

	@RequestMapping(value = "/inventory/list")
	public String list(Model model) {
		model.addAttribute("inventories", inventoryService.findAllActiveInventories());
		return "inventory/list";
	}

	@RequestMapping(value = "/inventory/inactivelist")
	public String inactivelist(Model model) {
		model.addAttribute("inventories", inventoryService.findAllInactiveInventories());
		return "inventory/list";
	}

	@RequestMapping(value = "/inventory/createinventory")
	@Transactional
	public String createInventoryView(Model model) {
		model.addAttribute("inventoryCreateForm", new InventoryCreateForm());
		return "inventory/createinventory";
	}

	@RequestMapping(value = "/inventory/createinventory", method = RequestMethod.POST)
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

	@RequestMapping("/inventory/editinventory/{id}")
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

	@RequestMapping(value = "/inventory/editinventory", method = RequestMethod.POST)
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

		return "redirect:/inventory/editinventory/" + inventory.getId();
	}

	@RequestMapping("/inventory/createcolumn/{id}")
	@Transactional
	public String createColumnView(@PathVariable("id") int id, Model model) throws NotFoundException {
		Inventory inventory = inventoryService.findById(id);
		if (inventory == null) {
			throw new NotFoundException("Inventory not found");
		}
		InventoryColumnCreateForm inventoryColumnCreateForm = new InventoryColumnCreateForm();
		inventoryColumnCreateForm.setInventoryId(inventory.getId());

		model.addAttribute("inventoryColumnCreateForm", inventoryColumnCreateForm);
		model.addAttribute("types", inventoryDataTypesService.findAll());

		return "inventory/createcolumn";
	}

	@RequestMapping(value = "/inventory/createcolumn", method = RequestMethod.POST)
	@Transactional
	public String createColumn(@Valid InventoryColumnCreateForm inventoryColumnCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		// validate
		if (bindingResult.hasErrors()) {
			model.addAttribute("types", inventoryDataTypesService.findAll());
			return "inventory/createcolumn";
		}

		Inventory inventory = inventoryService.findById(inventoryColumnCreateForm.getInventoryId());
		InventoryDataType idt = inventoryDataTypesService.findById(inventoryColumnCreateForm.getTypeId());
		InventoryColumn column = inventoryColumnsService.findByInventoryAndDataType(inventory.getId(), idt.getId());
		if (column != null) {
			bindingResult.rejectValue("typeId", "inventory.error.columnused", "ERROR");
			model.addAttribute("types", inventoryDataTypesService.findAll());
			return "inventory/createcolumn";
		}

		column = new InventoryColumn();
		column.setInventory(inventory);
		inventory.getColumns().add(column);
		column.setInventoryDataType(idt);
		column.setOrder(inventoryColumnCreateForm.getOrder());
		column.setRequired(inventoryColumnCreateForm.isRequired());
		column.setValidated(inventoryColumnCreateForm.isValidated());
		column.setColumnName(inventoryColumnCreateForm.getName());
		inventoryColumnsService.save(column);

		redirectAttrs.addFlashAttribute("msg",
				messageSource.getMessage("action.saved", null, locale) + ": " + column.getColumnName());
		return "redirect:/inventory/editinventory/" + inventoryColumnCreateForm.getInventoryId();
	}

	@RequestMapping("/inventory/deletecolumn/{inventoryId}/{columnId}")
	@Transactional
	public String deleteColumnView(@PathVariable("inventoryId") int inventoryId, @PathVariable("columnId") int columnId,
			RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException {
		InventoryColumn column = inventoryColumnsService.findById(columnId);
		if (column != null) {
			inventoryColumnsService.remove(column);
			redirectAttrs.addFlashAttribute("msg",
					messageSource.getMessage("action.removed", null, locale) + ": " + column.getColumnName());
			return "redirect:/inventory/editinventory/" + inventoryId;
		} else {
			throw new NotFoundException("Column not found");
		}
	}

	@RequestMapping(value = "/terminal/inventory")
	public String terminalView(Model model, Locale locale) {
		InventoryTerminalForm inventoryTerminalForm = new InventoryTerminalForm();
		inventoryTerminalForm.setCurrentColumnNumber(-1);
		inventoryTerminalForm.setCurrentColumnCode("inventory.type.inventoryid");
		inventoryTerminalForm.setCurrentColumnName(messageSource.getMessage("inventory.type.inventoryid", null, locale));
		model.addAttribute("inventoryTerminalForm", inventoryTerminalForm);
		return "inventory/inventory_terminal";
	}

	@RequestMapping(value = "/inventory/processentry", method = RequestMethod.POST)
	@Transactional
	public String terminalProcessInventoryEntry(InventoryTerminalForm inventoryTerminalForm,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException {

		/*
		inventory.type.code
		inventory.type.address
		inventory.type.location
		inventory.type.label
		inventory.type.order.sale
		inventory.type.order.purchase
		inventory.type.packagetype
		inventory.type.quantity
		inventory.type.freestring1
		inventory.type.freestring2
		inventory.type.freedouble
		*/

		try {
			/* ============================ INVENTORY ID =========================== */
			if (inventoryTerminalForm.getCurrentColumnCode().equals("inventory.type.inventoryid")) {
				// check number format
			
					int inventoryId = Integer.parseInt(inventoryTerminalForm.getCurrentValue());
					// find inventory
					Inventory inventory = inventoryService.findById(inventoryId);
					if (inventory == null) {
						bindingResult.rejectValue("currentValue", "inventory.error.inventorynotfound");
						// no inventory found
						return "inventory/inventory_terminal";
					}
					else if(!inventory.getActive()){
						bindingResult.rejectValue("currentValue", "inventory.error.inventoryinactive");
						// inventory inactive
						return "inventory/inventory_terminal";						
					}
					// inventory found and active!
					// get columns
					List<InventoryColumn> columns = inventoryColumnsService.findInventoryColumnsSortByOrder(inventory.getId());
					if (columns.isEmpty()){
						redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("inventory.error.nocolumnsdefined", null, locale));
						// no columns found	
						return "redirect:/terminal/inventory";
					}

					// save value and reset field
					inventoryTerminalForm.setInventoryId(inventoryId);
					// switch to next column
					switchToNextColumn(inventoryTerminalForm, columns);
					// show inventory title
					model.addAttribute("msg", inventory.getTitle());
					return "inventory/inventory_terminal";
			}
			
			/* ============================ CODE =========================== */
			if (inventoryTerminalForm.getCurrentColumnCode().equals("inventory.type.code")) {
				
				if(inventoryTerminalForm.getCurrentValue().length() == 0){
					// empty
					if(inventoryTerminalForm.isCurrentColumnRequired()){
						// required error
						bindingResult.rejectValue("currentValue", "inventory.error.fieldrequired");
						return "inventory/inventory_terminal";
					}
					else{
						// empty ok
						// switch to next column
						switchToNextColumn(inventoryTerminalForm);
							model.addAttribute("warning", messageSource.getMessage("inventory.warning.fieldskipped", null, locale));
							return "inventory/inventory_terminal";
					}
				}
				else {
					//not empty
					
				}
				
				
				// save value and reset field
				inventoryTerminalForm.setCode(inventoryTerminalForm.getCurrentValue().trim().toUpperCase());

				// switch to next column
				switchToNextColumn(inventoryTerminalForm);
					model.addAttribute("msg", "NEXT LINE!! " + inventoryTerminalForm.getCode());
					return "inventory/inventory_terminal";
			
			}
			
		} catch (EndOfColumnsException eoc){
			System.out.println("END OF COLUMNS " + inventoryTerminalForm.toString());
			model.addAttribute("live", "end of columns!");
			//String summary = generateSummary(inventoryTerminalForm());
		} catch (NumberFormatException nfe) {
			bindingResult.rejectValue("currentValue", "inventory.error.mustbeanumber");
			// bad number format
			return "inventory/inventory_terminal";	
		} catch (Exception e) {
			// catch any other exceptions
			model.addAttribute("error", e.getMessage());
			return "inventory/inventory_terminal";
		}
		return "redirect:/terminal/inventory";
	}

	private void switchToNextColumn(InventoryTerminalForm inventoryTerminalForm, List<InventoryColumn> columns) throws EndOfColumnsException{
		int number = inventoryTerminalForm.getCurrentColumnNumber()+1;
		if(number >= columns.size()){
			throw new EndOfColumnsException();
		}
		inventoryTerminalForm.setCurrentColumnNumber(number);
		inventoryTerminalForm.setCurrentColumnCode(columns.get(number).getInventoryDataType().getColumnTypeCode());
		inventoryTerminalForm.setCurrentColumnName(columns.get(number).getColumnName());
		inventoryTerminalForm.setCurrentColumnRequired(columns.get(number).getRequired());
		inventoryTerminalForm.setCurrentColumnValidated(columns.get(number).getValidated());
		inventoryTerminalForm.setCurrentValue("");
	}
	
	private void switchToNextColumn(InventoryTerminalForm inventoryTerminalForm) throws EndOfColumnsException {
		List<InventoryColumn> columns = inventoryColumnsService.findInventoryColumnsSortByOrder(inventoryTerminalForm.getInventoryId());
		int number = inventoryTerminalForm.getCurrentColumnNumber()+1;
		if(number >= columns.size()){
			throw new EndOfColumnsException();
		}
		inventoryTerminalForm.setCurrentColumnNumber(number);
		inventoryTerminalForm.setCurrentColumnCode(columns.get(number).getInventoryDataType().getColumnTypeCode());
		inventoryTerminalForm.setCurrentColumnName(columns.get(number).getColumnName());
		inventoryTerminalForm.setCurrentColumnRequired(columns.get(number).getRequired());
		inventoryTerminalForm.setCurrentColumnValidated(columns.get(number).getValidated());
		inventoryTerminalForm.setCurrentValue("");
	}

}

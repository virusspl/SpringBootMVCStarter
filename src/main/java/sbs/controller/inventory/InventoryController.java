package sbs.controller.inventory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import sbs.helpers.DateHelper;
import sbs.helpers.TextHelper;
import sbs.model.inventory.Inventory;
import sbs.model.inventory.InventoryColumn;
import sbs.model.inventory.InventoryDataType;
import sbs.model.inventory.InventoryEntry;
import sbs.model.users.User;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3PurchaseOrder;
import sbs.model.x3.X3SalesOrder;
import sbs.model.x3.X3StoreInfo;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.inventory.InventoryColumnsService;
import sbs.service.inventory.InventoryDataTypesService;
import sbs.service.inventory.InventoryEntriesService;
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
	JdbcOracleGeodeService geodeService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;
	@Autowired
	InventoryService inventoryService;
	@Autowired
	InventoryDataTypesService inventoryDataTypesService;
	@Autowired
	InventoryColumnsService inventoryColumnsService;
	@Autowired
	InventoryEntriesService inventoryEntriesService;

	String tmpMsg;
	String tmpWarning;

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
		inventory.setCompany(inventoryCreateForm.getCompany());
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
		inventoryCreateForm.setCompany(inventory.getCompany());

		model.addAttribute("inventoryCreateForm", inventoryCreateForm);
		model.addAttribute("columns", inventory.getColumns());

		return "inventory/editinventory";
	}

	@RequestMapping(value = "/inventory/editinventory", method = RequestMethod.POST)
	@Transactional
	public String editInventory(@Valid InventoryCreateForm inventoryCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		Inventory inventory = inventoryService.findById(inventoryCreateForm.getId());
		if (inventory == null) {
			throw new NotFoundException("Inventory not found");
		}

		// validate
		if (bindingResult.hasErrors()) {
			return "inventory/editinventory";
		}

		inventory.setActive(inventoryCreateForm.getActive());
		inventory.setCompany(inventoryCreateForm.getCompany());
		inventory.setTitle(inventoryCreateForm.getTitle());
		inventory.setDescription(inventoryCreateForm.getDescription());
		inventory.setInventoryDate(new Timestamp(inventoryCreateForm.getInventoryDate().getTime()));

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
		initTerminalForm(inventoryTerminalForm, locale, false);
		model.addAttribute("inventoryTerminalForm", inventoryTerminalForm);
		return "inventory/inventory_terminal";
	}

	private void initTerminalForm(InventoryTerminalForm form, Locale locale, boolean restoreInventoryId) {
		// first column - inventory ID
		form.setCurrentColumnNumber(-1);
		form.setCurrentColumnCode("inventory.type.inventoryid");
		form.setCurrentColumnName(messageSource.getMessage("inventory.type.inventoryid", null, locale));
		if(restoreInventoryId){
			form.setCurrentValue(form.getInventoryId()+"");
		}
		else{
			form.setCurrentValue("");
		}
		// fields empty for new entry
		form.setInventoryId(0);
		form.setCompany("");
		form.setCode("");
		form.setAddress("");
		form.setLocation("");
		form.setLabel("");
		form.setSaleOrder("");
		form.setPurchaseOrder("");
		form.setPackageType("");
		form.setQuantity(0.0);
		form.setFreeString1("");
		form.setFreeString2("");
		form.setFreeDouble(0.0);
		form.setReadyToSave(false);
		
	}

	@RequestMapping(value = "/inventory/processentry", params = { "step" }, method = RequestMethod.POST)
	@Transactional
	public String terminalProcessInventoryEntry(InventoryTerminalForm inventoryTerminalForm,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException {

		try {
			/*
			 * ================== INVENTORY ID ===============
			 */
			if (inventoryTerminalForm.getCurrentColumnCode().equals("inventory.type.inventoryid")) {
				// get inventory
				int inventoryId = Integer.parseInt(inventoryTerminalForm.getCurrentValue());
				Inventory inventory = inventoryService.findById(inventoryId);
				if (inventory == null) {
					bindingResult.rejectValue("currentValue", "inventory.error.inventorynotfound");
					return "inventory/inventory_terminal";
				} else if (!inventory.getActive()) {
					bindingResult.rejectValue("currentValue", "inventory.error.inventoryinactive");
					return "inventory/inventory_terminal";
				}

				// get columns
				List<InventoryColumn> columns = inventoryColumnsService
						.findInventoryColumnsSortByOrder(inventory.getId());
				if (columns.isEmpty()) {
					redirectAttrs.addFlashAttribute("warning",
							messageSource.getMessage("inventory.error.nocolumnsdefined", null, locale));
					return "redirect:/terminal/inventory";
				}

				// set inventory id and switch column
				inventoryTerminalForm.setInventoryId(inventory.getId());
				inventoryTerminalForm.setCompany(inventory.getCompany());
				switchToNextColumn(inventoryTerminalForm, columns);
				model.addAttribute("msg", inventory.getTitle());

				return "inventory/inventory_terminal";
			}

			/*
			 * ================== MANAGE EMPTY =====================
			 */
			if (inventoryTerminalForm.getCurrentValue().length() == 0) {
				if (inventoryTerminalForm.isCurrentColumnRequired()) {
					// return required error
					bindingResult.rejectValue("currentValue", "inventory.error.fieldrequired");
					return "inventory/inventory_terminal";
				} else {
					// switch to next column / empty OK
					switchToNextColumn(inventoryTerminalForm);
					model.addAttribute("warning",
							messageSource.getMessage("inventory.warning.fieldskipped", null, locale));
					return "inventory/inventory_terminal";
				}
			}
			/*
			 * ================== MANAGE VALIDATION ================
			 */
			String errorMessageCode = validateCurrentField(inventoryTerminalForm);

			if (errorMessageCode != null) {
				setValidationMessages(model);
				bindingResult.rejectValue("currentValue", errorMessageCode);
				return "inventory/inventory_terminal";
			} else {
				setValidationMessages(model);
				saveCurrentValueInForm(inventoryTerminalForm);
				switchToNextColumn(inventoryTerminalForm);
				return "inventory/inventory_terminal";
			}
		} catch (EndOfColumnsException eoc) {
			inventoryTerminalForm.setReadyToSave(true);
			return "inventory/inventory_terminal";
			// String summary = generateSummary(inventoryTerminalForm());
		} catch (NumberFormatException nfe) {
			bindingResult.rejectValue("currentValue", "inventory.error.mustbeanumber");
			return "inventory/inventory_terminal";
		} catch (UnknownDataTypeException udt) {
			bindingResult.rejectValue("currentValue", "inventory.error.unknowndatatype");
			return "inventory/inventory_terminal";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "inventory/inventory_terminal";
		}
	}
	
	@RequestMapping(value = "/inventory/save", method = RequestMethod.POST)
	@Transactional
	public String saveTerminalProcessInventoryEntry(InventoryTerminalForm inventoryTerminalForm,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException {
		
		saveInventoryEntryFromForm(inventoryTerminalForm);
		initTerminalForm(inventoryTerminalForm, locale, true);
		
		model.addAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		
		return "inventory/inventory_terminal";
	}
	
	@RequestMapping(value = "/inventory/cancel", method = RequestMethod.POST)
	@Transactional
	public String cancelTerminalProcessInventoryEntry(InventoryTerminalForm inventoryTerminalForm,
			BindingResult bindingResult, RedirectAttributes redirectAttrs, Locale locale, Model model)
			throws NotFoundException {
		
		initTerminalForm(inventoryTerminalForm, locale, true);
		model.addAttribute("warning", messageSource.getMessage("action.cancelled", null, locale));
		return "inventory/inventory_terminal";
	}
	
	@Transactional
	private void saveInventoryEntryFromForm(InventoryTerminalForm form) {
		
		Inventory inventory = inventoryService.findById(form.getInventoryId());
		User user = userService.getAuthenticatedUser();
		
		InventoryEntry entry = new InventoryEntry();
		
		entry.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		entry.setInventory(inventory);
		inventory.getEntries().add(entry);
		entry.setLine(inventory.getNextLine());
		inventory.setNextLine(entry.getLine()+1);
		entry.setUserCode(user.getUsername());
		entry.setUserName(user.getName());
		
		entry.setAddress(form.getAddress());
		entry.setCode(form.getCode());
		entry.setFreeDouble(form.getFreeDouble());
		entry.setFreeString1(form.getFreeString1());
		entry.setFreeString2(form.getFreeString2());
		entry.setLabel(form.getLabel());
		entry.setLocation(form.getLocation());
		entry.setPackageType(form.getPackageType());
		entry.setPurchaseOrder(form.getPurchaseOrder());
		entry.setQuantity(form.getQuantity());
		entry.setSaleOrder(form.getSaleOrder());

		inventoryEntriesService.save(entry);
	}

	private void setValidationMessages(Model model) {
		if (this.tmpMsg != null) {
			model.addAttribute("msg", this.tmpMsg);
			this.tmpMsg = null;
		} else if (this.tmpWarning != null) {
			model.addAttribute("warning", this.tmpWarning);
			this.tmpWarning = null;
		}

	}
	

	/**
	 * @return String - i18n message code of error OR null if validation OK
	 */
	private String validateCurrentField(InventoryTerminalForm inventoryTerminalForm)
			throws NumberFormatException, UnknownDataTypeException {

		boolean advanced = inventoryTerminalForm.isCurrentColumnValidated();
		int inventoryId = inventoryTerminalForm.getInventoryId();
		String company = inventoryTerminalForm.getCompany();
		String currentValue = inventoryTerminalForm.getCurrentValue().trim().toUpperCase();

		String resultCode = null;

		switch (inventoryTerminalForm.getCurrentColumnCode()) {
		/* CODE */
		case "inventory.type.code":
			if (currentValue.length() > 45) {
				resultCode = "inventory.error.valuetoolong";
			}
			if (advanced) {
				X3Product product = x3Service.findProductByCode(company, currentValue);
				if (product != null) {
					resultCode = null;
					this.tmpMsg = product.getDescription();
				} else {
					resultCode = "general.productCode.notfound";
				}
			}
			inventoryTerminalForm.setCodeTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* ADDRESS */
		case "inventory.type.address":
			if (currentValue.length() > 15) {
				resultCode = "inventory.error.valuetoolong";
			}
			if (advanced) {
				if (!geodeService.checkIfAddressExist(currentValue)) {
					resultCode = "inventory.error.addressnotexist";
				}
			}
			inventoryTerminalForm.setAddressTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* LOCATION */
		case "inventory.type.location":
			if (currentValue.length() > 25) {
				resultCode = "inventory.error.valuetoolong";
			}
			if (advanced) {
				if (!x3Service.checkIfLocationExist(company, currentValue)) {
					resultCode = "inventory.error.locationnotexist";
				}
			}
			inventoryTerminalForm.setLocationTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* LABEL */
		case "inventory.type.label":
			if (currentValue.length() > 15) {
				resultCode = "inventory.error.valuetoolong";
			}
			if (advanced) {
				List<InventoryEntry> entries = inventoryEntriesService.findByInventoryIdAndLabelNumber(inventoryId,
						currentValue);
				if (!entries.isEmpty()) {
					resultCode = "inventory.error.labelinuse";		
					this.tmpWarning = "";
					if(entries.get(0).getAddress() != null && !entries.get(0).getAddress().isEmpty()){
						this.tmpWarning = "["+entries.get(0).getAddress()+"]";
					}
					if(entries.get(0).getCode() != null && !entries.get(0).getCode().isEmpty()){
						this.tmpWarning += " ["+entries.get(0).getCode()+"]";
					}
					if(this.tmpWarning.length()==0){
						this.tmpWarning=null;
					}
				}
			}
			inventoryTerminalForm.setLabelTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* SALE ORDER */
		case "inventory.type.order.sale":
			if (currentValue.length() > 30) {
				resultCode = "inventory.error.valuetoolong";
			}
			if (advanced) {
				X3SalesOrder order = x3Service.findSalesOrderByNumber(company, currentValue);
				if (order == null) {
					resultCode = "general.order.notfound";		
				}
				else{
					this.tmpMsg = order.getClientName();
				}
			}
			inventoryTerminalForm.setSaleOrderTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* SALE PURCHASE */
		case "inventory.type.order.purchase":
			if (currentValue.length() > 30) {
				resultCode = "inventory.error.valuetoolong";
			}
			if (advanced) {
				X3PurchaseOrder order = x3Service.findPurchaseOrderByNumber(company, currentValue);
				if (order == null) {
					resultCode = "general.order.notfound";		
				}
				else{
					this.tmpMsg = order.getSupplierName();
				}
			}
			inventoryTerminalForm.setPurchaseOrderTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* PACKAGE */
		case "inventory.type.packagetype":
			if (currentValue.length() > 15) {
				resultCode = "inventory.error.valuetoolong";
			}
			if (advanced) {
				String packageDescription = x3Service.findPackageDescription(company, currentValue);
				if(packageDescription == null){
					resultCode = "inventory.error.packagenotexist";
				}
				else{
					this.tmpMsg = packageDescription;
				}
			}
			inventoryTerminalForm.setPackageTypeTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* QUANTITY */
		case "inventory.type.quantity":
			try {
				Double val = Double.parseDouble(currentValue);
				if(advanced){
					if (val <= 0) {
						resultCode = "inventory.error.mustbepositive";
					}
				}
			} catch (NumberFormatException e) {
				resultCode = "inventory.error.mustbeanumber";
			}
			inventoryTerminalForm.setQuantityTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* FREESTRING 1 */
		case "inventory.type.freestring1":
			if (currentValue.length() > 45) {
				resultCode = "inventory.error.valuetoolong";
			}
			inventoryTerminalForm.setFreeString1Title(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* FREESTRING 2 */
		case "inventory.type.freestring2":
			if (currentValue.length() > 45) {
				resultCode = "inventory.error.valuetoolong";
			}
			inventoryTerminalForm.setFreeString2Title(inventoryTerminalForm.getCurrentColumnName());
			break;
		/* FREEDOUBLE */
		case "inventory.type.freedouble":
			try {
				Double val = Double.parseDouble(currentValue);
				if(advanced){
					if (val <= 0) {
						resultCode = "inventory.error.mustbepositive";
					}
				}
			} catch (NumberFormatException e) {
				resultCode = "inventory.error.mustbeanumber";
			}
			inventoryTerminalForm.setFreeDoubleTitle(inventoryTerminalForm.getCurrentColumnName());
			break;
		default:
			throw new UnknownDataTypeException();
		}

		return resultCode;
	}

	private void saveCurrentValueInForm(InventoryTerminalForm inventoryTerminalForm) throws UnknownDataTypeException {

		String currentValue = inventoryTerminalForm.getCurrentValue().trim().toUpperCase();

		switch (inventoryTerminalForm.getCurrentColumnCode()) {
		case "inventory.type.code":
			inventoryTerminalForm.setCode(currentValue);
			break;
		case "inventory.type.address":
			inventoryTerminalForm.setAddress(currentValue);
			break;
		case "inventory.type.location":
			inventoryTerminalForm.setLocation(currentValue);
			break;
		case "inventory.type.label":
			inventoryTerminalForm.setLabel(currentValue);
			break;
		case "inventory.type.order.sale":
			inventoryTerminalForm.setSaleOrder(currentValue);
			break;
		case "inventory.type.order.purchase":
			inventoryTerminalForm.setPurchaseOrder(currentValue);
			break;
		case "inventory.type.packagetype":
			inventoryTerminalForm.setPackageType(currentValue);
			break;
		case "inventory.type.quantity":
			inventoryTerminalForm.setQuantity(Double.parseDouble(currentValue));
			break;
		case "inventory.type.freestring1":
			inventoryTerminalForm.setFreeString1(currentValue);
			break;
		case "inventory.type.freestring2":
			inventoryTerminalForm.setFreeString2(currentValue);
			break;
		case "inventory.type.freedouble":
			inventoryTerminalForm.setFreeDouble(Double.parseDouble(currentValue));
			break;
		default:
			throw new UnknownDataTypeException();
		}

	}

	private void switchToNextColumn(InventoryTerminalForm inventoryTerminalForm, List<InventoryColumn> columns)
			throws EndOfColumnsException {
		int number = inventoryTerminalForm.getCurrentColumnNumber() + 1;
		if (number >= columns.size()) {
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
		List<InventoryColumn> columns = inventoryColumnsService
				.findInventoryColumnsSortByOrder(inventoryTerminalForm.getInventoryId());
		int number = inventoryTerminalForm.getCurrentColumnNumber() + 1;
		if (number >= columns.size()) {
			throw new EndOfColumnsException();
		}
		inventoryTerminalForm.setCurrentColumnNumber(number);
		inventoryTerminalForm.setCurrentColumnCode(columns.get(number).getInventoryDataType().getColumnTypeCode());
		inventoryTerminalForm.setCurrentColumnName(columns.get(number).getColumnName());
		inventoryTerminalForm.setCurrentColumnRequired(columns.get(number).getRequired());
		inventoryTerminalForm.setCurrentColumnValidated(columns.get(number).getValidated());
		inventoryTerminalForm.setCurrentValue("");
	}
	
	@RequestMapping("/inventory/showinventory/{id}")
	@Transactional
	public String showInventoryView(@PathVariable("id") int id, Model model) throws NotFoundException, UnknownDataTypeException {
		
		Inventory inventory = inventoryService.findById(id);
		
		if (inventory == null) {
			throw new NotFoundException("Inventory not found");
		}
		
		List<InventoryColumn> columns = inventoryColumnsService.findInventoryColumnsSortByOrder(inventory.getId());
		List<InventoryEntry> entries = inventoryEntriesService.findByInventoryIdSortByLines(inventory.getId());
		List<InventoryShowLine> lines = new ArrayList<>();
		InventoryShowLine line;
		
		for(InventoryEntry entry: entries){
			line = prepareShowLine(entry, columns);
			lines.add(line);
		}
		
		model.addAttribute("inventory", inventory);
		model.addAttribute("columns", columns);
		model.addAttribute("lines", lines);

		return "inventory/showinventory";
	}
	
	@RequestMapping("/inventory/shipmentsummary/{id}")
	@Transactional
	public String showInventoryShipmentView(@PathVariable("id") int id, Model model, Locale locale, RedirectAttributes redirectAttrs) throws NotFoundException {

		Inventory inventory = inventoryService.findById(id);
		
		if (inventory == null) {
			throw new NotFoundException("Inventory not found");
		}
		
		boolean codePresent = false;
		boolean quantityPresent = false;
		 
		for(InventoryColumn col: inventory.getColumns()){
			if(col.getInventoryDataType().getColumnTypeCode().equals("inventory.type.code")){
				codePresent = true;
			}
			if(col.getInventoryDataType().getColumnTypeCode().equals("inventory.type.quantity")){
				quantityPresent = true;
			}
		}
		
		if(!codePresent || !quantityPresent){
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("inventory.error.norequiredcolumns", null, locale));
			return "redirect:/inventory/showinventory/" + inventory.getId();
		}
		
		List<X3Product> allProducts = x3Service.findAllActiveProducts("ATW");
		List<InventoryEntry> entries = inventoryEntriesService.findByInventoryIdSortByLines(inventory.getId());
		Map<String, Integer> shipmentStock = x3Service.findStockByLocation("ATW", "GEODE");
		Map<String, Integer> inventoryStock = new HashMap<>();
		
		
		for(InventoryEntry entry: entries){
			if(!inventoryStock.containsKey(entry.getCode())){
				inventoryStock.put(entry.getCode(), (int)entry.getQuantity());
			}
			else{
				inventoryStock.put(entry.getCode(), (int)(inventoryStock.get(entry.getCode())+ entry.getQuantity()));
			}
		}
		
		List<List<String>> lines = new ArrayList<>();
		List<String> lineValues;
		int invQty;
		int x3Qty;
		
		for(X3Product product: allProducts){
			invQty = inventoryStock.getOrDefault(product.getCode(), 0);
			x3Qty = shipmentStock.getOrDefault(product.getCode(), 0);
			
			if(invQty > 0 || x3Qty > 0){
				lineValues = new ArrayList<>();
				lineValues.add(product.getCode());
				lineValues.add(product.getDescription());
				lineValues.add(product.getCategory());
				lineValues.add(""+(invQty));
				lineValues.add(""+x3Qty);
				lineValues.add(""+ (invQty - x3Qty));
				lines.add(lineValues);
			}
		}		
		
		model.addAttribute("inventory", inventory);
		model.addAttribute("shiplines", lines);

		return "inventory/summary";
	}
	@RequestMapping("/inventory/summary/{id}")
	@Transactional
	public String showInventorySummaryView(@PathVariable("id") int id, Model model, Locale locale, RedirectAttributes redirectAttrs) throws NotFoundException {
		
		Inventory inventory = inventoryService.findById(id);
		
		if (inventory == null) {
			throw new NotFoundException("Inventory not found");
		}
		
		boolean codePresent = false;
		boolean quantityPresent = false;
		 
		for(InventoryColumn col: inventory.getColumns()){
			if(col.getInventoryDataType().getColumnTypeCode().equals("inventory.type.code")){
				codePresent = true;
			}
			if(col.getInventoryDataType().getColumnTypeCode().equals("inventory.type.quantity")){
				quantityPresent = true;
			}
		}
		
		if(!codePresent || !quantityPresent){
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("inventory.error.norequiredcolumns", null, locale));
			return "redirect:/inventory/showinventory/" + inventory.getId();
		}
		
		
		
		List<InventoryEntry> entries = inventoryEntriesService.findByInventoryIdSortByLines(inventory.getId());
		
		// count inventory result
		Map<String, InventorySummaryInfo> inventorySum = new HashMap<>();
		for(InventoryEntry entry: entries){
			if(!inventorySum.containsKey(entry.getCode())){
				inventorySum.put(entry.getCode(), new InventorySummaryInfo(entry.getCode()));
			}
			inventorySum.get(entry.getCode()).addQuantity((int)entry.getQuantity());
		}
		
		// get store info from X3
		Map<String, X3StoreInfo> storeInfo = x3Service.getX3StoreInfoByCode(inventory.getCompany());
				
		// prepare values
		List<List<String>> lines = new ArrayList<>();
		List<String> lineValues;
		X3StoreInfo empty = new X3StoreInfo();
		for(InventorySummaryInfo inf: inventorySum.values()){
			lineValues = new ArrayList<>();
			lineValues.add(inf.getProductCode());
			lineValues.add(inf.getCounter()+"");
			lineValues.add(inf.getQuantity()+"");
			lineValues.add(storeInfo.getOrDefault(inf.getProductCode(), empty).getGeneralStore()+"");
			lineValues.add((inf.getQuantity() - storeInfo.getOrDefault(inf.getProductCode(), empty).getGeneralStore())+"");
			lineValues.add(storeInfo.getOrDefault(inf.getProductCode(), empty).getMag()+"");
			lineValues.add(storeInfo.getOrDefault(inf.getProductCode(), empty).getQgx()+"");
			lineValues.add(storeInfo.getOrDefault(inf.getProductCode(), empty).getWgx()+"");
			lineValues.add(storeInfo.getOrDefault(inf.getProductCode(), empty).getGeode()+"");
			lines.add(lineValues);
		}
		
		
		model.addAttribute("inventory", inventory);
		model.addAttribute("lines", lines);

		return "inventory/summary";
	}

	private InventoryShowLine prepareShowLine(InventoryEntry entry, List<InventoryColumn> columns) throws UnknownDataTypeException {
		InventoryShowLine line = new InventoryShowLine();
		
		line.getValues().add(""+entry.getLine());
		line.getValues().add(dateHelper.formatYyyyMmDdHhMm(entry.getCreationDate()));
		
		for(InventoryColumn column: columns){
			switch (column.getInventoryDataType().getColumnTypeCode()) {
			case "inventory.type.code":
				line.getValues().add(entry.getCode());
				break;
			case "inventory.type.address":
				line.getValues().add(entry.getAddress());
				break;
			case "inventory.type.location":
				line.getValues().add(entry.getLocation());
				break;
			case "inventory.type.label":
				line.getValues().add(entry.getLabel());
				break;
			case "inventory.type.order.sale":
				line.getValues().add(entry.getSaleOrder());
				break;
			case "inventory.type.order.purchase":
				line.getValues().add(entry.getPurchaseOrder());
				break;
			case "inventory.type.packagetype":
				line.getValues().add(entry.getPackageType());
				break;
			case "inventory.type.quantity":
				line.getValues().add(textHelper.formatDouble2OrNoDigits(entry.getQuantity()));
				break;
			case "inventory.type.freestring1":
				line.getValues().add(entry.getFreeString1());
				break;
			case "inventory.type.freestring2":
				line.getValues().add(entry.getFreeString2());
				break;
			case "inventory.type.freedouble":
				line.getValues().add(textHelper.formatDouble2OrNoDigits(entry.getFreeDouble()));
				break;
			default:
				throw new UnknownDataTypeException();
			}
		}
		
		line.getValues().add(entry.getUserCode());
		line.getValues().add(entry.getUserName());

		
		
		
		return line;
	}

}

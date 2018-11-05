package sbs.controller.phones;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.thymeleaf.TemplateEngine;

import javassist.NotFoundException;
import sbs.helpers.TextHelper;
import sbs.model.phones.PhoneCategory;
import sbs.model.phones.PhoneEntry;
import sbs.service.phones.PhoneCategoriesService;
import sbs.service.phones.PhoneEntriesService;

@Controller
@RequestMapping("phones")
public class PhonesController {
	@Autowired
	MessageSource messageSource;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	TextHelper textHelper;
	@Autowired
	PhoneEntriesService phoneEntriesService;
	@Autowired
	PhoneCategoriesService phoneCategoriesService;

	@RequestMapping(value = "/list")
	public String defaultList(Model model, Locale locale) {
		if(locale.getLanguage().equals("pl")){
			return "redirect:/phones/list/pl";
		}
		else{
			return "redirect:/phones/list/it";
		}
	}
	
	@RequestMapping(value = "/list/{ver}")
	@Transactional
	public String list(@PathVariable("ver") String version, Model model) {
		model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber(version));
		model.addAttribute("version", version);
		return "phones/list";
	}

	@RequestMapping(value = "/print/{ver}")
	@Transactional
	public String print(@PathVariable("ver") String version, Model model) {
		List<PhoneEntry> entries = phoneEntriesService.findAllOrderByCategoryAndNumber(version);
		List<PhoneColumnLine> list = new ArrayList<>();

		String currentCategory = "";
		PhoneColumnLine line;
		for (PhoneEntry entry : entries) {
			// add additional category line if switch category
			if (!entry.getCategory().getName().equals(currentCategory)) {
				line = new PhoneColumnLine();
				line.setCategory(true);
				line.setName(entry.getCategory().getName());
				list.add(line);
				currentCategory = line.getName();
			}
			// add phone line
			line = new PhoneColumnLine();
			line.setCategory(false);
			line.setNumber(entry.getNumber());
			line.setName(entry.getName());
			list.add(line);
		}

		Calendar cal = Calendar.getInstance();
		String prtVersion = "ver. " + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.YEAR);

		model.addAttribute("ver", prtVersion);
		model.addAttribute("list", list);
		model.addAttribute("version", version);
		
		return "phones/print";
	}


	@RequestMapping(value = "/createentry/{ver}")
	@Transactional
	public String createEntryView(@PathVariable("ver") String version, Model model) {
		
		PhoneEditForm phoneEditForm = new PhoneEditForm();
		phoneEditForm.setVersion(version);
		model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder(version));
		model.addAttribute("phoneEditForm", phoneEditForm);
		model.addAttribute("version", version);
		return "phones/createentry";
	}
	
	@RequestMapping(value = "/editentry/{ver}/{id}")
	@Transactional
	public String editEntryView(@PathVariable("ver") String version, @PathVariable("id") int id, Model model) throws NotFoundException {
		
		PhoneEntry entry = phoneEntriesService.findById(id);
		if (entry == null) {
			throw new NotFoundException("Entry not found");
		}

		PhoneEditForm phoneEditForm = getPhoneEditFormFromEntity(entry, version);
		
		model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder(version));
		model.addAttribute("phoneEditForm", phoneEditForm);
		model.addAttribute("version", version);
		return "phones/editentry";
	}
	
	private PhoneEditForm getPhoneEditFormFromEntity(PhoneEntry entry, String version) {
		PhoneEditForm form = new PhoneEditForm();
		form.setCategoryId(entry.getCategory().getId());
		form.setEmail(entry.getEmail());
		form.setName(entry.getName());
		form.setNumber(entry.getNumber());
		form.setNumberShort(entry.getNumberShort());
		form.setNumberInternal(entry.getNumberInternal());
		form.setNumberInternalPortable(entry.getNumberInternalPortable());
		form.setPosition(entry.getPosition());
		form.setVersion(entry.getVersion());
		form.setVoip(entry.getVoip());
		form.setNote(entry.getNote());
		form.setId(entry.getId());
	
		return form;
	}
	
	@RequestMapping(value = "/numberaction", params = { "create" }, method = RequestMethod.POST)
	@Transactional
	public String createNumber(@Valid PhoneEditForm phoneEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder(phoneEditForm.getVersion()));
			model.addAttribute("version", phoneEditForm.getVersion());
			return "phones/createentry";
		}

		if (!(phoneEntriesService.findByNumber(phoneEditForm.getNumber(), phoneEditForm.getVersion()).isEmpty())) {
			bindingResult.rejectValue("number", "phones.error.numberinuse");
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder(phoneEditForm.getVersion()));
			model.addAttribute("version", phoneEditForm.getVersion());
			return "phones/createentry";
		}

		PhoneCategory category = phoneCategoriesService.findById(phoneEditForm.getCategoryId());
		PhoneEntry entry = new PhoneEntry();
		entry.setNumber(phoneEditForm.getNumber());
		entry.setNumberShort(phoneEditForm.getNumberShort());
		entry.setNumberInternal(phoneEditForm.getNumberInternal());
		entry.setNumberInternalPortable(phoneEditForm.getNumberInternalPortable());
		entry.setName(phoneEditForm.getName().trim());
		entry.setPosition(phoneEditForm.getPosition().trim());
		entry.setEmail(phoneEditForm.getEmail().trim());
		entry.setCategory(category);
		entry.setVersion(phoneEditForm.getVersion());
		entry.setNote(phoneEditForm.getNote());
		entry.setVoip(phoneEditForm.getVoip());
		
		phoneEntriesService.save(entry);

		// redirect
		redirectAttrs.addFlashAttribute("msg", entry.getName() + " (" + entry.getNumber() + ") - "
				+ messageSource.getMessage("action.saved", null, locale));
		return "redirect:/phones/list/"+phoneEditForm.getVersion();
	}

	@RequestMapping(value = "/numberaction", params = { "update" }, method = RequestMethod.POST)
	@Transactional
	public String updateNumber(@Valid PhoneEditForm phoneEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder(phoneEditForm.getVersion()));
			model.addAttribute("version", phoneEditForm.getVersion());
			return "phones/editentry";
		}

		PhoneEntry entry = phoneEntriesService.findById(phoneEditForm.getId());
		if (entry == null) {
			throw new NotFoundException("Entry not found");
		}

		PhoneCategory category = phoneCategoriesService.findById(phoneEditForm.getCategoryId());

		entry.setNumber(phoneEditForm.getNumber());
		entry.setNumberShort(phoneEditForm.getNumberShort());
		entry.setNumberInternal(phoneEditForm.getNumberInternal());
		entry.setNumberInternalPortable(phoneEditForm.getNumberInternalPortable());
		entry.setName(phoneEditForm.getName().trim());
		entry.setPosition(phoneEditForm.getPosition().trim());
		entry.setEmail(phoneEditForm.getEmail().trim());
		entry.setCategory(category);
		entry.setVersion(phoneEditForm.getVersion());
		entry.setNote(phoneEditForm.getNote());
		entry.setVoip(phoneEditForm.getVoip());

		phoneEntriesService.update(entry);

		// redirect
		redirectAttrs.addFlashAttribute("msg", entry.getName() + " (" + entry.getNumber() + ") - "
				+ messageSource.getMessage("action.updated", null, locale));
		return "redirect:/phones/list/"+phoneEditForm.getVersion();
	}

	@RequestMapping(value = "/numberaction", params = { "delete" }, method = RequestMethod.POST)
	@Transactional
	public String deleteNumber(PhoneEditForm phoneEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {


		PhoneEntry entry = phoneEntriesService.findById(phoneEditForm.getId());
		if (entry == null) {
			throw new NotFoundException("Entry not found");
		}

		phoneEntriesService.remove(entry);

		// redirect
		redirectAttrs.addFlashAttribute("msg", entry.getName() + " (" + entry.getNumber() + ") - "
				+ messageSource.getMessage("action.deleted", null, locale));
		return "redirect:/phones/list/"+phoneEditForm.getVersion();
	}
	
	@RequestMapping(value = "/managecategories/{ver}")
	@Transactional
	public String manageCatView(@PathVariable("ver") String version, RedirectAttributes redirectAttrs, Locale locale,
			Model model) throws NotFoundException {

		
		model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder(version));
		model.addAttribute("version", version);
		
		return "phones/managecategories";
	}
	
	@RequestMapping(value = "/createcategory/{ver}")
	@Transactional
	public String createCategoryView(@PathVariable("ver") String version, Model model) {
		
		PhoneCategoryEditForm phoneCategoryEditForm = new PhoneCategoryEditForm();
		phoneCategoryEditForm.setVersion(version);
		model.addAttribute("phoneCategoryEditForm", phoneCategoryEditForm);
		model.addAttribute("version", version);
		return "phones/createcategory";
	}
	
	@RequestMapping(value = "/editcategory/{ver}/{id}")
	@Transactional
	public String editCategoryView(@PathVariable("ver") String version, @PathVariable("id") int id, Model model) throws NotFoundException {
		
		PhoneCategory category = phoneCategoriesService.findById(id);
		if (category == null) {
			throw new NotFoundException("Category not found");
		}

		PhoneCategoryEditForm phoneCategoryEditForm = new PhoneCategoryEditForm();
		phoneCategoryEditForm.setVersion(version);
		phoneCategoryEditForm.setId(category.getId());
		phoneCategoryEditForm.setName(category.getName());
		phoneCategoryEditForm.setOrder(category.getOrder());
		
		model.addAttribute("phoneCategoryEditForm", phoneCategoryEditForm);
		model.addAttribute("version", version);
		return "phones/editcategory";
	}

	@RequestMapping(value = "/categoryaction", params = { "create" }, method = RequestMethod.POST)
	@Transactional
	public String createCategory(PhoneCategoryEditForm phoneCategoryEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {


		if (phoneCategoryEditForm.getName().length() == 0 || phoneCategoryEditForm.getName().length() > 50) {
			bindingResult.rejectValue("name", "error.size.wrong");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("version", phoneCategoryEditForm.getVersion());
			return "phones/createcategory";
		}

		PhoneCategory category = new PhoneCategory();
		category.setName(phoneCategoryEditForm.getName().trim().toUpperCase());
		category.setOrder(phoneCategoryEditForm.getOrder() == null ? 0 : phoneCategoryEditForm.getOrder());
		category.setVersion(phoneCategoryEditForm.getVersion());
		phoneCategoriesService.save(category);

		// redirect
		redirectAttrs.addFlashAttribute("msg",
				category.getName() + " - " + messageSource.getMessage("action.saved", null, locale));
		return "redirect:/phones/managecategories/"+phoneCategoryEditForm.getVersion();
	}

	@RequestMapping(value = "/categoryaction", params = { "update" }, method = RequestMethod.POST)
	@Transactional
	public String updateCategory(PhoneCategoryEditForm phoneCategoryEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		
		if (phoneCategoryEditForm.getName().length() == 0 || phoneCategoryEditForm.getName().length() > 50) {
			bindingResult.rejectValue("name", "error.size.wrong");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("version", phoneCategoryEditForm.getVersion());
			return "phones/editcategory";
		}
		
		PhoneCategory category = phoneCategoriesService.findById(phoneCategoryEditForm.getId());
		if (category == null) {
			throw new NotFoundException("Category not found");
		}

		category.setName(phoneCategoryEditForm.getName().trim().toUpperCase());
		category.setOrder(phoneCategoryEditForm.getOrder() == null ? 0 : phoneCategoryEditForm.getOrder());
		category.setVersion(phoneCategoryEditForm.getVersion());
		phoneCategoriesService.update(category);

		// redirect
		redirectAttrs.addFlashAttribute("msg",
				category.getName() + " - " + messageSource.getMessage("action.updated", null, locale));
		return "redirect:/phones/managecategories/"+phoneCategoryEditForm.getVersion();
	}

	@RequestMapping(value = "/categoryaction", params = { "delete" }, method = RequestMethod.POST)
	@Transactional
	public String deleteCategory(PhoneCategoryEditForm phoneCategoryEditForm, RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException {

		PhoneCategory category = phoneCategoriesService.findById(phoneCategoryEditForm.getId());
		if (category == null) {
			throw new NotFoundException("Category not found");
		}

		phoneCategoriesService.remove(category);

		// redirect
		redirectAttrs.addFlashAttribute("msg",
				category.getName() + " - " + messageSource.getMessage("action.deleted", null, locale));
		return "redirect:/phones/managecategories/"+phoneCategoryEditForm.getVersion();
	}

}

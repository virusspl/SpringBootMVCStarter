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
	@Transactional
	public String list(Model model) {
		PhoneEditForm phoneEditForm = new PhoneEditForm();
		model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder());
		model.addAttribute("phoneEditForm", phoneEditForm);
		model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber());
		return "phones/list";
	}

	@RequestMapping(value = "/print")
	@Transactional
	public String print(Model model) {
		List<PhoneEntry> entries = phoneEntriesService.findAllOrderByCategoryAndNumber();
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
		String version = "ver. " + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.YEAR);

		model.addAttribute("ver", version);
		model.addAttribute("list", list);

		return "phones/print";
	}

	@RequestMapping(value = "/numberaction", params = { "editcategory" }, method = RequestMethod.POST)
	@Transactional
	public String editCategory(PhoneEditForm phoneEditForm, RedirectAttributes redirectAttrs, Locale locale,
			Model model) throws NotFoundException {

		PhoneCategoryEditForm phoneCategoryEditForm = new PhoneCategoryEditForm();

		if (phoneEditForm.getCategoryId() != null) {
			PhoneCategory category = phoneCategoriesService.findById(phoneEditForm.getCategoryId());
			if (category == null) {
				throw new NotFoundException("No category found: #" + phoneEditForm.getCategoryId());
			}

			phoneCategoryEditForm.setId(category.getId());
			phoneCategoryEditForm.setName(category.getName());
			phoneCategoryEditForm.setOrder(category.getOrder());
		}
		model.addAttribute("phoneCategoryEditForm", phoneCategoryEditForm);
		return "phones/categoryedit";
	}

	@RequestMapping(value = "/categoryaction", params = { "create" }, method = RequestMethod.POST)
	@Transactional
	public String createCategory(PhoneCategoryEditForm phoneCategoryEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (phoneCategoryEditForm.getId() != null && phoneCategoryEditForm.getId() > 0) {
			bindingResult.rejectValue("id", "phones.error.redundandpcid");
		}

		if (phoneCategoryEditForm.getName().length() == 0 || phoneCategoryEditForm.getName().length() > 50) {
			bindingResult.rejectValue("name", "error.size.wrong");
		}

		if (bindingResult.hasErrors()) {
			return "phones/categoryedit";
		}

		PhoneCategory category = new PhoneCategory();
		category.setName(phoneCategoryEditForm.getName().trim().toUpperCase());
		category.setOrder(phoneCategoryEditForm.getOrder() == null ? 0 : phoneCategoryEditForm.getOrder());
		phoneCategoriesService.save(category);

		// redirect
		redirectAttrs.addFlashAttribute("msg",
				category.getName() + " - " + messageSource.getMessage("action.saved", null, locale));
		return "redirect:/phones/list";
	}

	@RequestMapping(value = "/categoryaction", params = { "update" }, method = RequestMethod.POST)
	@Transactional
	public String updateCategory(PhoneCategoryEditForm phoneCategoryEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (phoneCategoryEditForm.getId() == null || phoneCategoryEditForm.getId() < 0) {
			bindingResult.rejectValue("id", "phones.error.missingid");
		}

		if (phoneCategoryEditForm.getName().length() == 0 || phoneCategoryEditForm.getName().length() > 50) {
			bindingResult.rejectValue("name", "error.size.wrong");
		}

		if (bindingResult.hasErrors()) {
			return "phones/categoryedit";
		}

		PhoneCategory category = phoneCategoriesService.findById(phoneCategoryEditForm.getId());
		if (category == null) {
			bindingResult.rejectValue("id", "phones.error.categorynotfound");
			return "phones/categoryedit";
		}

		category.setName(phoneCategoryEditForm.getName().trim().toUpperCase());
		category.setOrder(phoneCategoryEditForm.getOrder() == null ? 0 : phoneCategoryEditForm.getOrder());
		phoneCategoriesService.update(category);

		// redirect
		redirectAttrs.addFlashAttribute("msg",
				category.getName() + " - " + messageSource.getMessage("action.updated", null, locale));
		return "redirect:/phones/list";
	}

	@RequestMapping(value = "/categoryaction", params = { "delete" }, method = RequestMethod.POST)
	@Transactional
	public String deleteCategory(PhoneCategoryEditForm phoneCategoryEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (phoneCategoryEditForm.getId() == null || phoneCategoryEditForm.getId() < 0) {
			bindingResult.rejectValue("id", "phones.error.missingid");
			return "phones/categoryedit";
		}
		PhoneCategory category = phoneCategoriesService.findById(phoneCategoryEditForm.getId());
		if (category == null) {
			bindingResult.rejectValue("id", "phones.error.categorynotfound");
			return "phones/categoryedit";
		}

		phoneCategoriesService.remove(category);

		// redirect
		redirectAttrs.addFlashAttribute("msg",
				category.getName() + " - " + messageSource.getMessage("action.deleted", null, locale));
		return "redirect:/phones/list";
	}

	@RequestMapping(value = "/numberaction", params = { "create" }, method = RequestMethod.POST)
	@Transactional
	public String createNumber(@Valid PhoneEditForm phoneEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder());
			model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber());
			return "phones/list";
		}

		if (!(phoneEntriesService.findByNumber(phoneEditForm.getNumber()).isEmpty())) {
			bindingResult.rejectValue("number", "phones.error.numberinuse");
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder());
			model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber());
			return "phones/list";
		}

		PhoneCategory category = phoneCategoriesService.findById(phoneEditForm.getCategoryId());
		PhoneEntry entry = new PhoneEntry();
		entry.setNumber(phoneEditForm.getNumber());
		entry.setName(phoneEditForm.getName().trim());
		entry.setPosition(phoneEditForm.getPosition().trim());
		entry.setCategory(category);
		phoneEntriesService.save(entry);

		// redirect
		redirectAttrs.addFlashAttribute("msg", entry.getName() + " (" + entry.getNumber() + ") - "
				+ messageSource.getMessage("action.saved", null, locale));
		return "redirect:/phones/list";
	}

	@RequestMapping(value = "/numberaction", params = { "update" }, method = RequestMethod.POST)
	@Transactional
	public String updateNumber(@Valid PhoneEditForm phoneEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder());
			model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber());
			return "phones/list";
		}

		List<PhoneEntry> entries = phoneEntriesService.findByNumber(phoneEditForm.getNumber());
		if (entries.isEmpty()) {
			bindingResult.rejectValue("number", "phones.error.numbernotfound");
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder());
			model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber());
			return "phones/list";
		}

		PhoneEntry entry = entries.get(0);
		PhoneCategory category = phoneCategoriesService.findById(phoneEditForm.getCategoryId());

		entry.setNumber(phoneEditForm.getNumber());
		entry.setName(phoneEditForm.getName().trim());
		entry.setPosition(phoneEditForm.getPosition().trim());
		entry.setCategory(category);

		phoneEntriesService.update(entry);

		// redirect
		redirectAttrs.addFlashAttribute("msg", entry.getName() + " (" + entry.getNumber() + ") - "
				+ messageSource.getMessage("action.updated", null, locale));
		return "redirect:/phones/list";
	}

	@RequestMapping(value = "/numberaction", params = { "delete" }, method = RequestMethod.POST)
	@Transactional
	public String deleteNumber(PhoneEditForm phoneEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (phoneEditForm.getNumber() == null) {
			bindingResult.rejectValue("number", "NotNull.phoneEditForm.number");
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder());
			model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber());
			return "phones/list";
		}

		List<PhoneEntry> entries = phoneEntriesService.findByNumber(phoneEditForm.getNumber());
		if (entries.isEmpty()) {
			bindingResult.rejectValue("number", "phones.error.numbernotfound");
			model.addAttribute("categories", phoneCategoriesService.findAllByAscOrder());
			model.addAttribute("list", phoneEntriesService.findAllOrderByCategoryAndNumber());
			return "phones/list";
		}

		phoneEntriesService.remove(entries.get(0));

		// redirect
		redirectAttrs.addFlashAttribute("msg", entries.get(0).getName() + " (" + entries.get(0).getNumber() + ") - "
				+ messageSource.getMessage("action.deleted", null, locale));
		return "redirect:/phones/list";
	}

}

package sbs.controller.downtimes;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.model.downtimes.DowntimeCause;
import sbs.model.downtimes.DowntimeType;
import sbs.model.users.User;
import sbs.service.downtimes.DowntimeCausesService;
import sbs.service.downtimes.DowntimeTypesService;
import sbs.service.downtimes.DowntimesService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("downtimes/manage")
public class DowntimesManageController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	DowntimeTypesService downtimeTypesService;
	@Autowired
	DowntimeCausesService downtimeCausesService;
	@Autowired
	UserService userService;
	@Autowired
	DowntimesService downtimesService;
	
	
	@Cacheable("downtimeTypes")
	@ModelAttribute("downtimeTypes")
	public List<DowntimeType> getDowntimeTypes(){
	    return downtimeTypesService.findAll();
	}
	
	@ModelAttribute("responsibleUsers")
	public List<User> getResponsibleUsers() {
		return userService.findByRole("ROLE_DTRESPONSIBLE");
	}
	
	
	@RequestMapping(value = "causes")
	public String showManageCauses(Model model) {
		model.addAttribute("downtimeCauses", downtimeCausesService.findAll());
		return "downtimes/causes";
	}
	
	@RequestMapping(value = "causes/add")
	public String showAddCause(Model model) {
		model.addAttribute("formCause", new FormCause());
		return "downtimes/causes.add";
	}
	
	@RequestMapping(value = "causes/add", method = RequestMethod.POST)
	public String addCause(@Valid FormCause formCause, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		// validate
		if (bindingResult.hasErrors()) {
			return "downtimes/causes.add";
		}
		
		DowntimeType type = downtimeTypesService.findById(formCause.getTypeId());
		if (type == null) {
			throw new NotFoundException("Downtime type not found: " + formCause.getTypeId());
		}
		
		User user = userService.findById(formCause.getResponsibleId());
		if (user == null) {
			throw new NotFoundException("Responsible user not found: " + formCause.getResponsibleId());
		}
		
		DowntimeCause cause = new DowntimeCause();
		cause.setActive(true);
		cause.setOrder(formCause.getOrder());
		cause.setShortText(formCause.getShortText());
		cause.setText(formCause.getText());
		cause.setDowntimeType(type);
		cause.setResponsibleUser(user);
		
		downtimeCausesService.save(cause);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale) + ": " + cause.getShortText());
		return "redirect:/downtimes/manage/causes";
	}
	
	
	
	@RequestMapping(value = "causes/edit/{id}")
	public String showEditCause(@PathVariable("id") int id, Model model) throws NotFoundException{
		
		DowntimeCause cause = downtimeCausesService.findById(id);
		if (cause == null) {
			throw new NotFoundException("Cause not found");
		}
		
		FormCause form = new FormCause();
		form.setId(cause.getId());
		form.setActive(cause.isActive());
		form.setOrder(cause.getOrder());
		form.setShortText(cause.getShortText());
		form.setText(cause.getText());
		form.setTypeId(cause.getDowntimeType().getId());
		form.setResponsibleId(cause.getResponsibleUser().getId());

		model.addAttribute("formCause", form);		
		
		return "downtimes/causes.edit";
	}
	
	
	@RequestMapping(value = "/causes/edit", method = RequestMethod.POST)
	@Transactional
	public String editCause(@Valid FormCause formCause, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
			
		if (bindingResult.hasErrors()) {
			return "downtimes/causes.add";
		}
		
		DowntimeCause cause = downtimeCausesService.findById(formCause.getId());
		if (cause == null) {
			throw new NotFoundException("Cause not found");
		}
		
		cause.setActive(formCause.isActive());
		cause.setOrder(formCause.getOrder());
		cause.setShortText(formCause.getShortText());
		cause.setText(formCause.getText());
		if(cause.getDowntimeType().getId() != formCause.getTypeId()){
			DowntimeType type = downtimeTypesService.findById(formCause.getTypeId());
			if (type == null) {
				throw new NotFoundException("Downtime type not found: " + formCause.getTypeId());
			}
			cause.setDowntimeType(type);
		}
		
		if(cause.getResponsibleUser().getId() != formCause.getResponsibleId()){
			User user = userService.findById(formCause.getResponsibleId());
			if (user == null) {
				throw new NotFoundException("Responsible user not found: " + formCause.getResponsibleId());
			}
			cause.setResponsibleUser(user);
		}
		
		downtimeCausesService.save(cause);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale) + ": " + cause.getShortText());
		return "redirect:/downtimes/manage/causes";
	}
	
	@RequestMapping(value = "/causes/delete/{id}")
	@Transactional
	public String editCause(@PathVariable("id") int id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		
		DowntimeCause cause = downtimeCausesService.findById(id);
		if (cause == null) {
			throw new NotFoundException("Cause not found");
		}
		
		if(downtimeCausesService.isIndependent(cause)){
			downtimeCausesService.remove(cause);
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.removed", null, locale) + ": " + cause.getShortText());
		}
		else{
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("downtimes.error.cause.inuse", null, locale) + ": " + cause.getShortText());
		}

		return "redirect:/downtimes/manage/causes";
	}
	
	@RequestMapping(value = "/reports")
	@Transactional
	public String showReportsView(Model model) {
		FormDowntimesReports form = new FormDowntimesReports();
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, -1);
		form.setStartDate(new Timestamp(cal.getTimeInMillis()));
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		form.setEndDate(new Timestamp(cal.getTimeInMillis()));

		model.addAttribute("dtReportsForm", form);
		return "downtimes/reports";
	}
	
	@RequestMapping(value = "/reports/makelist", method = RequestMethod.POST)
	public String viewList(@Valid FormDowntimesReports dtReportsForm, @RequestParam String type, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) throws NotFoundException {
		
		if (bindingResult.hasErrors()) {
			return "downtimes/reports";
		}
		
		List<ReportNotifierLine> notifierList = downtimesService.getReportByNotifier(dtReportsForm.getStartDate(), dtReportsForm.getEndDate());
		List<ReportResponsibleLine> responsibleList = downtimesService.getReportByResponsible(dtReportsForm.getStartDate(), dtReportsForm.getEndDate());

		redirectAttrs.addFlashAttribute("notifierList", notifierList);
		redirectAttrs.addFlashAttribute("responsibleList", responsibleList);
		
		return "redirect:/downtimes/manage/reports";		
	}
	
}

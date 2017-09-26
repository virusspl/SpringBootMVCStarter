package sbs.controller.hrua;

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
import sbs.controller.upload.UploadController;
import sbs.model.hruafiles.HrUaInfo;
import sbs.model.users.User;
import sbs.service.hrua.HrUaService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("hrua")
public class HrUaController {

	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	UploadController uploadController;
	@Autowired
	HrUaService hrUaService;
	
	
	@RequestMapping(value = "/list")
	@Transactional
	public String listAll(Model model) {
		model.addAttribute("users", hrUaService.findCurrent());
		return "hrua/list";
	}
	@RequestMapping(value = "/archive")
	@Transactional
	public String listArchived(Model model) {
		model.addAttribute("users", hrUaService.findArchived());
		return "hrua/list";
	}
	
	@RequestMapping(value = "/user")
	@Transactional
	public String create(HrUaCreateForm hrUaCreateForm, Model model) {
		return "hrua/user";
	}
	
	@RequestMapping(value = "/user/{id}")
	@Transactional
	public String edit(@PathVariable("id") int id,  Model model) throws NotFoundException {
		HrUaInfo object = hrUaService.findById(id); 
		if (object == null) {
			throw new NotFoundException("User not found");
		}
		model.addAttribute(HrUaCreateForm.hrUaCreateFormFromHrUaInfo(object));
		return "hrua/user";
	}
	
	@RequestMapping(value = "/user", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String save(@Valid HrUaCreateForm hrUaCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		// validate
		if (bindingResult.hasErrors()) {
			return "hrua/user";
		}
		
		HrUaInfo object = hrUaService.findById(hrUaCreateForm.getId());
		if (object == null) {
			object = new HrUaInfo();
			object.setCreationDate(new Timestamp(new java.util.Date().getTime()));
			User user = userService.getAuthenticatedUser();
			user.getHrUaFilesCreated().add(object);
			object.setCreator(user);
		}
		object.fillFromEditForm(hrUaCreateForm);
		hrUaService.saveOrUpdate(object);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/hrua/list/";
	}
	
}

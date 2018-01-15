package sbs.controller.proprog;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import sbs.controller.buyorders.ResponseForm;
import sbs.model.buyorders.BuyOrder;
import sbs.model.proprog.Project;
import sbs.service.proprog.ProjectProgressService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("proprog")
public class ProjectProgressController {

	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	ProjectProgressService projectProgressService;
	
	@RequestMapping(value = "/list")
	@Transactional
	public String listAll(Model model) {
		ArrayList<ListItem> projects = new ArrayList<>();
		List<Project> list = projectProgressService.findAllDesc();
		for (Project project: list){
			projects.add(new ListItem(project));
		}
		model.addAttribute("projects", projects);
		return "proprog/list";
	}
	
	@RequestMapping(value = "/create")
	@Transactional
	public String create(ProjectCreateForm projectCreateForm) {
		return "proprog/create";
	}
	
	@RequestMapping(value = "/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String save(@Valid ProjectCreateForm projectCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			return "proprog/create";
		}

		projectCreateForm.setProjectNumber(projectCreateForm.getProjectNumber().toUpperCase());
		Project project = new Project();
		project.setProjectNumber(projectCreateForm.getProjectNumber().toUpperCase());
		project.setProjectNumberUser(userService.getAuthenticatedUser());
		project.setProjectNumberDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		
		return "redirect:/proprog/view/"+project.getId();
	}
	
	@RequestMapping(value = "/view/{id}")
	@Transactional
	public String view(@PathVariable("id") int id, Model model, Locale locale) throws NotFoundException {
		Project project = projectProgressService.findById(id);
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		model.addAttribute("project", project);
		model.addAttribute("progress", project.getProgressTotal());
		model.addAttribute("color", project.getProgressBootstrapTitle());
		return "proprog/view";
	}
	
	
}

/*
USER ROLES:

PROPROG_SALES
private User projectNumberUser;	
private User clientAcceptUser;
private User codificationUser;	
PROPROG_DRAWINGVALIDATION
private User drawingValidationUser;
PROPROG_SALESORDER
private User orderInputUser;
PROPROG_PRODUCTIONPLAN
private User productionPlanUser;
PROPROG_PURCHASE
private User buyPartsUser;
PROPROG_TECHNOLOGY
private User technologyUser;
PROPROG_TOOLDRAWING
private User toolDrawingUser;
PROPROG_TOOLCREATION
private User toolCreationUser
PROPROG_QUALITYCHECK
private User firstItemUser;
*/

package sbs.controller.proprog;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.controller.bhptickets.TicketCreateForm;
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
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+project.getId();
	}
	
	@RequestMapping(value = "/view/{id}")
	@Transactional
	public String view(@PathVariable("id") int id, Model model, Locale locale) throws NotFoundException {
		Project project = projectProgressService.findByIdEager(id);
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		model.addAttribute("project", project);
		model.addAttribute("progress", project.getProgressTotal());
		model.addAttribute("color", project.getProgressBootstrapTitle());
		model.addAttribute("checklistForm", new ChecklistForm(id));
		return "proprog/view";
	}
	
	@RequestMapping(value = "/confirm", params = { "clientAccept" }, method = RequestMethod.POST)
	@Transactional
	public String clientAccept(@RequestParam("clientAccept") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setClientAcceptUser(userService.getAuthenticatedUser());
		project.setClientAcceptDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
	
	@RequestMapping(value = "/confirm", params = { "codification" }, method = RequestMethod.POST)
	@Transactional
	public String codificationAccept(@RequestParam("codification") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setCodificationUser(userService.getAuthenticatedUser());
		project.setCodificationDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
	
	@RequestMapping(value = "/confirm", params = { "drawingValidation" }, method = RequestMethod.POST)
	@Transactional
	public String drawingAccept(@Valid ChecklistForm checklistForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
			// validate
		
		Project project = projectProgressService.findByIdEager(checklistForm.getId());
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		
		if (checklistForm.getDrawingNumber()==null || checklistForm.getDrawingNumber().length() < 3 || checklistForm.getDrawingNumber().length() > 30 ) {
			bindingResult.rejectValue("drawingNumber", "error.proprog.drawingnumber", "ERROR");
			model.addAttribute("project", project);
			model.addAttribute("progress", project.getProgressTotal());
			model.addAttribute("color", project.getProgressBootstrapTitle());
			return "proprog/view";
		}
		
		project.setDrawingValidationUser(userService.getAuthenticatedUser());
		project.setDrawingValidationDate(new Timestamp(new java.util.Date().getTime()));
		project.setDrawingNumber(checklistForm.getDrawingNumber());
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+project.getId();
	}
	
	@RequestMapping(value = "/confirm", params = { "salesOrder" }, method = RequestMethod.POST)
	@Transactional
	public String orderAccept(@Valid ChecklistForm checklistForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		// 
		
		Project project = projectProgressService.findByIdEager(checklistForm.getId());
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		
		if (checklistForm.getOrderNumber()==null || checklistForm.getOrderNumber().length() < 3 || checklistForm.getOrderNumber().length() > 30 ) {
			bindingResult.rejectValue("orderNumber", "error.proprog.ordernumber", "ERROR");
			model.addAttribute("project", project);
			model.addAttribute("progress", project.getProgressTotal());
			model.addAttribute("color", project.getProgressBootstrapTitle());
			return "proprog/view";
		}
		
		if (checklistForm.getOrderQuantity()==null || checklistForm.getOrderQuantity() < 1 ) {
			bindingResult.rejectValue("orderQuantity", "error.proprog.orderquantity", "ERROR");
			model.addAttribute("project", project);
			model.addAttribute("progress", project.getProgressTotal());
			model.addAttribute("color", project.getProgressBootstrapTitle());
			return "proprog/view";
		}
		
		project.setOrderInputUser(userService.getAuthenticatedUser());
		project.setOrderInputDate(new Timestamp(new java.util.Date().getTime()));
		project.setOrderNumber(checklistForm.getOrderNumber());
		project.setOrderQuantity(checklistForm.getOrderQuantity());
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+project.getId();
	}
	
	@RequestMapping(value = "/confirm", params = { "productionPlan" }, method = RequestMethod.POST)
	@Transactional
	public String productionPlanAccept(@RequestParam("productionPlan") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setProductionPlanUser(userService.getAuthenticatedUser());
		project.setProductionPlanDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
	
	@RequestMapping(value = "/confirm", params = { "buyParts" }, method = RequestMethod.POST)
	@Transactional
	public String buyPartsAccept(@RequestParam("buyParts") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setBuyPartsUser(userService.getAuthenticatedUser());
		project.setBuyPartsDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
	
	@RequestMapping(value = "/confirm", params = { "technology" }, method = RequestMethod.POST)
	@Transactional
	public String technologyAccept(@RequestParam("technology") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setTechnologyUser(userService.getAuthenticatedUser());
		project.setTechnologyDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
	
	@RequestMapping(value = "/confirm", params = { "toolDrawed" }, method = RequestMethod.POST)
	@Transactional
	public String toolDrawedAccept(@RequestParam("toolDrawed") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setToolDrawingUser(userService.getAuthenticatedUser());
		project.setToolDrawingDate(new Timestamp(new java.util.Date().getTime()));
		project.setToolDrawingNeeded(true);
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
		
	@RequestMapping(value = "/confirm", params = { "toolNotNeeded" }, method = RequestMethod.POST)
	@Transactional
	public String toolNotNeeded(@RequestParam("toolNotNeeded") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setToolDrawingUser(userService.getAuthenticatedUser());
		project.setToolDrawingDate(new Timestamp(new java.util.Date().getTime()));
		project.setToolDrawingNeeded(false);
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
	
	@RequestMapping(value = "/confirm", params = { "toolCreation" }, method = RequestMethod.POST)
	@Transactional
	public String toolCreationAccept(@RequestParam("toolCreation") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setToolCreationUser(userService.getAuthenticatedUser());
		project.setToolCreationDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
	}
	
	@RequestMapping(value = "/confirm", params = { "firstItem" }, method = RequestMethod.POST)
	@Transactional
	public String firstItemAccept(@RequestParam("firstItem") String id, RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{
		Project project = projectProgressService.findByIdEager(Integer.parseInt(id));
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		project.setFirstItemUser(userService.getAuthenticatedUser());
		project.setFirstItemDate(new Timestamp(new java.util.Date().getTime()));
		projectProgressService.saveOrUpdate(project);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/proprog/view/"+id;
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

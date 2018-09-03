package sbs.controller.tools;

import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;

import javassist.NotFoundException;
import sbs.controller.upload.UploadController;
import sbs.helpers.TextHelper;
import sbs.model.tools.ToolsProject;
import sbs.model.tools.ToolsProjectState;
import sbs.model.users.User;
import sbs.model.x3.X3Client;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3Workstation;
import sbs.service.mail.MailService;
import sbs.service.tools.ToolsProjectService;
import sbs.service.tools.ToolsProjectStateService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("tools")
public class ToolsController {
	@Autowired
	ToolsProjectService toolsProjectService;
	@Autowired
	ToolsProjectStateService toolsProjectStateService;
	@Autowired
	UserService userService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	MessageSource messageSource;
	@Autowired
	UploadController uploadController;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	TextHelper textHelper;

	@RequestMapping(value = "/dispatch")
	@Transactional
	public String dispatch(Model model) {
		List<User> toolsUsers = userService.findByRole("ROLE_TOOLSNORMALUSER");
		
		Map<String, List<ToolsProject>> map = new HashMap<>();
		for(User user: toolsUsers){
			map.put(user.getName(), toolsProjectService.findPendingToolsProjectsByUserDescByPriority(user));
		}
		
		model.addAttribute("userProjectMap", map);
		return "tools/dispatch";
	}
	

	
	@RequestMapping(value = "/createproject")
	@Transactional
	public String createProjectView(Model model) {
		model.addAttribute("toolsProjectCreateForm", new ToolsProjectCreateForm());
		return "tools/createproject";
	}
	
	@RequestMapping(value = "/createproject", method = RequestMethod.POST)
	@Transactional
	public String create(@Valid ToolsProjectCreateForm toolsProjectCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			return "tools/createproject";
		}

		// create project object
		ToolsProject project = new ToolsProject();
		
		// check depending data - client
		if(toolsProjectCreateForm.getClientCode().trim().length()>0){
			X3Client client = x3Service.findClientByCode("ATW", toolsProjectCreateForm.getClientCode().trim());
			if(client==null){
				bindingResult.rejectValue("clientCode", "error.client.not.found", "ERROR");
				return "tools/createproject";
			}
			else{
				project.setClientCode(client.getCode());
				project.setClientName(client.getName());
			}
		}
		else{
			project.setClientCode("");
			project.setClientName("");
		}
		
		// check depending data - asset
		if(toolsProjectCreateForm.getAssetCode().trim().length()>0){
			X3Product product = x3Service.findProductByCode("ATW", toolsProjectCreateForm.getAssetCode().trim());
			if(product==null){
				X3Workstation workstation = x3Service.findWorkstationByCode("ATW",toolsProjectCreateForm.getAssetCode().trim());
				if(workstation==null){
					bindingResult.rejectValue("assetCode", "error.asset.not.found", "ERROR");
					return "tools/createproject";
				}
				else{
					project.setAssetCode(workstation.getCode());
					project.setAssetName(workstation.getName());
				}
			}
			else{
				project.setAssetCode(product.getCode());
				project.setAssetName(product.getDescription());
			}
		}
		else{
			project.setAssetCode("");
			project.setAssetName("");
		}

		// fields
		Timestamp credat = new Timestamp(new java.util.Date().getTime());
		project.setCreationDate(credat);
		project.setUpdateDate(credat);
		project.setCechOld(toolsProjectCreateForm.getCechOld().toUpperCase().trim());
		project.setCechNew(toolsProjectCreateForm.getCechNew().toUpperCase().trim());
		project.setDescription(toolsProjectCreateForm.getDescription().trim());
		
		// relations
		// state
		ToolsProjectState state = toolsProjectStateService.findByOrder(10);
		state.getToolsProjects().add(project);
		project.setState(state);
		// creator
		User creator = userService.getAuthenticatedUser();
		creator.getCreatedToolsProjects().add(project);
		project.setCreator(creator);
		project.setPriority(1);
		
		// save
		toolsProjectService.save(project);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/editproject/" + project.getId();
	}
	
	@RequestMapping("/showproject/{id}")
	@Transactional
	public String showProject(@PathVariable("id") int id, Model model) throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(id);
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		boolean isAssigned;
		
		if(project.getAssignedUser() != null){
			isAssigned = userService.getAuthenticatedUser().getId() == project.getAssignedUser().getId();
		}
		else{
			isAssigned = false;
		}
		model.addAttribute("isAssigned", isAssigned );
		
		model.addAttribute("project", project);
		return "tools/showproject";
	}
	
	@RequestMapping("/editproject/{id}")
	@Transactional
	public String editProjectForm(@PathVariable("id") int id, Model model) throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(id);
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		ToolsProjectCreateForm toolsProjectCreateForm = new ToolsProjectCreateForm();
		toolsProjectCreateForm.setState(project.getState().getDescription());
		toolsProjectCreateForm.setId(project.getId());
		toolsProjectCreateForm.setCechOld(project.getCechOld());
		toolsProjectCreateForm.setCechNew(project.getCechNew());
		toolsProjectCreateForm.setAssetCode(project.getAssetCode());
		toolsProjectCreateForm.setAssetName(project.getAssetName());
		toolsProjectCreateForm.setClientCode(project.getClientCode());
		toolsProjectCreateForm.setClientName(project.getClientName());
		toolsProjectCreateForm.setDescription(project.getDescription());
		toolsProjectCreateForm.setCreationDate(project.getCreationDate());
		toolsProjectCreateForm.setUpdateDate(project.getUpdateDate());
		toolsProjectCreateForm.setCreator(project.getCreator().getName());
		toolsProjectCreateForm.setThumbnailFileName(project.getThumbnailFileName());
		toolsProjectCreateForm.setPriority(project.getPriority());
		if(project.getAssignedUser()!=null){
			toolsProjectCreateForm.setAssignedUser(project.getAssignedUser().getId());
		}
		else{
			toolsProjectCreateForm.setAssignedUser(0L);
		}

		model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
		model.addAttribute("toolsProjectCreateForm", toolsProjectCreateForm);
		return "tools/editproject";
	}
	
	private void repeatConstants(ToolsProject project, ToolsProjectCreateForm form){
		form.setCreationDate(project.getCreationDate());
		form.setUpdateDate(project.getCreationDate());
		form.setAssetName(project.getAssetName());
		form.setClientName(project.getClientName());
	}

	@RequestMapping(value = "/projectaction", params = { "markAsDone" }, method = RequestMethod.POST)
	@Transactional
	public String markAsDone(@RequestParam String markAsDone, RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException{
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(markAsDone));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		
		// state: done - to accept
		ToolsProjectState state = toolsProjectStateService.findByOrder(40);
		state.getToolsProjects().add(project);
		project.setState(state);
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));		
		return "redirect:/tools/showproject/" + project.getId();
	}
	
	@RequestMapping(value = "/projectaction", params = { "hold" }, method = RequestMethod.POST)
	@Transactional
	public String hold(@RequestParam String hold, RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException{
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(hold));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		
		// state: hold
		ToolsProjectState state = toolsProjectStateService.findByOrder(30);
		state.getToolsProjects().add(project);
		project.setState(state);
		
		// assigned user: null
		if(project.getAssignedUser()!=null){
			project.getAssignedUser().getAssignedToolsProjects().remove(project);
			project.setAssignedUser(null);
		}
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));		
		return "redirect:/tools/showproject/" + project.getId();
	}
	
	@RequestMapping(value = "/projectaction", params = { "cancel" }, method = RequestMethod.POST)
	@Transactional
	public String cancel(@RequestParam String cancel, RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException{
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(cancel));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		
		// state: cancel
		ToolsProjectState state = toolsProjectStateService.findByOrder(90);
		state.getToolsProjects().add(project);
		project.setState(state);
		
		// assigned user: null
		if(project.getAssignedUser()!=null){
			project.getAssignedUser().getAssignedToolsProjects().remove(project);
			project.setAssignedUser(null);
		}
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));		
		return "redirect:/tools/showproject/" + project.getId();
	}
	
	@RequestMapping(value = "/projectaction", params = { "approve" }, method = RequestMethod.POST)
	@Transactional
	public String approve(@RequestParam String approve, RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException{
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(approve));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		
		// state: cancel
		ToolsProjectState state = toolsProjectStateService.findByOrder(50);
		state.getToolsProjects().add(project);
		project.setState(state);
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));		
		return "redirect:/tools/showproject/" + project.getId();
	}
	
	@RequestMapping(value = "/editproject", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String edit(@Valid ToolsProjectCreateForm toolsProjectCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException{

		// get project
		ToolsProject project = toolsProjectService.findById(toolsProjectCreateForm.getId());
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		
		// validate
		if (bindingResult.hasErrors()) {
			repeatConstants(project, toolsProjectCreateForm);
			model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
			return "tools/editproject";
		}

		// assigned user
		if(toolsProjectCreateForm.getAssignedUser() > 0 ){
			User newAssignedUser = userService.findById(toolsProjectCreateForm.getAssignedUser());
			if (newAssignedUser == null) {
				bindingResult.rejectValue("assignedUser", "error.user.not.found", "ERROR");
				repeatConstants(project, toolsProjectCreateForm);
				model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
				return "tools/editproject";
			} else if (project.getAssignedUser() == null
					|| project.getAssignedUser().getId() != newAssignedUser.getId()) {
				newAssignedUser.getAssignedToolsProjects().add(project);
				project.setAssignedUser(newAssignedUser);
				// state
				ToolsProjectState state = toolsProjectStateService.findByOrder(20);
				state.getToolsProjects().add(project);
				project.setState(state);
			}
		}
		else if(project.getState().getOrder() == 20){
			ToolsProjectState state = toolsProjectStateService.findByOrder(10);
			state.getToolsProjects().add(project);
			project.setState(state);
			project.getAssignedUser().getAssignedToolsProjects().remove(project);
			project.setAssignedUser(null);
		}
		
		// check depending data - client
		if(!toolsProjectCreateForm.getClientCode().trim().toUpperCase().equals(project.getClientCode())){
			X3Client client = x3Service.findClientByCode("ATW", toolsProjectCreateForm.getClientCode().trim());
			if(client==null){
				bindingResult.rejectValue("clientCode", "error.client.not.found", "ERROR");
				repeatConstants(project, toolsProjectCreateForm);
				model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
				return "tools/editproject";
			}
			else{
				project.setClientCode(client.getCode());
				project.setClientName(client.getName());
			}
		}
		
		// check depending data - asset
		if(!toolsProjectCreateForm.getAssetCode().trim().toUpperCase().equals(project.getAssetCode())){
			X3Product product = x3Service.findProductByCode("ATW", toolsProjectCreateForm.getAssetCode().trim());
			if(product==null){
				X3Workstation workstation = x3Service.findWorkstationByCode("ATW",toolsProjectCreateForm.getAssetCode().trim());
				if(workstation==null){
					bindingResult.rejectValue("assetCode", "error.asset.not.found", "ERROR");
					repeatConstants(project, toolsProjectCreateForm);
					model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
					return "tools/editproject";
				}
				else{
					project.setAssetCode(workstation.getCode());
					project.setAssetName(workstation.getName());
				}
			}
			else{
				project.setAssetCode(product.getCode());
				project.setAssetName(product.getDescription());
			}
		}
		
		// fields
		Timestamp upddat = new Timestamp(new java.util.Date().getTime());
		project.setUpdateDate(upddat);
		project.setCechOld(toolsProjectCreateForm.getCechOld().trim().toUpperCase());
		project.setCechNew(toolsProjectCreateForm.getCechNew().trim().toUpperCase());
		project.setDescription(toolsProjectCreateForm.getDescription().trim());
		project.setPriority(toolsProjectCreateForm.getPriority());

		// save
		toolsProjectService.save(project);
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));		
		return "redirect:/tools/editproject/" + project.getId();
	}
	
	
	@RequestMapping(value = "/pendinglist")
	@Transactional
	public String managerView(Model model, Locale locale) {
		
		List<ToolsProject> list = toolsProjectService.findAllPendingToolsProjects();
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.pending", null, locale));
		
		return "tools/dispatch";
	}
	
	@RequestMapping(value = "/closedlist")
	@Transactional
	public String userView(Model model, Locale locale) {
		
		List<ToolsProject> list = toolsProjectService.findClosedToolsProjects();
		model.addAttribute("list", list);		
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.closed", null, locale));
		
		return "tools/dispatch";
	}

}

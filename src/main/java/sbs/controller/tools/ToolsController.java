package sbs.controller.tools;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
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
import org.thymeleaf.context.Context;

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
		for (User user : toolsUsers) {
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

		// cech old
		if (toolsProjectCreateForm.getCechOld().trim().length() > 0) {
			if (toolsProjectService.isCechOldInUse(toolsProjectCreateForm.getCechOld().trim())) {
				bindingResult.rejectValue("cechOld", "tools.error.cech.busy", "ERROR");
				return "tools/createproject";
			} else {
				project.setCechOld(toolsProjectCreateForm.getCechOld().trim().toUpperCase());
			}
		}

		// cech new
		if (toolsProjectCreateForm.getCechNew().trim().length() > 0) {
			if (toolsProjectService.isCechNewInUse(toolsProjectCreateForm.getCechNew().trim())) {
				bindingResult.rejectValue("cechNew", "tools.error.cech.busy", "ERROR");
				return "tools/createproject";
			} else {
				project.setCechNew(toolsProjectCreateForm.getCechNew().trim().toUpperCase());
			}
		}

		// check depending data - client
		if (toolsProjectCreateForm.getClientCode().trim().length() > 0) {
			X3Client client = x3Service.findClientByCode("ATW", toolsProjectCreateForm.getClientCode().trim());
			if (client == null) {
				bindingResult.rejectValue("clientCode", "error.client.not.found", "ERROR");
				return "tools/createproject";
			} else {
				project.setClientCode(client.getCode());
				project.setClientName(client.getName());
			}
		} else {
			project.setClientCode("");
			project.setClientName("");
		}

		// check depending data - asset
		if (toolsProjectCreateForm.getAssetCode().trim().length() > 0) {
			X3Product product = x3Service.findProductByCode("ATW", toolsProjectCreateForm.getAssetCode().trim());
			if (product == null) {
				X3Workstation workstation = x3Service.findWorkstationByCode("ATW",
						toolsProjectCreateForm.getAssetCode().trim());
				if (workstation == null) {
					bindingResult.rejectValue("assetCode", "error.asset.not.found", "ERROR");
					return "tools/createproject";
				} else {
					project.setAssetCode(workstation.getCode());
					project.setAssetName(workstation.getName());
				}
			} else {
				project.setAssetCode(product.getCode());
				project.setAssetName(product.getDescription());
			}
		} else {
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
		project.setPhotoName("noimage.png");

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
		if (project.getAssignedUser() != null) {
			isAssigned = userService.getAuthenticatedUser().getId() == project.getAssignedUser().getId();
		} else {
			isAssigned = false;
		}
		model.addAttribute("isAssigned", isAssigned);
		model.addAttribute("project", project);
		return "tools/showproject";
	}

	/*private String getPhotoFilenameForProjectById(int id) {
		ArrayList<String> fileList = uploadController.listFiles(uploadController.getToolsPhotoPath());
		for (int i = fileList.size() - 1; i >= 0; i--) {
			if (fileList.get(i).startsWith("tool_" + id + "_")) {
				return fileList.get(i);
			}
		}
		return "noimage.png";
	}*/

	@RequestMapping("/edit/photos/{id}")
	@Transactional
	public String showToolsPhotosForm(@PathVariable("id") int id, Model model) throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(id);
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		model.addAttribute("project", project);
		return "tools/photos";
	}
	
	@RequestMapping("/edit/pdf/{id}")
	@Transactional
	public String showToolsPdfForm(@PathVariable("id") int id, Model model) throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(id);
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		model.addAttribute("project", project);
		return "tools/setpdf";
	}
	
	
	@RequestMapping(value = ("/edit/pdf/{id}"),  method = RequestMethod.POST)
	public String setPdfPath(@PathVariable("id") int id, @RequestParam String pdfpath, RedirectAttributes redirectAttrs,
			Locale locale) throws NotFoundException {
		// not empty
		if (pdfpath.isEmpty()) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("tools.error.missingfilename", null, locale));
			return "redirect:/tools/edit/pdf/" + id;
		}
		// exist
		File file = new File(pdfpath);
		if (!file.exists()) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("tools.error.fileinaccessibile", null, locale) + ": " + pdfpath);
			return "redirect:/tools/edit/pdf/" + id;
		}
		// project
		ToolsProject project = toolsProjectService.findById(id);
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		
		project.setPdfUrl(pdfpath);
		toolsProjectService.update(project);
		
			// say ok
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale) +": " + pdfpath);

		return "redirect:/tools/showproject/" + id;
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
		if (project.getAssignedUser() != null) {
			toolsProjectCreateForm.setAssignedUser(project.getAssignedUser().getId());
			toolsProjectCreateForm.setAssigned(userService.getAuthenticatedUser().getId() == project.getAssignedUser().getId());
		} else {
			toolsProjectCreateForm.setAssignedUser(0L);
			toolsProjectCreateForm.setAssigned(false);
		}
		model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
		model.addAttribute("toolsProjectCreateForm", toolsProjectCreateForm);
		return "tools/editproject";
	}

	private void repeatConstants(ToolsProject project, ToolsProjectCreateForm form, boolean isAssigned) {
		form.setCreationDate(project.getCreationDate());
		form.setUpdateDate(project.getCreationDate());
		form.setAssetName(project.getAssetName());
		form.setClientName(project.getClientName());
		form.assigned = isAssigned;
	}

	@RequestMapping(value = "/projectaction", params = { "markAsDone" }, method = RequestMethod.POST)
	@Transactional
	public String markAsDone(@RequestParam String markAsDone, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(markAsDone));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		// state: done - to accept
		ToolsProjectState state = toolsProjectStateService.findByOrder(40);
		state.getToolsProjects().add(project);
		project.setState(state);

		// notify by mail
		try {
			mailNotifyAsDone(project);
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("notification.mail.send.error", null, locale) + ": " + e.getMessage());
		}

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	private void mailNotifyAsDone(ToolsProject project) throws UnknownHostException, MessagingException {
		List<User> managerList = userService.findByAnyRole(new String[] { "ROLE_TOOLSMANAGER" });
		ArrayList<String> addressMailingList = new ArrayList<>();
		for (User sprv : managerList) {
			addressMailingList.add(sprv.getEmail());
		}
		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("project", project);
		String body = templateEngine.process("tools/mail.asdone", context);
		String[] self = {userService.getAuthenticatedUser().getEmail()};
		mailService.sendEmail("webapp@atwsystem.pl", addressMailingList.toArray(new String[0]), self,
				"ADR Polska S.A. - Projekt do zatwierdzenia", body);
	}
	
	private void mailNotifyProduced(ToolsProject project) throws UnknownHostException, MessagingException {
		List<User> managerList = userService.findByAnyRole(new String[] { "ROLE_TOOLSMANAGER", "ROLE_TOOLSMAILINGUSER" });
		Set<String> addressMailingList = new HashSet<>();
		for (User sprv : managerList) {
			addressMailingList.add(sprv.getEmail());
		}
		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("project", project);
		String body = templateEngine.process("tools/mail.produced", context);
		String[] self = {userService.getAuthenticatedUser().getEmail()};
		mailService.sendEmail("webapp@atwsystem.pl", addressMailingList.toArray(new String[0]), self,
				"ADR Polska S.A. - Zakończono produkcję narzędzia", body);
	}
	
	private void mailNotifyToProduce(ToolsProject project) throws UnknownHostException, MessagingException {
		List<User> managerList = userService.findByAnyRole(new String[] { "ROLE_TOOLSMANAGER", "ROLE_TOOLSMAILINGUSER", "ROLE_TOOLSPRODMANAGER" });
		Set<String> addressMailingList = new HashSet<>();
		for (User sprv : managerList) {
			addressMailingList.add(sprv.getEmail());
		}
		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("project", project);
		String body = templateEngine.process("tools/mail.toproduce", context);
		String[] self = {userService.getAuthenticatedUser().getEmail()};
		mailService.sendEmail("webapp@atwsystem.pl", addressMailingList.toArray(new String[0]), self,
				"ADR Polska S.A. - Zlecenie wykonania przyrządu", body);
	}
	
	private void mailNotifyToProduceOutside(ToolsProject project) throws UnknownHostException, MessagingException {
		List<User> managerList = userService.findByAnyRole(new String[] { "ROLE_TOOLSMANAGER", "ROLE_TOOLSMAILINGUSER", "ROLE_TOOLSPRODMANAGER" });
		Set<String> addressMailingList = new HashSet<>();
		for (User sprv : managerList) {
			addressMailingList.add(sprv.getEmail());
		}
		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("project", project);
		String body = templateEngine.process("tools/mail.toproduceoutside", context);
		String[] self = {userService.getAuthenticatedUser().getEmail()};
		mailService.sendEmail("webapp@atwsystem.pl", addressMailingList.toArray(new String[0]), self,
				"ADR Polska S.A. - Zewnętrzne zlecenie wykonania przyrządu", body);
	}

	private void mailNotifyAssigned(ToolsProject project) throws UnknownHostException, MessagingException {

		ArrayList<String> addressMailingList = new ArrayList<>();
		addressMailingList.add(project.getAssignedUser().getEmail());

		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("project", project);
		String body = templateEngine.process("tools/mail.assigned", context);
		String[] self = {userService.getAuthenticatedUser().getEmail()};
		mailService.sendEmail("webapp@atwsystem.pl", addressMailingList.toArray(new String[0]), self,
				"ADR Polska S.A. - Przypisano nowy projekt", body);
	}

	@RequestMapping(value = "/projectaction", params = { "hold" }, method = RequestMethod.POST)
	@Transactional
	public String hold(@RequestParam String hold, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(hold));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		// state: hold
		ToolsProjectState state = toolsProjectStateService.findByOrder(30);
		state.getToolsProjects().add(project);
		project.setState(state);

		// assigned user: null
		if (project.getAssignedUser() != null) {
			project.getAssignedUser().getAssignedToolsProjects().remove(project);
			project.setAssignedUser(null);
		}

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	@RequestMapping(value = "/projectaction", params = { "cancel" }, method = RequestMethod.POST)
	@Transactional
	public String cancel(@RequestParam String cancel, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(cancel));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		// state: cancel
		ToolsProjectState state = toolsProjectStateService.findByOrder(90);
		state.getToolsProjects().add(project);
		project.setState(state);

		// assigned user: null
		if (project.getAssignedUser() != null) {
			project.getAssignedUser().getAssignedToolsProjects().remove(project);
			project.setAssignedUser(null);
		}

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	@RequestMapping(value = "/projectaction", params = { "approve" }, method = RequestMethod.POST)
	@Transactional
	public String approve(@RequestParam String approve, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {
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

	@RequestMapping(value = "/projectaction", params = { "delegateadr" }, method = RequestMethod.POST)
	@Transactional
	public String delegateAdr(@RequestParam String delegateadr, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(delegateadr));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		// state: cancel
		ToolsProjectState state = toolsProjectStateService.findByOrder(60);
		state.getToolsProjects().add(project);
		project.setState(state);

		// mail notify
		try {
			mailNotifyToProduce(project);
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("notification.mail.send.error", null, locale) + ": " + e.getMessage());
		}
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	@RequestMapping(value = "/projectaction", params = { "delegateoutside" }, method = RequestMethod.POST)
	@Transactional
	public String delegateOutside(@RequestParam String delegateoutside, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(delegateoutside));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		// state: cancel
		ToolsProjectState state = toolsProjectStateService.findByOrder(61);
		state.getToolsProjects().add(project);
		project.setState(state);

		// mail notify
		try {
			mailNotifyToProduceOutside(project);
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("notification.mail.send.error", null, locale) + ": " + e.getMessage());
		}
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	@RequestMapping(value = "/projectaction", params = { "chgdiff" }, method = RequestMethod.POST)
	@Transactional
	public String test(@RequestParam String chgdiff, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {

		int id = Integer.parseInt(chgdiff.split(";")[0]);
		int diff = Integer.parseInt(chgdiff.split(";")[1]);

		ToolsProject project = toolsProjectService.findById(id);
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		project.setDifficulty(diff);
		toolsProjectService.save(project);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	@RequestMapping(value = "/projectaction", params = { "startproduction" }, method = RequestMethod.POST)
	@Transactional
	public String startProduction(@RequestParam String startproduction, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {

		int max = 35;
		ToolsProject project = toolsProjectService.findById(Integer.parseInt(startproduction));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		List<ToolsProject> list = toolsProjectService.findToolsProjectsByStateOrder(70);
		if (list.size() >= max) {
			redirectAttrs.addFlashAttribute("error",
					messageSource.getMessage("tools.error.max.tools.started", null, locale) + ": " + max);
			return "redirect:/tools/showproject/" + project.getId();
		}

		// state: in production
		ToolsProjectState state = toolsProjectStateService.findByOrder(70);
		state.getToolsProjects().add(project);
		project.setState(state);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	@RequestMapping(value = "/projectaction", params = { "produced" }, method = RequestMethod.POST)
	@Transactional
	public String produced(@RequestParam String produced, RedirectAttributes redirectAttrs, Locale locale)
			throws NotFoundException {

		ToolsProject project = toolsProjectService.findById(Integer.parseInt(produced));
		if (project == null) {
			throw new NotFoundException("Project not found");
		}

		// state: produced
		ToolsProjectState state = toolsProjectStateService.findByOrder(80);
		state.getToolsProjects().add(project);
		project.setState(state);

		// mail notify
		try {
			mailNotifyProduced(project);
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("notification.mail.send.error", null, locale) + ": " + e.getMessage());
		}
		
		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/showproject/" + project.getId();
	}

	@RequestMapping(value = "/editproject", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String edit(@Valid ToolsProjectCreateForm toolsProjectCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		boolean notifyAssignedUser = false;

		// get project
		ToolsProject project = toolsProjectService.findById(toolsProjectCreateForm.getId());
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

		// validate
		if (bindingResult.hasErrors()) {
			repeatConstants(project, toolsProjectCreateForm, isAssigned);
			model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
			return "tools/editproject";
		}

		// assigned user
		if (toolsProjectCreateForm.getAssignedUser() > 0) {
			User newAssignedUser = userService.findById(toolsProjectCreateForm.getAssignedUser());
			if (newAssignedUser == null) {
				bindingResult.rejectValue("assignedUser", "error.user.not.found", "ERROR");
				repeatConstants(project, toolsProjectCreateForm, isAssigned);
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
				notifyAssignedUser = true;
			}
		} else if (project.getState().getOrder() == 20) {
			ToolsProjectState state = toolsProjectStateService.findByOrder(10);
			state.getToolsProjects().add(project);
			project.setState(state);
			project.getAssignedUser().getAssignedToolsProjects().remove(project);
			project.setAssignedUser(null);
		}

		// check depending data - client
		if (toolsProjectCreateForm.getClientCode().trim().length() == 0) {
			project.setClientCode("");
			project.setClientName("");
		} else if (!toolsProjectCreateForm.getClientCode().trim().toUpperCase().equals(project.getClientCode())) {
			X3Client client = x3Service.findClientByCode("ATW", toolsProjectCreateForm.getClientCode().trim());
			if (client == null) {
				bindingResult.rejectValue("clientCode", "error.client.not.found", "ERROR");
				repeatConstants(project, toolsProjectCreateForm, isAssigned);
				model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
				return "tools/editproject";
			} else {
				project.setClientCode(client.getCode());
				project.setClientName(client.getName());
			}

		}

		// check depending data - asset
		if (toolsProjectCreateForm.getAssetCode().trim().length() == 0) {
			project.setAssetCode("");
			project.setAssetName("");
		} else if (!toolsProjectCreateForm.getAssetCode().trim().toUpperCase().equals(project.getAssetCode())) {
			X3Product product = x3Service.findProductByCode("ATW", toolsProjectCreateForm.getAssetCode().trim());
			if (product == null) {
				X3Workstation workstation = x3Service.findWorkstationByCode("ATW",
						toolsProjectCreateForm.getAssetCode().trim());
				if (workstation == null) {
					bindingResult.rejectValue("assetCode", "error.asset.not.found", "ERROR");
					repeatConstants(project, toolsProjectCreateForm, isAssigned);
					model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
					return "tools/editproject";
				} else {
					project.setAssetCode(workstation.getCode());
					project.setAssetName(workstation.getName());
				}
			} else {
				project.setAssetCode(product.getCode());
				project.setAssetName(product.getDescription());
			}
		}

		// cech old
		if (toolsProjectCreateForm.getCechOld().trim().length() == 0) {
			project.setCechOld("");
		} else if (!toolsProjectCreateForm.getCechOld().trim().toUpperCase().equals(project.getCechOld())) {
			if (toolsProjectService.isCechOldInUse(toolsProjectCreateForm.getCechOld().trim())) {
				bindingResult.rejectValue("cechOld", "tools.error.cech.busy", "ERROR");
				repeatConstants(project, toolsProjectCreateForm, isAssigned);
				model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
				return "tools/editproject";
			} else {
				project.setCechOld(toolsProjectCreateForm.getCechOld().trim().toUpperCase());
			}
		}

		// cech new
		if (toolsProjectCreateForm.getCechNew().trim().length() == 0) {
			project.setCechNew("");
		} else if (!toolsProjectCreateForm.getCechNew().trim().toUpperCase().equals(project.getCechNew())) {
			if (toolsProjectService.isCechNewInUse(toolsProjectCreateForm.getCechNew().trim())) {
				bindingResult.rejectValue("cechNew", "tools.error.cech.busy", "ERROR");
				repeatConstants(project, toolsProjectCreateForm, isAssigned);
				model.addAttribute("toolsUsers", userService.findByRole("ROLE_TOOLSNORMALUSER"));
				return "tools/editproject";
			} else {
				project.setCechNew(toolsProjectCreateForm.getCechNew().trim().toUpperCase());
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

		// notify by mail
		if (notifyAssignedUser) {
			try {
				mailNotifyAssigned(project);
			} catch (Exception e) {
				redirectAttrs.addFlashAttribute("warning",
						messageSource.getMessage("notification.mail.send.error", null, locale) + ": " + e.getMessage());
			}
		}

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/tools/editproject/" + project.getId();
	}

	@RequestMapping(value = "/alllist")
	@Transactional
	public String allView(Model model, Locale locale) {
		
		List<ToolsProject> list = toolsProjectService.findAllToolsProjects();
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.all", null, locale));
		
		return "tools/dispatch";
	}
	
	@RequestMapping(value = "/pendinglist")
	@Transactional
	public String managerView(Model model, Locale locale) {

		List<ToolsProject> list = toolsProjectService.findAllPendingToolsProjects();
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.pending", null, locale));

		return "tools/dispatch";
	}

	@RequestMapping(value = "/acceptedlist")
	@Transactional
	public String acceptedView(Model model, Locale locale) {

		List<ToolsProject> list = toolsProjectService.findToolsProjectsByStateOrder(50);
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.accepted", null, locale));

		return "tools/dispatch";
	}

	@RequestMapping(value = "/fwadrlist")
	@Transactional
	public String fwAdrView(Model model, Locale locale) {

		List<ToolsProject> list = toolsProjectService.findToolsProjectsByStateOrder(60);
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.forwarded.adr", null, locale));

		return "tools/dispatch";
	}

	@RequestMapping(value = "/fwoutsidelist")
	@Transactional
	public String fwOutsideView(Model model, Locale locale) {

		List<ToolsProject> list = toolsProjectService.findToolsProjectsByStateOrder(61);
		model.addAttribute("list", list);
		model.addAttribute("listtitle",
				messageSource.getMessage("tools.projects.list.forwarded.outside", null, locale));

		return "tools/dispatch";
	}

	@RequestMapping(value = "/inproductionlist")
	@Transactional
	public String inProductionView(Model model, Locale locale) {

		List<ToolsProject> list = toolsProjectService.findToolsProjectsByStateOrder(70);
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.inproduction", null, locale));

		return "tools/dispatch";
	}

	@RequestMapping(value = "/producedlist")
	@Transactional
	public String producedView(Model model, Locale locale) {

		List<ToolsProject> list = toolsProjectService.findToolsProjectsByStateOrder(80);
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.produced", null, locale));

		return "tools/dispatch";
	}

	@RequestMapping(value = "/cancelledlist")
	@Transactional
	public String cancelledView(Model model, Locale locale) {

		List<ToolsProject> list = toolsProjectService.findToolsProjectsByStateOrder(90);
		model.addAttribute("list", list);
		model.addAttribute("listtitle", messageSource.getMessage("tools.projects.list.cancelled", null, locale));

		return "tools/dispatch";
	}

}

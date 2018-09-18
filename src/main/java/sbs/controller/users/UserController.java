package sbs.controller.users;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javassist.NotFoundException;
import sbs.model.tools.ToolsProject;
import sbs.model.users.Role;
import sbs.model.users.User;
import sbs.service.mail.MailService;
import sbs.service.users.RoleService;
import sbs.service.users.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	UsersCriteriaHolder criteriaHolder;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	
	@ModelAttribute("availableRoles")
	public List<Role> getAvailableRoles(){
	    return roleService.findAll();
	}
	
	/**
	 * show existing profile
	 * @param model
	 * @return
	 */
	@RequestMapping("/list/{crit}")
	public String showUsersList(@PathVariable("crit") String crit, Model model) {
		
		if(criteriaHolder.getFindRange()==null){
			criteriaHolder.setFindRange(UserService.ACTIVE_USERS);
		}
		if (criteriaHolder.getSortOrder()==null){
			criteriaHolder.setSortOrder(UserService.SORT_ORDER_USERNAME);
		}
		switch (crit){
			case UserService.ACTIVE_USERS:
				criteriaHolder.setFindRange(UserService.ACTIVE_USERS);
				break;
			case UserService.INACTIVE_USERS:
				criteriaHolder.setFindRange(UserService.INACTIVE_USERS);
				break;
			case UserService.ALL_USERS:
				criteriaHolder.setFindRange(UserService.ALL_USERS);
				break;
			case UserService.SORT_ORDER_ID:
				criteriaHolder.setSortOrder(UserService.SORT_ORDER_ID);
				break;
			case UserService.SORT_ORDER_USERNAME:
				criteriaHolder.setSortOrder(UserService.SORT_ORDER_USERNAME);
				break;
			default:
				break;
		}
		model.addAttribute("users", userService.find(criteriaHolder.getFindRange(), criteriaHolder.getSortOrder()));
		return "users/list";
	}
	
	
	@RequestMapping("/showcurrent")
	public String showCurrentProfile(Model model) {
		model.addAttribute("user",userService.getAuthenticatedUser());
		return "users/show";
	}
	@RequestMapping("/show/{id}")
	public String showProfile(@PathVariable("id") long id, Model model) {
		model.addAttribute("user",userService.findById(id));
		return "users/show";
	}
	
	@RequestMapping("/edit/{id}")
	@Transactional
	public String showUserCreateForm(@PathVariable("id") long id, Model model) throws NotFoundException{
		UserEditForm userEditForm = new UserEditForm();
		User user = userService.findById(id);
		if(user == null){
			throw new NotFoundException("user not found");
		}
		userEditForm.setId(user.getId());
		userEditForm.setUsername(user.getUsername());
		userEditForm.setName(user.getName());
		userEditForm.setEmail(user.getEmail());
		userEditForm.setActive(user.isActive());
		userEditForm.setRoles(user.getRoles());
		userEditForm.setAvatarFileName(user.getAvatarfilename());
		model.addAttribute("userEditForm",userEditForm);
		return "users/edit";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveUser(@Valid UserEditForm userEditForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Locale locale, Model model){
		User modelUser = userService.findById(userEditForm.getId());
		if(!modelUser.getUsername().equals(userEditForm.getUsername())){
			if (userService.findByUsername(userEditForm.getUsername())!= null){
				bindingResult.rejectValue("username", "error.user.already.exist", "ERROR");
			}
		}
		if(bindingResult.hasErrors()){
			userEditForm.setRoles(userService.findById(userEditForm.getId()).getRoles());
			return "users/edit";
		}
		modelUser.setUsername(userEditForm.getUsername());
		modelUser.setName(userEditForm.getName());
		modelUser.setEmail(userEditForm.getEmail());
		modelUser.setActive(userEditForm.getActive());
		userService.update(modelUser);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/users/edit/" + userEditForm.getId();
	}
	
	@RequestMapping("/changepassword/{id}")
	@Transactional
	public String showChangePass(@PathVariable("id") long id, Model model) throws NotFoundException{
		User user = userService.findById(id);
		if(user == null){
			throw new NotFoundException("user not found");
		}
		UserPasswordForm userPasswordForm = new UserPasswordForm();
		userPasswordForm.setId(user.getId());
		userPasswordForm.setUsername(user.getUsername());
		
		model.addAttribute("userPasswordForm", userPasswordForm);
		return "users/changepassword";
	}
	
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public String changePass(@Valid UserPasswordForm userPasswordForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Locale locale, Model model){
		if (!userPasswordForm.getNewPassword().equals(userPasswordForm.getRepeatPassword())){
			bindingResult.rejectValue("repeatPassword", "error.password.repeat", "ERROR");
		}
		if(bindingResult.hasErrors()){
			return "users/changepassword";
		}
		
		User user = userService.findById(userPasswordForm.getId());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(userPasswordForm.getNewPassword());
		user.setPassword(hashedPassword);
		userService.update(user);
		
		
		try {
			mailNotifyPassword(user, userPasswordForm.getNewPassword());
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("warning",
					messageSource.getMessage("notification.mail.send.error", null, locale) + ": " + e.getMessage());
		}
		
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.password.changed", null, locale));
		return "redirect:/users/edit/" + userPasswordForm.getId();
	}
	
	private void mailNotifyPassword(User user, String password) throws UnknownHostException, MessagingException {
		List<User> ccList = userService.findByAnyRole(new String[] { "ROLE_ADMIN" });
		ArrayList<String> addressCCList = new ArrayList<>();
		for (User sprv : ccList) {
			addressCCList.add(sprv.getEmail());
		}
		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("user", user);
		context.setVariable("pass", password);
		
		String body = templateEngine.process("users/mail.passwordset", context);
		String[] to = {user.getEmail()};
		mailService.sendEmail("webapp@atwsystem.pl", to, addressCCList.toArray(new String[0]),
				"ADR Polska S.A. - Dane do logowania w aplikacij webowej", body);
	}
	

	@RequestMapping(value = "/manageroles/{id}", params = { "add" }, method = RequestMethod.POST)
	@Transactional
	public String addRole(@PathVariable("id") long userId, RedirectAttributes redirectAttrs, HttpServletRequest req,
			Locale locale) {
		long roleId = Long.valueOf(req.getParameter("add"));
		Role role = roleService.findById(roleId);
		User user = userService.findById(userId);
		user.getRoles().add(role);
		role.getUsers().add(user);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/users/edit/" + userId;
	}
	
	@RequestMapping(value = "/manageroles/{id}", params = { "remove" }, method = RequestMethod.POST)
	@Transactional
	public String removeRole(@PathVariable("id") long userId, RedirectAttributes redirectAttrs, HttpServletRequest req,
			Locale locale) {
		long roleId = Long.valueOf(req.getParameter("remove"));
		Role role = roleService.findById(roleId);
		User user = userService.findById(userId);
		user.getRoles().remove(role);
		role.getUsers().remove(user);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/users/edit/" + userId;
	}
	
	
	@RequestMapping("/create")
	@Transactional
	public String showCreate(UserCreateForm userCreateForm) throws NotFoundException{
		return "users/create";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional
	public String createUser(@Valid UserCreateForm userCreateForm, BindingResult bindingResult,  RedirectAttributes redirectAttrs, Locale locale, Model model){
		User user = userService.findByUsername(userCreateForm.getUsername());
			if (user != null){
				bindingResult.rejectValue("username", "error.user.already.exist", "ERROR");
			}
		
		if(bindingResult.hasErrors()){
			return "users/create";
		}
		;
		Role userRole=roleService.findByName("ROLE_USER");
		user = new User();
		
		user.setActive(true);
		user.setUsername(userCreateForm.getUsername());
		user.setName(userCreateForm.getName());
		user.setEmail(userCreateForm.getEmail());
		
		user.getRoles().add(userRole);
		userRole.getUsers().add(user);
		
		userService.save(user);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.user.created", null, locale));
		return "redirect:/users/edit/" + user.getId();
	}
	
	@RequestMapping("/monitor")
	@Transactional
	public String showMonitor(Model model) {
		
		model.addAttribute("sessions", userService.getSessionsInfo());
		
		return "users/monitor";
	}
	
}

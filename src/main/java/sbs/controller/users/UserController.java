package sbs.controller.users;

import java.util.List;
import java.util.Locale;

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

import javassist.NotFoundException;
import sbs.model.Role;
import sbs.model.User;
import sbs.service.RoleService;
import sbs.service.UserService;

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
		model.addAttribute("userEditForm",userEditForm);
		return "users/edit";
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
		redirectAttrs.addFlashAttribute("ok", messageSource.getMessage("action.password.changed", null, locale));
		return "redirect:/users/edit/" + userPasswordForm.getId();
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
			/*
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.register", bindingResult);
			redirectAttrs.addFlashAttribute("userEditForm", userEditForm);
			redirectAttrs.addFlashAttribute("error",bindingResult.getAllErrors());
			return "redirect:/users/edit/" + userEditForm.getId();
			*/
			userEditForm.setRoles(userService.findById(userEditForm.getId()).getRoles());
			return "users/edit";
		}
		modelUser.setUsername(userEditForm.getUsername());
		modelUser.setName(userEditForm.getName());
		modelUser.setEmail(userEditForm.getEmail());
		modelUser.setActive(userEditForm.getActive());
		userService.update(modelUser);
		
		redirectAttrs.addFlashAttribute("ok", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/users/edit/" + userEditForm.getId();
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
		redirectAttrs.addFlashAttribute("ok", messageSource.getMessage("action.saved", null, locale));
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
		redirectAttrs.addFlashAttribute("ok", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/users/edit/" + userId;
	}
	
	
	@RequestMapping(value = "/manageroles", method = RequestMethod.POST)
	public String manageRoles(){
		
		return "redirect:/";
		
	}
	
	
	/*

// for thymeleaf list binding
  <span th:each="role, stat : *{roles}">         
    <input type="checkbox" 
            th:name="|roles[${stat.index}]|"
            th:value="${role.id}"
            th:checked="${true}" />

     </span>



	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional
	public String registerUser(@Valid UserForm registerForm, BindingResult bindingResult) {
		
		User modelUser = userService.findByUsername(registerForm.getUsername());
		if(modelUser != null){
			bindingResult.rejectValue("username", "error.user.already.exist", "ERROR");
		}
		if(!registerForm.getPassword().equals(registerForm.getRepeatPassword())){
			bindingResult.rejectValue("repeatPassword", "password.repeat", "ERROR");
		}
		if (bindingResult.hasErrors()) {
			return "register";
		}
		
		// user role
		Role userRole = roleService.findByName("ROLE_USER");
		modelUser = new User();
		modelUser.setUsername(registerForm.getUsername());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(registerForm.getPassword());
		modelUser.setPassword(hashedPassword);
		modelUser.setName(registerForm.getName());
		modelUser.setEmail(registerForm.getEmail());
		// many to many
		modelUser.getRoles().add(userRole);
		userRole.getUsers().add(modelUser);
		
		userService.save(modelUser);
		
		return "redirect:/profile/show";
	}
	*/
}

package sbs.controller.users;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	private User getAuthenticatedUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User modelUser;
			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)auth.getPrincipal();
			modelUser = userService.findByUsername(user.getUsername());
			return modelUser;
		}
		return null;
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
	
	@RequestMapping("/show/{id}")
	public String showProfile(@PathVariable("id") long id, Model model) {
		model.addAttribute("user",userService.findById(id));
		return "users/show";
	}
	
	@RequestMapping("/edit/{id}")
	public String showUserCreateForm(@PathVariable("id") long id, Model model) {
		model.addAttribute("userForm", new UserForm());
		model.addAttribute("user",userService.findById(id));
		return "users/edit";
	}
	
	@RequestMapping(value = "/create")
	public String showUserCreateForm(UserForm registerForm) {
		return "register";
	}

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
	
	
		@RequestMapping(value = "/edit")
		public String showUserEditForm(Model model) {
			User user = getAuthenticatedUser();
			model.addAttribute("user", user);
			model.addAttribute("profileForm", new ProfileForm(user));
			return "profileedit";
		}
		
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String updateUser(@Valid ProfileForm profileForm, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			return "profileedit";
		}
		User modelUser = userService.findByUsername(profileForm.getUsername());
		modelUser.setName(profileForm.getName());
		modelUser.setEmail(profileForm.getEmail());
		userService.update(modelUser);
		
		return "redirect:/profile/show";
	}

}

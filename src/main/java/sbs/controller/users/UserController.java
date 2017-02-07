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
	@RequestMapping("/list")
	public String showUsersList(Model model) {
		model.addAttribute("users", userService.findAll());
		return "profile";
	}
	
	/**
	 * show existing profile
	 * @param model
	 * @return
	 */
	@RequestMapping("/show")
	public String showProfile(Model model) {
		model.addAttribute("user", getAuthenticatedUser());
		return "edit";
	}
	
	@RequestMapping(value = "/create")
	public String showUserCreateForm(RegisterForm registerForm) {
		return "register";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional
	public String registerUser(@Valid RegisterForm registerForm, BindingResult bindingResult) {
		
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

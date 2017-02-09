package sbs.controller.users;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import sbs.model.User;

public class ProfileForm {
	@Size(min = 2, max=25)
	private String username;
	@Size(min = 2, max=25)
	private String name;
	@Email  
	@NotEmpty
	private String email;
	private String avatarPath;
	
	public ProfileForm(){
		
	}
	
	public ProfileForm(User user){
		if(user!=null){
			this.username =  user.getUsername();
			this.name = user.getName();
			this.email = user.getEmail();
			this.avatarPath= user.getAvatarFileName();
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatarPath() {
		return avatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	
	
}

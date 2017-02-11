package sbs.controller.users;

import java.util.Set;
import sbs.model.Role;

public class RolesHolder {
	private Set<Role> roles;

	
	
	public RolesHolder(Set<Role> roles) {
		super();
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}

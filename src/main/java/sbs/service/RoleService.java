package sbs.service;

import sbs.model.Role;

public interface RoleService extends GenericService<Role, Long>{
	public Role findByName(String roleName);
}

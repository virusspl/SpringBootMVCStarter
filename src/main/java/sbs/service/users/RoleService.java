package sbs.service.users;

import sbs.model.users.Role;
import sbs.service.GenericService;

public interface RoleService extends GenericService<Role, Long>{
	public Role findByName(String roleName);
}

package sbs.repository.users;

import sbs.model.users.Role;
import sbs.repository.GenericRepository;

public interface RoleRepository extends GenericRepository<Role,Long> {
	public Role findByName(String roleName);
}


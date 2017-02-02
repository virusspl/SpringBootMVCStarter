package sbs.repository;

import sbs.model.Role;

public interface RoleRepository extends GenericRepository<Role,Long> {
	public Role findByName(String roleName);
}


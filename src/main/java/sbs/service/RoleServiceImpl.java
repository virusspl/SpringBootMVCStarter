package sbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import sbs.model.Role;
import sbs.repository.GenericRepository;
import sbs.repository.RoleRepository;

@Service
public class RoleServiceImpl extends GenericServiceAdapter<Role, Long> implements RoleService{
	
	private RoleRepository roleRepository;
	
    @Autowired
	public RoleServiceImpl(@Qualifier("roleRepositoryImpl") GenericRepository<Role, Long> genericRepository) {
			super(genericRepository);
			this.roleRepository = (RoleRepository) genericRepository;
	}

	@Override
	public Role findByName(String roleName) {
		return roleRepository.findByName(roleName);
	}
	

}

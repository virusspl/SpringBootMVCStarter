package sbs.repository;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.Role;

@Repository
@Transactional
public class RoleRepositoryImpl extends GenericRepositoryAdapter<Role,Long> implements RoleRepository {

	@Override
	public Role findByName(String roleName) {
		String hql = "from Role r where lower(r.name) = :roleName";
		@SuppressWarnings("unchecked")
		List<Role> result = (List<Role>) currentSession().createQuery(hql).setString("roleName", roleName.toLowerCase()).list();
        if (result == null || result.isEmpty()) {
            return null;
        } 
        return result.get(0);
	}

}

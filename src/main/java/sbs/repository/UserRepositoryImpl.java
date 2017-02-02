package sbs.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import sbs.model.User;

@Repository
@Transactional
public class UserRepositoryImpl extends GenericRepositoryAdapter<User, Long> implements UserRepository {
	
	@Override
	public User findByUsername(String username) {
		String hql = "from User u where lower(u.username) = :username";
		@SuppressWarnings("unchecked")
		List<User> result = (List<User>) currentSession().createQuery(hql).setString("username", username.toLowerCase()).list();
        if (result == null || result.isEmpty()) {
            return null;
        } 
        return result.get(0);
	}

}

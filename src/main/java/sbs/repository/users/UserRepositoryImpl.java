package sbs.repository.users;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import sbs.model.User;
import sbs.repository.GenericRepositoryAdapter;

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
	@Override
	public User findActiveByUsername(String username) {
		String hql = "from User u where lower(u.username) = :username and u.active=true";
		@SuppressWarnings("unchecked")
		List<User> result = (List<User>) currentSession().createQuery(hql).setString("username", username.toLowerCase()).list();
		if (result == null || result.isEmpty()) {
			return null;
		} 
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> find(String range, String sort) {
		
		Criteria crit = currentSession().createCriteria(User.class);
		
		switch (range){
		case UserRepository.ACTIVE_USERS:
			crit.add(Restrictions.eq("active", true));
			break;
		case UserRepository.INACTIVE_USERS:
			crit.add(Restrictions.eq("active", false));
			break;
		case UserRepository.ALL_USERS:
			break;
		default:
			break;
	}
		switch (sort){
		case UserRepository.SORT_ORDER_ID:
			crit.addOrder(Order.asc("id"));
			break;
		case UserRepository.SORT_ORDER_USERNAME:
			crit.addOrder(Order.asc("username"));
			break;
		default:
			break;
		}
		return crit.list();
	}

}

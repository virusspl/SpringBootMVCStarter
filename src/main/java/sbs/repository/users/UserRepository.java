package sbs.repository.users;

import java.util.List;

import sbs.model.users.User;
import sbs.repository.GenericRepository;

public interface UserRepository extends GenericRepository<User,Long>{
	
	public static final String ALL_USERS="all";
	public static final String ACTIVE_USERS="active";
	public static final String INACTIVE_USERS="inactive";
	public static final String SORT_ORDER_ID="id";
	public static final String SORT_ORDER_USERNAME="username";
	
	public User findByUsername(String username);
	public User findActiveByUsername(String username);
	public List<User> find(String range, String sort);
	public List<User> findByRole(String role);
	public List<User> findByAnyRole(String[] roles);
	public User findByRcpNumber(String rcpNumber);
	
}

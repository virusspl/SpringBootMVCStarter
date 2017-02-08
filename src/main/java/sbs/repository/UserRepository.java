package sbs.repository;

import java.util.List;

import sbs.model.User;

public interface UserRepository extends GenericRepository<User,Long>{
	
	public static final String ALL_USERS="all";
	public static final String ACTIVE_USERS="active";
	public static final String INACTIVE_USERS="inactive";
	public static final String SORT_ORDER_ID="id";
	public static final String SORT_ORDER_USERNAME="username";
	
	public User findByUsername(String username);
	public List<User> find(String range, String sort);
}

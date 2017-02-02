package sbs.service;

import sbs.model.User;

public interface UserService extends GenericService<User, Long>{
	public User findByUsername(String username);
	public String getPrincipalName();
}

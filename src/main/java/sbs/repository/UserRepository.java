package sbs.repository;

import sbs.model.User;

public interface UserRepository extends GenericRepository<User,Long>{
	public User findByUsername(String username);
}

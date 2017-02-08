package sbs.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sbs.model.User;
import sbs.repository.GenericRepository;
import sbs.repository.UserRepository;

@Service
public class UserServiceImpl extends GenericServiceAdapter<User, Long> implements UserService{

    private UserRepository userRepository;
    
    @Autowired
	public UserServiceImpl(@Qualifier("userRepositoryImpl") GenericRepository<User, Long> genericRepository) {
			super(genericRepository);
			this.userRepository = (UserRepository) genericRepository;
	}
	
    @Override
    public List<User> findAll(){
    	List<User> result = userRepository.findAll();
		for (User user: result){
			Hibernate.initialize(user.getRoles());
		}
		return result;
    }
    
	@Override
	@Transactional
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user!=null){
			Hibernate.initialize(user.getRoles());
		}
		return user;
	}
	
	@Override
	@Transactional
	public List<User> find(String range, String sort) {
    	List<User> result = userRepository.find(range, sort);
		for (User user: result){
			Hibernate.initialize(user.getRoles());
		}
		return result;
	}

	@Override
	public String getPrincipalName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	
}

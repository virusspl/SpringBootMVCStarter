package sbs.service.users;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sbs.model.users.User;
import sbs.repository.GenericRepository;
import sbs.repository.users.UserRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class UserServiceImpl extends GenericServiceAdapter<User, Long> implements UserService{

    private UserRepository userRepository;
    private SessionRegistry sessionRegistry;
    
    @Autowired
	public UserServiceImpl(
			@Qualifier("userRepositoryImpl") GenericRepository<User, Long> genericRepository,
			SessionRegistry sessionRegistry
			) {
			super(genericRepository);
			this.userRepository = (UserRepository) genericRepository;
			this.sessionRegistry = sessionRegistry;
	}
	
    @Override
    @Transactional
    public List<User> findAll(){
    	List<User> result = userRepository.findAll();
		for (User user: result){
			Hibernate.initialize(user.getRoles());
		}
		return result;
    }
    
    @Override
    @Transactional
    public User findById(Long id) {
    	User user = userRepository.findById(id);
    	if (user!=null){
    		Hibernate.initialize(user.getRoles());
    	}
    	return user;
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

	@Override
	@Transactional
	public User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User modelUser;
			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)auth.getPrincipal();
			modelUser = findByUsername(user.getUsername());
			return modelUser;
		}
		return null;
		}

	@Override
	@Transactional
	public List<User> getLoggedInUsers() {

		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
		List<User> list = new ArrayList<>();
		User modelUser;
		
	        for(Object principal : allPrincipals) {
	            if(principal instanceof org.springframework.security.core.userdetails.User) {
	            	org.springframework.security.core.userdetails.User user = 
	            			(org.springframework.security.core.userdetails.User)principal;
	    			modelUser = findByUsername(user.getUsername());
	            	
	    			if(modelUser!=null){
	    				list.add(modelUser);
	    			}
	            }
	        }
	        return list;
	}
}

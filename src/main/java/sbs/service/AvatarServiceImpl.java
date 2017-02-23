package sbs.service;

import java.io.IOException;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import sbs.config.UploadProperties;
import sbs.model.User;

@Service
public class AvatarServiceImpl implements AvatarService {
	private final String avatarUploadPath;
	
    @Value(value = "classpath:static/images/anonymous/anonymous_2.jpg")
    private Resource anonymousPicture;
	@Autowired 
	UserService userService;
	
    @Autowired
    public AvatarServiceImpl(UploadProperties uploadProperties) {
    	avatarUploadPath = uploadProperties.getAvatarPath();
    }

    @Override
	public Resource getAnonymousPicture() {
		return anonymousPicture;
	}

	@Override
	@Cacheable(value="avatarById")
	public Resource getAvatarResourceById(Long id){
		return getUserAvatar(userService.findById(id));
	}
	
	@Override
	@Cacheable(value="avatarByUsername")
	public Resource getAvatarResourceByUsername(String username){
		return getUserAvatar(userService.findByUsername(username));
	}
	
/*
 * HELPERS
 */
	
private Resource getUserAvatar(User user){
	Resource picture;
	if( user.getAvatarFileName() ==  null || user.getAvatarFileName().isEmpty()){	
		picture = anonymousPicture;
	}else{
		try{
			picture = (new DefaultResourceLoader()).getResource(avatarUploadPath + "/" + user.getAvatarFileName());
			picture.getInputStream().close();
		} catch (IOException ex){
			user.setAvatarFileName(null);
			userService.update(user);
			picture = anonymousPicture;				
		}
	}
	return picture;
}
	

}

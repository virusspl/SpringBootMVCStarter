package sbs.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sbs.config.UploadProperties;
import sbs.model.User;
import sbs.service.UserService;

@Service
public class AvatarServiceImpl implements AvatarService {
	private final String avatarUploadPath;
	
	@Autowired UserService userService;
	
    @Autowired
    public AvatarServiceImpl(UploadProperties uploadProperties) {
    	avatarUploadPath = uploadProperties.getAvatarPath();
    }

    
    @Value(value = "classpath:static/images/anonymous/anonymous_2.jpg")
    private Resource anonymousPicture;
    
	@Override
	//@Cacheable("avatarById")
	@Transactional
	public Resource getAvatarResourceById(Long id){
		Resource picture;
		User modelUser = userService.findById(id);
		if( modelUser.getAvatarFileName() ==  null || modelUser.getAvatarFileName().isEmpty()){	
			picture = anonymousPicture;
		}else{
			try{
				picture = (new DefaultResourceLoader()).getResource(avatarUploadPath + "/" + modelUser.getAvatarFileName());
				picture.getInputStream().close();
			} catch (IOException ex){
				modelUser.setAvatarFileName(null);
				userService.update(modelUser);
				picture = anonymousPicture;				
			}
		}
		return picture;
	}
	
	@Override
	public Resource getCurrentPicturePath() {
		// TODO Auto-generated method stub
		return null;
	}

}

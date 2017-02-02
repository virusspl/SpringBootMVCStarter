package sbs.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
	private final Resource anonymousPicture;
	private final Resource chatBotPicture;
	
	@Autowired UserService userService;
	
    @Autowired
    public AvatarServiceImpl(UploadProperties uploadProperties) {
        anonymousPicture = uploadProperties.getAnonymousPicture();
        chatBotPicture = uploadProperties.getChatBotPicture();
    }
    
    @Override
	public Resource getCurrentPicturePath(){
    	Resource picturePath;
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
				return getAvatarResourceByUsername(auth.getName());
		}else{
			picturePath = anonymousPicture;
		}
    	
    	return picturePath;
    }
    
	@Override
	@Cacheable("avatar")
	public Resource getAvatarResourceByUsername(String username){
		if(username.equals("ChatBot")){
			return chatBotPicture;
		}
		Resource picturePath;
		User modelUser = userService.findByUsername(username);
		if( modelUser.getAvatarPath() ==  null || modelUser.getAvatarPath().isEmpty()){
			picturePath = anonymousPicture;
		}else{
			try{
			picturePath = (new DefaultResourceLoader()).getResource("file:./" + modelUser.getAvatarPath());
			picturePath.getInputStream().close();
			} catch (IOException ex){
				modelUser.setAvatarPath(null);
				userService.update(modelUser);
				picturePath = anonymousPicture;				
			}
		}
		return picturePath;
	}

}

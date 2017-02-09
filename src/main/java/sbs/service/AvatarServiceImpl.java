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
	private final Resource avatarPath;
	
	@Autowired UserService userService;
	
    @Autowired
    public AvatarServiceImpl(UploadProperties uploadProperties) {
    	avatarPath = uploadProperties.getAvatarPath();
    }

	@Override
	@Cacheable("avatarById")
	public Resource getAvatarResourceById(Long id){
		Resource picturePath;
		User modelUser = userService.findById(id);
		if( modelUser.getAvatarPath() ==  null || modelUser.getAvatarPath().isEmpty()){
			//picturePath = avatarPath.;
		}else{
			try{
			picturePath = (new DefaultResourceLoader()).getResource("file:./" + modelUser.getAvatarPath());
			picturePath.getInputStream().close();
			} catch (IOException ex){
				modelUser.setAvatarPath(null);
				userService.update(modelUser);
				//picturePath = anonymousPicture;				
			}
		}
		return null;
	}
	
	@Override
	public Resource getCurrentPicturePath() {
		// TODO Auto-generated method stub
		return null;
	}

}

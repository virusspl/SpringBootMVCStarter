package sbs.service;

import org.springframework.core.io.Resource;

public interface AvatarService {

	Resource getCurrentPicturePath();
	Resource getAvatarResourceById(Long id);

}
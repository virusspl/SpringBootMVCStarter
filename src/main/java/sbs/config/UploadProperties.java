package sbs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "upload")
public class UploadProperties {
	private String uploadPath;
	private String avatarPath;

	public String getUploadPath() {
		return uploadPath;
	}

	public void setPath(String path) {
		this.uploadPath = path;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

}
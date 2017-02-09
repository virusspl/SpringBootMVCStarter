package sbs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "upload")
public class UploadProperties {
	private Resource path;
	private Resource avatarPath;

	public Resource getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = new DefaultResourceLoader().getResource(path);
	}

	public Resource getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = new DefaultResourceLoader().getResource(avatarPath);
	}

}
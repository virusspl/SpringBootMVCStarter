package sbs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "upload")
public class UploadProperties {
	private Resource picturesUploadPath;
	private Resource anonymousPicture;
	private Resource chatBotPicture;

	public Resource getChatBotPicture() {
		return chatBotPicture;
	}
	
	public void setChatBotPicture(String chatBotPicture) {
		this.chatBotPicture = new DefaultResourceLoader().getResource(chatBotPicture);
	}
	
	public Resource getAnonymousPicture() {
		return anonymousPicture;
	}

	public void setAnonymousPicture(String anonymousPicture) {
    this.anonymousPicture = new DefaultResourceLoader().getResource(anonymousPicture);
  }

	public Resource getPicturesUploadPath() {
		return picturesUploadPath;
	}

	public void setPicturesUploadPath(String picturesUploadPath) {
		this.picturesUploadPath = new DefaultResourceLoader().getResource(picturesUploadPath);
	}
}
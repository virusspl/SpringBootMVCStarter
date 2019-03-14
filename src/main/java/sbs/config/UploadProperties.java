package sbs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "upload")
public class UploadProperties {
	private String uploadPath;
	private String avatarPath;
	private String bhpPhotoPath;
	private String toolsPhotoPath;
	private String mapPhotoPath;
	private String qSurveysPhotoPath;
	

	public String getUploadPath() {
		return uploadPath;
	}
	
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	
	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getBhpPhotoPath() {
		return bhpPhotoPath;
	}

	public void setBhpPhotoPath(String bhpPhotoPath) {
		this.bhpPhotoPath = bhpPhotoPath;
	}

	public String getToolsPhotoPath() {
		return toolsPhotoPath;
	}

	public void setToolsPhotoPath(String toolsPhotoPath) {
		this.toolsPhotoPath = toolsPhotoPath;
	}

	public String getMapPhotoPath() {
		return mapPhotoPath;
	}

	public void setMapPhotoPath(String mapPhotoPath) {
		this.mapPhotoPath = mapPhotoPath;
	}

	public String getqSurveysPhotoPath() {
		return qSurveysPhotoPath;
	}

	public void setqSurveysPhotoPath(String qSurveysPhotoPath) {
		this.qSurveysPhotoPath = qSurveysPhotoPath;
	}




	
	
}
package sbs.controller.tools;

import java.sql.Timestamp;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


public class ToolsProjectCreateForm {

	boolean assigned;
	
	@Size(max = 30)
	String cechOld;
	@Size(max = 30)
	String cechNew;
	@Size(max = 30)
	String assetCode;
	@Size(max = 15)
	String clientCode;
	@NotEmpty
	String description;

	String assetName;
	String clientName;
	String thumbnailFileName;
	int priority;
	String creator;
	Long assignedUser;
	String state;
	@DateTimeFormat(pattern= "yyyy.MM.dd HH:mm")
	Timestamp creationDate;
	@DateTimeFormat(pattern= "yyyy.MM.dd HH:mm")	
	Timestamp updateDate;
	
	int id;


	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public ToolsProjectCreateForm() {

	}

	public String getCechOld() {
		return cechOld;
	}

	public void setCechOld(String cechOld) {
		this.cechOld = cechOld;
	}

	public String getCechNew() {
		return cechNew;
	}

	public void setCechNew(String cechNew) {
		this.cechNew = cechNew;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getThumbnailFileName() {
		return thumbnailFileName;
	}

	public void setThumbnailFileName(String thumbnailFileName) {
		this.thumbnailFileName = thumbnailFileName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Long getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(Long assignedUser) {
		this.assignedUser = assignedUser;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return "ToolsProjectCreateForm [cechOld=" + cechOld + ", cechNew=" + cechNew + ", assetCode=" + assetCode
				+ ", clientCode=" + clientCode + ", description=" + description + ", assetName=" + assetName
				+ ", clientName=" + clientName + ", thumbnailFileName=" + thumbnailFileName + ", priority=" + priority
				+ ", creator=" + creator + ", assignedUser=" + assignedUser + ", creationDate="
				+ creationDate + ", updateDate=" + updateDate + ", id=" + id + "]";
	}



	
	
}








	
		

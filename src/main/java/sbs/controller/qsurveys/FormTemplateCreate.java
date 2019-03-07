package sbs.controller.qsurveys;

import javax.validation.constraints.Size;

public class FormTemplateCreate {
	
	
	@Size(min = 5, max = 255)
	private String title;
	private String description;
	private String comment;
	
	
	public FormTemplateCreate() {
	
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	@Override
	public String toString() {
		return "FormTemplateCreate [title=" + title + ", description=" + description + ", comment=" + comment + "]";
	}
	
	
		
	

}

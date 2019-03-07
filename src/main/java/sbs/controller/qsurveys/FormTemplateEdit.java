package sbs.controller.qsurveys;

import javax.validation.constraints.Size;

public class FormTemplateEdit {
	
	
	@Size(min = 5, max = 255)
	private String title;
	private String description;
	private String comment;
	private int id;
	private boolean active;
	
	
	public FormTemplateEdit() {
	
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


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	@Override
	public String toString() {
		return "FormTemplateEdit [title=" + title + ", description=" + description + ", comment=" + comment + ", id="
				+ id + ", active=" + active + "]";
	}


	
	
		
	

}

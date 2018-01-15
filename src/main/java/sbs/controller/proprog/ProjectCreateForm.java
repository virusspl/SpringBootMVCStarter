package sbs.controller.proprog;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ProjectCreateForm {
	
	@NotEmpty
	@Size(min = 3, max = 30)
	String projectNumber;
	
	public ProjectCreateForm() {
		
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	@Override
	public String toString() {
		return "ProjectCreateForm [projectNumber=" + projectNumber + "]";
	}
	
}

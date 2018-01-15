package sbs.controller.proprog;

import sbs.model.proprog.Project;

public class ListItem {
	private Project project;
	private String bootstrapColorTitle;
	private int progress;
	
	public ListItem(Project project) {
		this.project =  project;
		this.bootstrapColorTitle = project.getProgressBootstrapTitle();
		this.progress = project.getProgressTotal();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getBootstrapColorTitle() {
		return bootstrapColorTitle;
	}

	public void setBootstrapColorTitle(String bootstrapColorTitle) {
		this.bootstrapColorTitle = bootstrapColorTitle;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public String toString() {
		return "ListItem [project=" + project + ", bootstrapColorTitle=" + bootstrapColorTitle + ", progress="
				+ progress + "]";
	}
	
	
	
	
}

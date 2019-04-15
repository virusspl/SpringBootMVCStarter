package sbs.controller.qcheck;

import javax.validation.constraints.Size;

public class QCheckActionForm {

	@Size(max=255)
	private String comment;
	
	public QCheckActionForm() {
	
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "QCheckActionForm [comment=" + comment + "]";
	}
	
	
	
}

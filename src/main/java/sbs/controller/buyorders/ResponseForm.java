package sbs.controller.buyorders;

import org.hibernate.validator.constraints.NotEmpty;

public class ResponseForm {

	int id;
	@NotEmpty
	String responderComment;
	
	public ResponseForm() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResponderComment() {
		return responderComment;
	}

	public void setResponderComment(String responderComment) {
		this.responderComment = responderComment;
	}

	@Override
	public String toString() {
		return "ResponseForm [id=" + id + ", responderComment=" + responderComment + "]";
	}

	
	
}

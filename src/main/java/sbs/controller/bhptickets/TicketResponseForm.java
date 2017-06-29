package sbs.controller.bhptickets;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class TicketResponseForm {
	
	boolean modificationAllowed;
	@NotEmpty
	String comment;
	@NotNull
	int newState;
	@NotNull
	int id;
	
	public TicketResponseForm() {
		
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getNewState() {
		return newState;
	}

	public void setNewState(int newState) {
		this.newState = newState;
	}

	
	
	public boolean isModificationAllowed() {
		return modificationAllowed;
	}

	public void setModificationAllowed(boolean modificationAllowed) {
		this.modificationAllowed = modificationAllowed;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TicketResponseForm [comment=" + comment + ", newState=" + newState + "]";
	}

	
	
}








	
		

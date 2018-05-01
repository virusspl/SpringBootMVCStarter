package sbs.controller.bhptickets;

import javax.validation.constraints.NotNull;


public class TicketResponseForm {
	
	boolean ticketsUserModificationAllowed;
	boolean utrCommentNeeded;
	boolean utrUserModificationAllowed;
	String comment;
	String utrComment;
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

	public String getUtrComment() {
		return utrComment;
	}

	public void setUtrComment(String utrComment) {
		this.utrComment = utrComment;
	}

	public int getNewState() {
		return newState;
	}

	public void setNewState(int newState) {
		this.newState = newState;
	}

	
	public boolean isUtrCommentNeeded() {
		return utrCommentNeeded;
	}

	public void setUtrCommentNeeded(boolean isUtrCommentNeeded) {
		this.utrCommentNeeded = isUtrCommentNeeded;
	}

	public boolean isTicketsUserModificationAllowed() {
		return ticketsUserModificationAllowed;
	}

	public void setTicketsUserModificationAllowed(boolean ticketsUserModificationAllowed) {
		this.ticketsUserModificationAllowed = ticketsUserModificationAllowed;
	}

	public boolean isUtrUserModificationAllowed() {
		return utrUserModificationAllowed;
	}

	public void setUtrUserModificationAllowed(boolean utrUserModificationAllowed) {
		this.utrUserModificationAllowed = utrUserModificationAllowed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TicketResponseForm [ticketsUserModificationAllowed=" + ticketsUserModificationAllowed
				+ ", utrUserModificationAllowed=" + utrUserModificationAllowed + ", comment=" + comment
				+ ", utrComment=" + utrComment + ", newState=" + newState + ", id=" + id + "]";
	}
	
}








	
		

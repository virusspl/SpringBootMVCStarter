package sbs.controller.bhptickets;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


public class TicketCreateForm {
	
	@NotEmpty
	@Size(min = 3, max = 200)
	String title;
	@NotEmpty
	String description;
	@NotNull
	boolean toSend;
	@NotNull
	long assignedUser;
	@Future
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date toDoDate;
	int id;
	String stateDescription;
	int stateOrder;
	
	public TicketCreateForm() {
		
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

	public boolean isToSend() {
		return toSend;
	}

	public void setToSend(boolean toSend) {
		this.toSend = toSend;
	}

	public long getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(long assignedUser) {
		this.assignedUser = assignedUser;
	}

	public Date getToDoDate() {
		return toDoDate;
	}

	public void setToDoDate(Date toDoDate) {
		this.toDoDate = toDoDate;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	public int getStateOrder() {
		return stateOrder;
	}

	public void setStateOrder(int stateOrder) {
		this.stateOrder = stateOrder;
	}

	@Override
	public String toString() {
		return "TicketCreateForm [title=" + title + ", description=" + description + ", toSend=" + toSend
				+ ", assignedUser=" + assignedUser + ", toDoDate=" + toDoDate + "]";
	}
	
}








	
		

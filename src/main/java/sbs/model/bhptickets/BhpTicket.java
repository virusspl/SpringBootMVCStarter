package sbs.model.bhptickets;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import sbs.model.users.User;

@Entity
@Table(name = "bhp_tickets")
public class BhpTicket {

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bhpt_bhps_id", nullable = false)
	private BhpTicketState state;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bhpt_usr_id_creuser", nullable = false)
	private User creator;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bhpt_usr_id_assigned_user", nullable = false)
	private User assignedUser;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bhpt_id")
	private int id;
	
	@Column(name = "bhpt_title", length = 200, nullable = false)
	private String title;
	
	@Column(name = "bhpt_description", nullable = false)
	@Type(type="text")
	private String description;
	
	@Column(name = "bhpt_comment")
	@Type(type="text")
	private String comment;
	
	@Column(name = "bhpt_utr_comment")
	@Type(type="text")
	private String utrComment;
	
	@Column(name = "bhpt_credat", nullable = false)
	private Timestamp creationDate;

	@Column(name = "bhpt_lastupddat", nullable = false)
	private Timestamp updateDate;	

	@Column(name = "bhpt_tododat", nullable = false)
	private Timestamp toDoDate;

	@Column(name = "bhpt_tosend", nullable=false)
	private Boolean toSend;
	

	public BhpTicket() {

	}


	public BhpTicketState getState() {
		return state;
	}



	public void setState(BhpTicketState state) {
		this.state = state;
	}



	public User getCreator() {
		return creator;
	}



	public void setCreator(User creator) {
		this.creator = creator;
	}



	public User getAssignedUser() {
		return assignedUser;
	}



	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
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

	
	public String getUtrComment() {
		return utrComment;
	}


	public void setUtrComment(String utrComment) {
		this.utrComment = utrComment;
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

	
	public Timestamp getToDoDate() {
		return toDoDate;
	}

	
	public void setToDoDate(Timestamp toDoDate) {
		this.toDoDate = toDoDate;
	}


	public Boolean getToSend() {
		return toSend;
	}



	public void setToSend(Boolean toSend) {
		this.toSend = toSend;
	}

	@Override
	public String toString() {
		return "BhpTicket [state=" + state + ", creator=" + creator + ", assignedUser=" + assignedUser + ", id=" + id
				+ ", title=" + title + ", description=" + description + ", comment=" + comment + ", utrComment="
				+ utrComment + ", creationDate=" + creationDate + ", updateDate=" + updateDate + ", toDoDate="
				+ toDoDate + ", toSend=" + toSend + "]";
	}
	
	/**
	 * if UTR has to respond or already responded - show it
	 * @return boolean
	 */
	public boolean isUtrCommentNeeded(){
		if(getState().getOrder() == 32 || !getUtrComment().equals("")){
			return true;
		}
		return false;
	}
	


}

package sbs.model.buyorders;

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
@Table(name = "buy_orders")
public class BuyOrder {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bo_id_creuser", nullable = false)
	private User creator;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bo_id_respuser", nullable = true)
	private User responder;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bo_id")
	private int id;
	
	@Column(name = "bo_credat", nullable = false)
	private Timestamp creationDate;
	
	@Column(name = "bo_respdat", nullable = true)
	private Timestamp responseDate;
	
	@Column(name = "bo_item_code", length = 30, nullable = false)
	private String itemCode;
	
	@Column(name = "bo_item_description", length = 120, nullable = false)
	private String itemDescription;
	
	@Column(name = "bo_quantity", nullable = false)
	private int quantity;
	
	@Column(name = "bo_creator_comment")
	@Type(type="text")
	private String creatorComment;
	
	@Column(name = "bo_responder_comment")
	@Type(type="text")
	private String responderComment;
	
	@Column(name = "bo_other_comment")
	@Type(type="text")
	private String otherComment;

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getResponder() {
		return responder;
	}

	public void setResponder(User responder) {
		this.responder = responder;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Timestamp responseDate) {
		this.responseDate = responseDate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCreatorComment() {
		return creatorComment;
	}

	public void setCreatorComment(String creatorComment) {
		this.creatorComment = creatorComment;
	}

	public String getResponderComment() {
		return responderComment;
	}

	public void setResponderComment(String responderComment) {
		this.responderComment = responderComment;
	}

	public String getOtherComment() {
		return otherComment;
	}

	public void setOtherComment(String otherComment) {
		this.otherComment = otherComment;
	}

	@Override
	public String toString() {
		return "BuyOrder [creator=" + creator + ", responder=" + responder + ", id=" + id + ", creationDate="
				+ creationDate + ", responseDate=" + responseDate + ", itemCode=" + itemCode + ", itemDescription="
				+ itemDescription + ", quantity=" + quantity + ", creatorComment=" + creatorComment
				+ ", responderComment=" + responderComment + ", otherComment=" + otherComment + "]";
	}	
	
	
	
	
}

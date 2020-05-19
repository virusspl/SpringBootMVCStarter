package sbs.model.cebs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cebs_lines")
public class CebsLine {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ci_ce_id", nullable = false)
	private CebsEvent cebsEvent;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cl_id")
	private int lineId;
	
	@Column(name = "cl_long_id", nullable = false)
	private long longId;
	
	@Column(name = "cl_user_id", nullable = false)
	private long userId;
	
	@Column(name = "cl_item", length = 100, nullable = false)
	private String item;
	
	@Column(name = "cl_comment", length = 100, nullable = false)
	private String comment;
	
	@Column(name = "cl_quantity", nullable = false)
	private int quantity;
	
	@Column(name = "cl_amount", nullable = false)
	private double amount;
	
	@Column(name = "cl_paid", nullable = false)
	private boolean paid;

	public CebsLine() {
	
	}

	public CebsEvent getCebsEvent() {
		return cebsEvent;
	}

	public void setCebsEvent(CebsEvent cebsEvent) {
		this.cebsEvent = cebsEvent;
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public long getLongId() {
		return longId;
	}

	public void setLongId(long longId) {
		this.longId = longId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	@Override
	public String toString() {
		return "CebsLine [lineId=" + lineId + ", longId=" + longId + ", userId=" + userId + ", item=" + item
				+ ", comment=" + comment + ", quantity=" + quantity + ", amount=" + amount + ", paid=" + paid + "]";
	}

	
	
	
}

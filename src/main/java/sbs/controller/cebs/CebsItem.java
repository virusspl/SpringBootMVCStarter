package sbs.controller.cebs;

import sbs.model.users.User;

public class CebsItem {

	Long id;
	User user;
	String item;
	String comment;
	int quantity;
	Double amount;
	boolean paid;
	
	public CebsItem(User user) {
		this.id = new java.util.Date().getTime();
		this.user = user;
		this.paid = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean payed) {
		this.paid = payed;
	}

	@Override
	public String toString() {
		return "CebsItem [id=" + id + ", user=" + user + ", item=" + item + ", comment=" + comment + ", quantity="
				+ quantity + ", amount=" + amount + "]";
	}

}

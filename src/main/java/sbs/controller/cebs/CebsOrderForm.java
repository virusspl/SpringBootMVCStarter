package sbs.controller.cebs;

public class CebsOrderForm {

	String item;
	String comment;
	int quantity;
	
	public CebsOrderForm() {
		
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

	@Override
	public String toString() {
		return "CebsOrderForm [item=" + item + ", comment=" + comment + ", quantity=" + quantity + "]";
	}

	
}

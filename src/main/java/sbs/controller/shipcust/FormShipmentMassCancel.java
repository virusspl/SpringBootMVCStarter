package sbs.controller.shipcust;

import javax.validation.constraints.Size;

public class FormShipmentMassCancel {

	private int orderId;

	@Size(max=150)
	private String comment;

	public FormShipmentMassCancel() {
	
	}
	
	public FormShipmentMassCancel(int orderId) {
		this.orderId = orderId;
	}
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "FormShipmentMassCancel [orderId=" + orderId + ", comment=" + comment + "]";
	}
	
	
	
}

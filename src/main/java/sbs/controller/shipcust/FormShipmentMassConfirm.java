package sbs.controller.shipcust;

import javax.validation.constraints.Size;

public class FormShipmentMassConfirm {

	private int orderId;

	@Size(max=50)
	private String waybill;
	@Size(max=150)
	private String comment;
	
	public FormShipmentMassConfirm() {
	
	}

	public FormShipmentMassConfirm(int orderId) {
		this.orderId = orderId;
	}
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getWaybill() {
		return waybill;
	}

	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "FormShipmentMassConfirm [orderId=" + orderId + ", waybill=" + waybill + ", comment=" + comment + "]";
	}
	
	
	
}

package sbs.controller.shipcust;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import sbs.model.shipcust.CustomShipmentLine;

public class FormShipmentLineManage {

	private int lineId;
	private int orderId;
	private String productCode;
	private String productDescription;
	private String stateCode;
	private int ordered;
	
	
	@Min(1)
	private int shipped;
	@Size(max=50)
	private String waybill;
	@Size(max=150)
	private String comment;

	public FormShipmentLineManage() {
	
	}
	
	public FormShipmentLineManage(CustomShipmentLine line, int orderId) {
		this.orderId = orderId;
		this.shipped = line.getQuantityDemanded();
		this.lineId = line.getId();
		this.productCode = line.getProductCode();
		this.productDescription = line.getProductDescription();
		this.stateCode = line.getState().getCode();
		this.ordered = line.getQuantityDemanded();
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public int getOrdered() {
		return ordered;
	}

	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}

	public int getShipped() {
		return shipped;
	}

	public void setShipped(int shipped) {
		this.shipped = shipped;
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
	

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "FormShipmentLineManage [lineId=" + lineId + ", productCode=" + productCode + ", productDescription="
				+ productDescription + ", stateCode=" + stateCode + ", ordered=" + ordered + ", shipped=" + shipped
				+ ", waybill=" + waybill + ", comment=" + comment + "]";
	}
	
	
	
}

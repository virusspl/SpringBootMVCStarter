package sbs.controller.shipcust;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sbs.model.shipcust.CustomShipment;

public class FormCreateCustomShipmentLine {

	
	private String clientCode;
	private String clientName;
	private String transport;
	private int shipmentId;
	
	@Size(min=3, max=25)
	private String productCode;
	@NotNull
	@Min(1)
	private int quantityDemanded;
	@Size(min=3, max=25)
	private String salesOrder;
	
	private boolean addition;
	private boolean certificate;
	private boolean spare;
	
	@Size(max=255)
	private String comment;

	public FormCreateCustomShipmentLine() {
	
	}
	
	public FormCreateCustomShipmentLine(CustomShipment shipment) {
		this.clientCode = shipment.getClientCode();
		this.clientName = shipment.getClientName();
		this.transport = shipment.getTransport().getName();
		this.shipmentId = shipment.getId();
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getQuantityDemanded() {
		return quantityDemanded;
	}

	public void setQuantityDemanded(int quantityDemanded) {
		this.quantityDemanded = quantityDemanded;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public boolean isAddition() {
		return addition;
	}

	public void setAddition(boolean addition) {
		this.addition = addition;
	}

	public boolean isCertificate() {
		return certificate;
	}

	public void setCertificate(boolean certificate) {
		this.certificate = certificate;
	}

	public boolean isSpare() {
		return spare;
	}

	public void setSpare(boolean spare) {
		this.spare = spare;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public int getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(int shipmentId) {
		this.shipmentId = shipmentId;
	}

	@Override
	public String toString() {
		return "FormCreateCustomShipmentLine [clientCode=" + clientCode + ", clientName=" + clientName + ", transport="
				+ transport + ", productCode=" + productCode + ", quantityDemanded=" + quantityDemanded
				+ ", salesOrder=" + salesOrder + ", addition=" + addition + ", certificate=" + certificate + ", spare="
				+ spare + ", comment=" + comment + "]";
	}
}
	

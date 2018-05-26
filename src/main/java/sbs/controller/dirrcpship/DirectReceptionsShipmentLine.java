package sbs.controller.dirrcpship;

import java.sql.Timestamp;

public class DirectReceptionsShipmentLine {
	private String orderNumber;
	private String productCode;
	private String productDescription;
	private Timestamp demandedDate;
	private String clientName;
	private String country;
	private int leftToSend;
	private int magStock;
	private int toGive;
	
	public DirectReceptionsShipmentLine() {
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public Timestamp getDemandedDate() {
		return demandedDate;
	}

	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getMagStock() {
		return magStock;
	}

	public void setMagStock(int magStock) {
		this.magStock = magStock;
	}

	public int getToGive() {
		return toGive;
	}

	public void setToGive(int toGive) {
		this.toGive = toGive;
	}

	public int getLeftToSend() {
		return leftToSend;
	}

	public void setLeftToSend(int leftToSend) {
		this.leftToSend = leftToSend;
	}

	@Override
	public String toString() {
		return "DirectReceptionsShipmentLine [orderNumberAndLine=" + orderNumber + ", productCode=" + productCode
				+ ", productDescription=" + productDescription + ", demandedDate=" + demandedDate + ", clientName="
				+ clientName + ", country=" + country + ", magStock=" + magStock + ", leftToSend=" + leftToSend
				+ ", toGive=" + toGive + "]";
	}

	
}

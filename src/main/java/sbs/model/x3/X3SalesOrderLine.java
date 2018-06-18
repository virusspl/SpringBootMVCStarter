package sbs.model.x3;

import java.sql.Timestamp;

public class X3SalesOrderLine {
	private String orderNumber;
	private int orderLineNumber;
	private String productCode;
	private String productDescription;
	private String productGr1;
	private String productGr2;
	private String productGr3;
	private String clientCode;
	private String clientName;
	private String country;
	private Timestamp demandedDate;
	private int quantityLeftToSend;
	private int quantityOrdered;
	
	public X3SalesOrderLine() {
		
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOrderLineNumber() {
		return orderLineNumber;
	}

	public void setOrderLineNumber(int orderLineNumber) {
		this.orderLineNumber = orderLineNumber;
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

	public String getProductGr1() {
		return productGr1;
	}

	public void setProductGr1(String productGr1) {
		this.productGr1 = productGr1;
	}

	public String getProductGr2() {
		return productGr2;
	}

	public void setProductGr2(String productGr2) {
		this.productGr2 = productGr2;
	}

	public String getProductGr3() {
		return productGr3;
	}

	public void setProductGr3(String productGr3) {
		this.productGr3 = productGr3;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Timestamp getDemandedDate() {
		return demandedDate;
	}

	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
	}

	public int getQuantityLeftToSend() {
		return quantityLeftToSend;
	}

	public void setQuantityLeftToSend(int quantityLeftToSend) {
		this.quantityLeftToSend = quantityLeftToSend;
	}

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	@Override
	public String toString() {
		return "X3SalesOrderLine [orderNumber=" + orderNumber + ", orderLineNumber=" + orderLineNumber
				+ ", productCode=" + productCode + ", productDescription=" + productDescription + ", productGr1="
				+ productGr1 + ", productGr2=" + productGr2 + ", productGr3=" + productGr3 + ", clientCode="
				+ clientCode + ", clientName=" + clientName + ", country=" + country + ", demandedDate=" + demandedDate
				+ ", quantityLeftToSend=" + quantityLeftToSend + ", quantityOrdered=" + quantityOrdered + "]";
	}
	
	
	
	
}

package sbs.model.x3;

import java.sql.Timestamp;

public class X3SalesOrderItem {
	private String orderNumber;
	private Timestamp demandedDate;
	private String productCode;
	private String productDescription;
	private int quantityOrdered;
	
	public X3SalesOrderItem() {
		
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

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Timestamp getDemandedDate() {
		return demandedDate;
	}

	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
	}

	@Override
	public String toString() {
		return "X3SalesOrderItem [orderNumber=" + orderNumber + ", demandedDate=" + demandedDate + ", productCode="
				+ productCode + ", productDescription=" + productDescription + ", quantityOrdered=" + quantityOrdered
				+ "]";
	}

	
}

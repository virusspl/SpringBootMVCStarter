package sbs.model.x3;

import java.sql.Timestamp;

public class X3PurchaseOrder {
	
	private String purchaseNumber;
	private String supplierCode;
	private String supplierName;
	private Timestamp orderDate;
	private boolean opened;
	
	public X3PurchaseOrder() {
	
	}

	public String getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(String purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	@Override
	public String toString() {
		return "X3PurchaseOrder [purchaseNumber=" + purchaseNumber + ", supplierCode=" + supplierCode
				+ ", supplierName=" + supplierName + ", orderDate=" + orderDate + ", opened=" + opened + "]";
	}

	

}

package sbs.controller.prodorigin;

import java.sql.Timestamp;

public class SupplyInfo {

	private String supplierCode;
	private String supplierName;
	private String supplierCountry;
	private int numberOfReceptions;
	private int purchasedQuantity;
	private Timestamp lastReceptionDate;
	private String lastReceptionNumber;
	private double lastReceptionPrice;
	
	public SupplyInfo() {
		
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

	public int getNumberOfReceptions() {
		return numberOfReceptions;
	}

	public void setNumberOfReceptions(int numberOfReceptions) {
		this.numberOfReceptions = numberOfReceptions;
	}

	public int getPurchasedQuantity() {
		return purchasedQuantity;
	}

	public void setPurchasedQuantity(int purchasedQuantity) {
		this.purchasedQuantity = purchasedQuantity;
	}

	public Timestamp getLastReceptionDate() {
		return lastReceptionDate;
	}

	public void setLastReceptionDate(Timestamp lastReceptionDate) {
		this.lastReceptionDate = lastReceptionDate;
	}

	public String getLastReceptionNumber() {
		return lastReceptionNumber;
	}

	public void setLastReceptionNumber(String lastReceptionNumber) {
		this.lastReceptionNumber = lastReceptionNumber;
	}

	public String getSupplierCountry() {
		return supplierCountry;
	}

	public void setSupplierCountry(String supplierCountry) {
		this.supplierCountry = supplierCountry;
	}

	public double getLastReceptionPrice() {
		return lastReceptionPrice;
	}

	public void setLastReceptionPrice(double lastReceptionPrice) {
		this.lastReceptionPrice = lastReceptionPrice;
	}
	

	
}

package sbs.model.x3;

import java.sql.Timestamp;

public class X3SupplyStatInfo {
	
	String productCode;
	String supplierCode;
	
	String lastReceptionNumber;
	Timestamp lastReceptionDate;
	int numberOfReceptions;
	int purchasedQuantity;
	double lastReceptionPrice;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getLastReceptionNumber() {
		return lastReceptionNumber;
	}
	public void setLastReceptionNumber(String lastReceptionNumber) {
		this.lastReceptionNumber = lastReceptionNumber;
	}
	public Timestamp getLastReceptionDate() {
		return lastReceptionDate;
	}
	public void setLastReceptionDate(Timestamp lastReceptionDate) {
		this.lastReceptionDate = lastReceptionDate;
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
	
	public double getLastReceptionPrice() {
		return lastReceptionPrice;
	}
	public void setLastReceptionPrice(double lastReceptionPrice) {
		this.lastReceptionPrice = lastReceptionPrice;
	}
	
	@Override
	public String toString() {
		return "X3SupplyStatInfo [productCode=" + productCode + ", supplierCode=" + supplierCode
				+ ", lastReceptionNumber=" + lastReceptionNumber + ", lastReceptionDate=" + lastReceptionDate
				+ ", numberOfReceptions=" + numberOfReceptions + ", purchasedQuantity=" + purchasedQuantity + "]";
	}
	
	
	
	

}

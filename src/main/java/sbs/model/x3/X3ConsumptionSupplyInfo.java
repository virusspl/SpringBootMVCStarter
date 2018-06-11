package sbs.model.x3;

import java.sql.Timestamp;

public class X3ConsumptionSupplyInfo {
	
	private Timestamp date;
	private String supplierCode;
	private String name;
	private String productCode;
	
	public X3ConsumptionSupplyInfo(){
		
	}
	
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	@Override
	public String toString() {
		return "X3ConsumptionSupplyInfo [date=" + date + ", supplierCode=" + supplierCode + ", name=" + name
				+ ", productCode=" + productCode + "]";
	}
	
	
	

}

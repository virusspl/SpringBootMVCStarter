package sbs.model.geode;

import java.sql.Timestamp;

public class GeodeObject {

	private String number;
	private String store;
	private String storeType;
	private String address;
	private String itemCode;
	private double quantity;
	private Timestamp inputDate;
	
	public GeodeObject() {
	
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public Timestamp getInputDate() {
		return inputDate;
	}
	public void setInputDate(Timestamp inputDate) {
		this.inputDate = inputDate;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	@Override
	public String toString() {
		return "GeodeObject [number=" + number + ", store=" + store + ", address=" + address + ", itemCode=" + itemCode
				+ ", quantity=" + quantity + ", inputDate=" + inputDate + "]";
	}


	
	
	
	
}

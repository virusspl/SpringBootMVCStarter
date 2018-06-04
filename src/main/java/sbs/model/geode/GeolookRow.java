package sbs.model.geode;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class GeolookRow {
	private String store;
	private String address;
	private String object;
	private String product;
	private BigDecimal quantity;
	private String unit;
	private Timestamp inputDate;
	
	public GeolookRow() {
		
	}
	
	public GeolookRow(String store, String address, String object, String product, BigDecimal quantity, String unit,
			Timestamp inputDate) {
		super();
		this.store = store;
		this.address = address;
		this.object = object;
		this.product = product;
		this.quantity = quantity;
		this.unit = unit;
		this.inputDate = inputDate;
	}
	/**
	 * @return the store
	 */
	public String getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the object
	 */
	public String getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(String object) {
		this.object = object;
	}
	/**
	 * @return the item
	 */
	public String getProduct() {
		return product;
	}
	/**
	 * @param item the item to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the inputDate
	 */
	public Timestamp getInputDate() {
		return inputDate;
	}
	/**
	 * @param inputDate the inputDate to set
	 */
	public void setInputDate(Timestamp inputDate) {
		this.inputDate = inputDate;
	}

	@Override
	public String toString() {
		return "GeolookRow [store=" + store + ", address=" + address + ", object=" + object + ", product=" + product
				+ ", quantity=" + quantity + ", unit=" + unit + ", inputDate=" + inputDate + "]";
	}
	
	


}

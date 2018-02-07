package sbs.model.x3;

import java.sql.Timestamp;

public class X3ShipmentMovement {

	private String itemCode;
	private String itemDescription;
	private Timestamp date;
	private double quantity;
	private double emergencyAveragePrice;
	private double price;
	private double value;
	
	
	public X3ShipmentMovement() {
		
	}

	public X3ShipmentMovement(String itemCode, String itemDescription, Timestamp date, int quantity,
			double emergencyAveragePrice) {
		super();
		this.itemCode = itemCode;
		this.itemDescription = itemDescription;
		this.date = date;
		this.quantity = quantity;
		this.emergencyAveragePrice = emergencyAveragePrice;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public double getEmergencyAveragePrice() {
		return emergencyAveragePrice;
	}

	public void setEmergencyAveragePrice(double emergencyAveragePrice) {
		this.emergencyAveragePrice = emergencyAveragePrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "X3ShipmentMovement [itemCode=" + itemCode + ", itemDescription=" + itemDescription + ", date=" + date
				+ ", quantity=" + quantity + ", emergencyAveragePrice=" + emergencyAveragePrice + "]";
	}
	
}

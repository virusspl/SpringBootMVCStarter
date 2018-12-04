package sbs.model.x3;

import java.sql.Timestamp;

import sbs.helpers.TextHelper;

public class X3ShipmentMovement {

	
	TextHelper textHelper;
	
	private String itemCode;
	private String itemDescription;
	private String itemCategory;
	private String gr2;
	private String document;
	private String clientCode;
	private String clientName;
	private String movementNumber;
	private Timestamp date;
	private double quantity;
	private double emergencyAveragePrice;
	private double price;
	private double value;
	
	private String quantityFormat;
	private String priceFormat;
	private String valueFormat;
	
	
	
	public X3ShipmentMovement() {
		textHelper = new TextHelper();
		
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
	
	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
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
		this.quantityFormat = textHelper.numberFormatDotNoSpace(quantity); 
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
		this.priceFormat = textHelper.numberFormatDotNoSpace(price);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		this.valueFormat = textHelper.numberFormatDotNoSpace(value);
	}

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}
	
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
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

	public String getMovementNumber() {
		return movementNumber;
	}

	public void setMovementNumber(String movementNumber) {
		this.movementNumber = movementNumber;
	}

	public String getQuantityFormat() {
		return quantityFormat;
	}

	public void setQuantityFormat(String quantityFormat) {
		this.quantityFormat = quantityFormat;
	}

	public String getPriceFormat() {
		return priceFormat;
	}

	public void setPriceFormat(String priceFormat) {
		this.priceFormat = priceFormat;
	}

	public String getValueFormat() {
		return valueFormat;
	}

	public void setValueFormat(String valueFormat) {
		this.valueFormat = valueFormat;
	}

	@Override
	public String toString() {
		return "X3ShipmentMovement [itemCode=" + itemCode + ", itemDescription=" + itemDescription + ", itemCategory="
				+ itemCategory + ", gr2=" + gr2 + ", document=" + document + ", clientCode=" + clientCode
				+ ", clientName=" + clientName + ", movementNumber=" + movementNumber + ", date=" + date + ", quantity="
				+ quantity + ", emergencyAveragePrice=" + emergencyAveragePrice + ", price=" + price + ", value="
				+ value + "]";
	}


	
}

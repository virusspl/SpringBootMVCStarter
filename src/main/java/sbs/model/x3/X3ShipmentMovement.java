package sbs.model.x3;

import java.sql.Timestamp;

import sbs.helpers.TextHelper;

public class X3ShipmentMovement {

	
	private TextHelper textHelper;
	
	private String itemCode;
	private String itemDescription;
	private String itemCategory;
	private String gr2;
	private String document;
	private String clientCode;
	private String clientName;
	private String finalClientCode;
	private String finalClientName;
	private String movementNumber;
	private Timestamp date;
	private double quantity;
	private double emergencyAveragePrice;
	private double price;
	private double value;
	private Timestamp orderDate;
	private Timestamp demandedDate;
	private String quantityFormat;
	private String priceFormat;
	private String valueFormat;
	
	// empty on init from repository
	private String lastMachineCode;
	private String lastDepartmentCode;
	
	
	public X3ShipmentMovement() {
		textHelper = new TextHelper();
		
	}

	public String getLastMachineCode() {
		return lastMachineCode;
	}

	public void setLastMachineCode(String lastMachineCode) {
		this.lastMachineCode = lastMachineCode;
	}

	public String getLastDepartmentCode() {
		return lastDepartmentCode;
	}

	public void setLastDepartmentCode(String lastDepartmentCode) {
		this.lastDepartmentCode = lastDepartmentCode;
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
	
	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public Timestamp getDemandedDate() {
		return demandedDate;
	}

	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
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
	
	/**
	 * null in raw query result
	 */
	public String getFinalClientCode() {
		return finalClientCode;
	}

	public void setFinalClientCode(String finalClientCode) {
		this.finalClientCode = finalClientCode;
	}
	
	/**
	 * null in raw query result
	 */
	public String getFinalClientName() {
		return finalClientName;
	}

	public void setFinalClientName(String finalClientName) {
		this.finalClientName = finalClientName;
	}

	@Override
	public String toString() {
		return "X3ShipmentMovement [itemCode=" + itemCode + ", itemDescription=" + itemDescription + ", itemCategory="
				+ itemCategory + ", gr2=" + gr2 + ", document=" + document + ", clientCode=" + clientCode
				+ ", clientName=" + clientName + ", finalClientCode=" + finalClientCode + ", finalClientName="
				+ finalClientName + ", movementNumber=" + movementNumber + ", date=" + date + ", quantity=" + quantity
				+ ", emergencyAveragePrice=" + emergencyAveragePrice + ", price=" + price + ", value=" + value
				+ ", orderDate=" + orderDate + ", demandedDate=" + demandedDate + ", quantityFormat=" + quantityFormat
				+ ", priceFormat=" + priceFormat + ", valueFormat=" + valueFormat + "]";
	}




	
}

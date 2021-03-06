package sbs.controller.rcptosale;

public class ReceptionToSaleLine implements Comparable<ReceptionToSaleLine>{
	
	private String orderNumber;
	private String orderLine;
	private String itemCode;
	private String itemDescription;
	private String clientCode;
	private String clientName;
	private String Gr1OrObjectNumber;
	private String Gr2OrAddress;
	private String dateInput;
	private String date;
	
	private double toSendOrQuantity;
	private double ordered;
	private double valueToSend;
	
	
	public ReceptionToSaleLine() {
	
	}

	public String getDateInput() {
		return dateInput;
	}


	public void setDateInput(String dateInput) {
		this.dateInput = dateInput;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public String getOrderLine() {
		return orderLine;
	}


	public void setOrderLine(String orderLine) {
		this.orderLine = orderLine;
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

	public String getGr1OrObjectNumber() {
		return Gr1OrObjectNumber;
	}

	public void setGr1OrObjectNumber(String gr1OrObjectNumber) {
		Gr1OrObjectNumber = gr1OrObjectNumber;
	}

	public String getGr2OrAddress() {
		return Gr2OrAddress;
	}

	public void setGr2OrAddress(String gr2OrAddress) {
		Gr2OrAddress = gr2OrAddress;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getToSendOrQuantity() {
		return toSendOrQuantity;
	}

	public void setToSendOrQuantity(double toSendOrQuantity) {
		this.toSendOrQuantity = toSendOrQuantity;
	}

	public double getOrdered() {
		return ordered;
	}

	public void setOrdered(double ordered) {
		this.ordered = ordered;
	}


	public double getValueToSend() {
		return valueToSend;
	}

	public void setValueToSend(double valueToSend) {
		this.valueToSend = valueToSend;
	}

	@Override
	public String toString() {
		return "ReceptionToSaleLine [orderNumber=" + orderNumber + ", orderLine=" + orderLine + ", itemCode=" + itemCode
				+ ", itemDescription=" + itemDescription + ", clientCode=" + clientCode + ", clientName=" + clientName
				+ ", Gr1OrObjectNumber=" + Gr1OrObjectNumber + ", Gr2OrAddress=" + Gr2OrAddress + ", dateInput="
				+ dateInput + ", date=" + date + ", toSendOrQuantity=" + toSendOrQuantity + ", ordered=" + ordered
				+ "]";
	}

	@Override
	public int compareTo(ReceptionToSaleLine o) {
		return new Double(this.getToSendOrQuantity()).compareTo(new Double(o.getToSendOrQuantity()));
	}
	
	
	
	
	

}

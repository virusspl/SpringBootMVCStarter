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
	private String date;
	private double toSendOrQuantity;
	private double ordered;
	
	
	public ReceptionToSaleLine() {
	
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReceptionToSaleLine [orderNumber=");
		builder.append(orderNumber);
		builder.append(", orderLine=");
		builder.append(orderLine);
		builder.append(", itemCode=");
		builder.append(itemCode);
		builder.append(", itemDescription=");
		builder.append(itemDescription);
		builder.append(", clientCode=");
		builder.append(clientCode);
		builder.append(", clientName=");
		builder.append(clientName);
		builder.append(", Gr1OrObjectNumber=");
		builder.append(Gr1OrObjectNumber);
		builder.append(", Gr2OrAddress=");
		builder.append(Gr2OrAddress);
		builder.append(", date=");
		builder.append(date);
		builder.append(", toSendOrQuantity=");
		builder.append(toSendOrQuantity);
		builder.append(", ordered=");
		builder.append(ordered);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(ReceptionToSaleLine o) {
		return new Double(this.getToSendOrQuantity()).compareTo(new Double(o.getToSendOrQuantity()));
	}
	
	
	
	
	

}

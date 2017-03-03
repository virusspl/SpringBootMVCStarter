package sbs.model.x3;

import java.sql.Timestamp;

public class X3SalesOrder {
	
	private String salesNumber;
	private String clientCode;
	private String clientName;
	private Timestamp orderDate;
	private boolean opened;
	
	public X3SalesOrder() {
	
	}

	public X3SalesOrder(String salesNumber, String clientCode, String clientName, Timestamp orderDate, Boolean opened) {
		super();
		this.salesNumber = salesNumber;
		this.clientCode = clientCode;
		this.clientName = clientName;
		this.orderDate = orderDate;
		this.opened = opened;
	}

	public String getSalesNumber() {
		return salesNumber;
	}

	public void setSalesNumber(String salesNumber) {
		this.salesNumber = salesNumber;
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

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	@Override
	public String toString() {
		return "X3SalesOrder [salesNumber=" + salesNumber + ", clientCode=" + clientCode + ", clientName=" + clientName
				+ ", orderDate=" + orderDate + ", opened=" + opened + "]";
	}

}

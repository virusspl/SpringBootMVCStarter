package sbs.model.x3;

import java.io.Serializable;

public class X3ProductEvent implements Serializable{

	
	/*
		1 INNE PRZYJECIA
		2 INNE WYDANIA
		3 PRZYJECIE OD DOSTAWCY
		4 WYSYLKA DO KLIENTA
		5 PRZYJECIE Z PRODUKCJI
		6 WYDANIE NA PRODUKCJE
		12 ZWROT WYSYLEK
	*/
	
	private static final long serialVersionUID = -2087838119460091271L;
	private String productCode;
	private double adjustment;
	private double before;
	private double after;
	private java.util.Date date;
	private int transactionType;
	
	
	public X3ProductEvent() {
		
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public double getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(double adjustment) {
		this.adjustment = adjustment;
	}
	public double getBefore() {
		return before;
	}
	public void setBefore(double before) {
		this.before = before;
	}
	public double getAfter() {
		return after;
	}
	public void setAfter(double after) {
		this.after = after;
	}
	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	public int getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public String toString() {
		return "\n X3ProductEvent \n [productCode=" + productCode + ", adjustment=" + adjustment + ", before=" + before
				+ ", after=" + after + ", date=" + date + ", transactionType=" + transactionType + "]";
	}
	
	
	
}

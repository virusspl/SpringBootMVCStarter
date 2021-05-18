package sbs.model.x3;

import java.sql.Timestamp;

public class X3SalesInvoice {

	private String number;
	private String client;
	private String clientName;
	private Timestamp issueDate;
	private Timestamp paymentStartDueDate;
	private String paymentTerms;
	private double value;
	private String currency;
	
	public X3SalesInvoice() {
	
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Timestamp getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Timestamp issueDate) {
		this.issueDate = issueDate;
	}

	public Timestamp getPaymentStartDueDate() {
		return paymentStartDueDate;
	}

	public void setPaymentStartDueDate(Timestamp paymentStartDueDate) {
		this.paymentStartDueDate = paymentStartDueDate;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "X3SalesInvoice [number=" + number + ", client=" + client + ", clientName=" + clientName + ", issueDate="
				+ issueDate + ", paymentStartDueDate=" + paymentStartDueDate + ", paymentTerms=" + paymentTerms + "]";
	}



}

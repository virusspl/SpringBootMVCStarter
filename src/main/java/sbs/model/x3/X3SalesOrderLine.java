package sbs.model.x3;

import java.sql.Timestamp;

public class X3SalesOrderLine {
	private String orderNumber;
	private int orderLineNumber;
	private String productCode;
	private String productDescription;
	private String productCategory;
	private String productGr1;
	private String productGr2;
	private String productGr3;
	private String clientCode;
	private String clientName;
	private String country;
	private Timestamp demandedDate;
	private Timestamp creationDate;
	private Timestamp updateDate;
	private Timestamp originalDate;
	private int quantityLeftToSend;
	private int quantityOrdered;
	private double unitPrice;
	private double exchangeRate;
	private String currency;
	private int demandState;
	private String finalClientCode;
	private String finalClientName;
	private String creationUserCode;
	private String creationUserName;
	private double margin;
	private int orderSourceMenuPosition;

	
	public X3SalesOrderLine() {
		
	}
	
	public String getCreationUserCode() {
		return creationUserCode;
	}

	public void setCreationUserCode(String creationUserCode) {
		this.creationUserCode = creationUserCode;
	}
	
	public String getCreationUserName() {
		return creationUserName;
	}

	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}

	public int getDemandState() {
		return demandState;
	}

	public void setDemandState(int demandState) {
		this.demandState = demandState;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOrderLineNumber() {
		return orderLineNumber;
	}

	public void setOrderLineNumber(int orderLineNumber) {
		this.orderLineNumber = orderLineNumber;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductGr1() {
		return productGr1;
	}

	public void setProductGr1(String productGr1) {
		this.productGr1 = productGr1;
	}

	public String getProductGr2() {
		return productGr2;
	}

	public void setProductGr2(String productGr2) {
		this.productGr2 = productGr2;
	}

	public String getProductGr3() {
		return productGr3;
	}

	public void setProductGr3(String productGr3) {
		this.productGr3 = productGr3;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Timestamp getDemandedDate() {
		return demandedDate;
	}

	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
	}
	
	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public int getQuantityLeftToSend() {
		return quantityLeftToSend;
	}

	public void setQuantityLeftToSend(int quantityLeftToSend) {
		this.quantityLeftToSend = quantityLeftToSend;
	}

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Timestamp getOriginalDate() {
		return originalDate;
	}

	public void setOriginalDate(Timestamp originalDate) {
		this.originalDate = originalDate;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	
	public String getFinalClientCode() {
		return finalClientCode;
	}

	public void setFinalClientCode(String finalClientCode) {
		this.finalClientCode = finalClientCode;
	}

	public String getFinalClientName() {
		return finalClientName;
	}

	public void setFinalClientName(String finalClientName) {
		this.finalClientName = finalClientName;
	}
	

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}
	
	/**
	 * X3 menu: 6237
	 */
	public int getOrderSourceMenuPosition() {
		return orderSourceMenuPosition;
	}

	public void setOrderSourceMenuPosition(int orderSourceMenuPosition) {
		this.orderSourceMenuPosition = orderSourceMenuPosition;
	}

	@Override
	public String toString() {
		return "X3SalesOrderLine [orderNumber=" + orderNumber + ", orderLineNumber=" + orderLineNumber
				+ ", productCode=" + productCode + ", productDescription=" + productDescription + ", productCategory="
				+ productCategory + ", productGr1=" + productGr1 + ", productGr2=" + productGr2 + ", productGr3="
				+ productGr3 + ", clientCode=" + clientCode + ", clientName=" + clientName + ", country=" + country
				+ ", demandedDate=" + demandedDate + ", creationDate=" + creationDate + ", updateDate=" + updateDate
				+ ", originalDate=" + originalDate + ", quantityLeftToSend=" + quantityLeftToSend + ", quantityOrdered="
				+ quantityOrdered + ", unitPrice=" + unitPrice + ", exchangeRate=" + exchangeRate + ", currency="
				+ currency + ", demandState=" + demandState + ", finalClientCode=" + finalClientCode
				+ ", finalClientName=" + finalClientName + "]";
	}

	
}

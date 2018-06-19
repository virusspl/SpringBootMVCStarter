package sbs.controller.prodtosale;

import java.sql.Timestamp;

public class ProductionToSaleLine {
	
	private String orderNumberAndLine;
	private String productCode;
	private String productDescription;
	private String productGr1;
	private String productGr2;
	private String productGr3;
	private String clientCode;
	private String clientName;
	private Timestamp demandedDate;
	private int quantityOrdered;
	private int quantityLeftToSend;
	private int quantityAvailable;
	
	public String getOrderNumberAndLine() {
		return orderNumberAndLine;
	}
	public void setOrderNumberAndLine(String orderNumberAndLine) {
		this.orderNumberAndLine = orderNumberAndLine;
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
	public Timestamp getDemandedDate() {
		return demandedDate;
	}
	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
	}
	public int getQuantityOrdered() {
		return quantityOrdered;
	}
	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	public int getQuantityLeftToSend() {
		return quantityLeftToSend;
	}
	public void setQuantityLeftToSend(int quantityLeftToSend) {
		this.quantityLeftToSend = quantityLeftToSend;
	}
	public int getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	@Override
	public String toString() {
		return "ProductionToSaleLine [orderNumberAndLine=" + orderNumberAndLine + ", productCode=" + productCode
				+ ", productDescription=" + productDescription + ", productGr1=" + productGr1 + ", productGr2="
				+ productGr2 + ", productGr3=" + productGr3 + ", clientCode=" + clientCode + ", clientName="
				+ clientName + ", demandedDate=" + demandedDate + ", quantityOrdered=" + quantityOrdered
				+ ", quantityLeftToSend=" + quantityLeftToSend + ", quantityAvailable=" + quantityAvailable + "]";
	}
	
	
	
}

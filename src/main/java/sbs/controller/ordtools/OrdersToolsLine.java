package sbs.controller.ordtools;

import java.sql.Timestamp;

import sbs.model.x3.X3ToolEntry;

public class OrdersToolsLine {

	private String order;
	private String productCode;
	private String productDescription;
	private int quantity;
	private Timestamp demandedDate;
	private X3ToolEntry tool;
	
	public OrdersToolsLine() {
	
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public X3ToolEntry getTool() {
		return tool;
	}

	public void setTool(X3ToolEntry tool) {
		this.tool = tool;
	}

	public Timestamp getDemandedDate() {
		return demandedDate;
	}

	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
	}

	@Override
	public String toString() {
		return "OrdersToolsLine [order=" + order + ", productCode=" + productCode + ", productDescription="
				+ productDescription + ", quantity=" + quantity + ", demandedDate=" + demandedDate + ", tool=" + tool
				+ "]";
	}


	
	
	
	
}

package sbs.controller.ordtools;

public class OrdersToolsLine {

	private String order;
	private String productCode;
	private String productDescription;
	private int quantity;
	private String tool;
	
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

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	@Override
	public String toString() {
		return "OrdersToolsLine [order=" + order + ", productCode=" + productCode + ", productDescription="
				+ productDescription + ", quantity=" + quantity + ", tool=" + tool + "]";
	}
	
	
	
	
}

package sbs.model.x3;

public class X3SalesOrderItemSum {
	private String orderNumber;
	
	private String productCode;
	private String productDescription;
	private int quantityOrdered;
	
	public X3SalesOrderItemSum() {
		
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	@Override
	public String toString() {
		return "X3SalesOrderItemSum [orderNumber=" + orderNumber + ", productCode=" + productCode
				+ ", productDescription=" + productDescription + ", quantityOrdered=" + quantityOrdered + "]";
	}
	
	

	
	
}

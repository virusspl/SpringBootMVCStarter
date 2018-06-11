package sbs.model.x3;

public class X3ConsumptionProductInfo {
	private String productCode;
	private String productDescriptionPl;
	private int stock;
	private int inOrder;
	private double averageCost;
	
	public X3ConsumptionProductInfo() {
	
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescriptionPl() {
		return productDescriptionPl;
	}

	public void setProductDescriptionPl(String productDescriptionPl) {
		this.productDescriptionPl = productDescriptionPl;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getInOrder() {
		return inOrder;
	}

	public void setInOrder(int inOrder) {
		this.inOrder = inOrder;
	}

	public double getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(double averageCost) {
		this.averageCost = averageCost;
	}

	@Override
	public String toString() {
		return "X3ConsumptionProductInfo [productCode=" + productCode + ", productDescriptionPl=" + productDescriptionPl
				+ ", stock=" + stock + ", inOrder=" + inOrder + ", averageCost=" + averageCost + "]";
	}


	
	
	
	
}

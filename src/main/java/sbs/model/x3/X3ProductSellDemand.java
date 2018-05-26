package sbs.model.x3;

public class X3ProductSellDemand {
	
	private String productCode;
	private int quantity;
	private String orders;
	
	public X3ProductSellDemand() {

	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public void addOrder(String order, int line){
		if(this.orders == null){
			this.orders = order + "/" + line;
		}
		else{
			this.orders += "; " + order + "/" + line;
		}
	}
	
	public void addQuantity(int quantity){
		this.quantity += quantity;
	}

	@Override
	public String toString() {
		return "X3ProductSellDemand [productCode=" + productCode + ", quantity=" + quantity + ", orders=" + orders
				+ "]";
	}
	
	

}

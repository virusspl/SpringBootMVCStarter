package sbs.model.x3;

import java.util.ArrayList;



public class X3ProductSellDemand {
	
	private String productCode;
	private String quantity;
	private ArrayList<String> orders;
	
	public X3ProductSellDemand() {
		orders = new ArrayList<>();
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public ArrayList<String> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<String> orders) {
		this.orders = orders;
	}
	
	public void addOrder(String order, String line){
		this.orders.add(order + "/" + line);
	}

	@Override
	public String toString() {
		return "X3ProductSellDemand [productCode=" + productCode + ", quantity=" + quantity + ", orders=" + orders
				+ "]";
	}
	
	

}

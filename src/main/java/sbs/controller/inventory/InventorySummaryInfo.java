package sbs.controller.inventory;

public class InventorySummaryInfo {

	String productCode;
	int counter;
	int quantity;
	
	public InventorySummaryInfo(){
		
	}
	
	public InventorySummaryInfo(String productCode){
		this.productCode = productCode;
	}
	
	public InventorySummaryInfo(String productCode, int quantity){
		this.productCode = productCode;
		this.quantity = quantity;
		this.counter = 1;
	}
	
	public void addQuantity(int quantity) {
		this.quantity += quantity;
		this.counter++;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "InventorySummaryInfo [productCode=" + productCode + ", counter=" + counter + ", quantity=" + quantity
				+ "]";
	}
	
	
}

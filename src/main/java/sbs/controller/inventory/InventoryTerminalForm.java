package sbs.controller.inventory;

public class InventoryTerminalForm {
	
	private int inventoryId;	
	private String code;	
	private String address;	
	private String location;	
	private String label;	
	private String saleOrder;	
	private String purchaseOrder;	
	private String packageType;
	private double quantity;
	private String freeString1;
	private String freeString2;
	private double freeDouble;
	
	private String currentValue;
	private int currentColumnNumber;
	private String currentColumnName;
	private String currentColumnCode;
	private boolean currentColumnRequired;
	private boolean currentColumnValidated;
	
	
	public InventoryTerminalForm() {

	}

	public int getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(String saleOrder) {
		this.saleOrder = saleOrder;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getFreeString1() {
		return freeString1;
	}

	public void setFreeString1(String freeString1) {
		this.freeString1 = freeString1;
	}

	public String getFreeString2() {
		return freeString2;
	}

	public void setFreeString2(String freeString2) {
		this.freeString2 = freeString2;
	}

	public double getFreeDouble() {
		return freeDouble;
	}

	public void setFreeDouble(double freeDouble) {
		this.freeDouble = freeDouble;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	
	public int getCurrentColumnNumber() {
		return currentColumnNumber;
	}

	public void setCurrentColumnNumber(int currentColumnNumber) {
		this.currentColumnNumber = currentColumnNumber;
	}

	public String getCurrentColumnName() {
		return currentColumnName;
	}

	public void setCurrentColumnName(String currentColumnName) {
		this.currentColumnName = currentColumnName;
	}
	

	public String getCurrentColumnCode() {
		return currentColumnCode;
	}

	public void setCurrentColumnCode(String currentColumnCode) {
		this.currentColumnCode = currentColumnCode;
	}

	public boolean isCurrentColumnRequired() {
		return currentColumnRequired;
	}

	public void setCurrentColumnRequired(boolean currentColumnRequired) {
		this.currentColumnRequired = currentColumnRequired;
	}

	public boolean isCurrentColumnValidated() {
		return currentColumnValidated;
	}

	public void setCurrentColumnValidated(boolean currentColumnValidated) {
		this.currentColumnValidated = currentColumnValidated;
	}


	
	
	
	
	

}

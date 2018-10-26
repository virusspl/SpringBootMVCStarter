package sbs.controller.inventory;

public class InventoryTerminalForm {
	
	private int inventoryId;	
	private String company;
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
	
	private String codeTitle;	
	private String addressTitle;	
	private String locationTitle;	
	private String labelTitle;	
	private String saleOrderTitle;	
	private String purchaseOrderTitle;	
	private String packageTypeTitle;
	private String quantityTitle;
	private String freeString1Title;
	private String freeString2Title;
	private String freeDoubleTitle;
	
	private String currentValue;
	private int currentColumnNumber;
	private String currentColumnName;
	private String currentColumnCode;
	private boolean currentColumnRequired;
	private boolean currentColumnValidated;
	private boolean readyToSave;;
	
	
	public InventoryTerminalForm() {
		this.readyToSave = false;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	

	public boolean isReadyToSave() {
		return readyToSave;
	}

	public void setReadyToSave(boolean readyToSave) {
		this.readyToSave = readyToSave;
	}

	public String getCodeTitle() {
		return codeTitle;
	}

	public void setCodeTitle(String codeTitle) {
		this.codeTitle = codeTitle;
	}

	public String getAddressTitle() {
		return addressTitle;
	}

	public void setAddressTitle(String addressTitle) {
		this.addressTitle = addressTitle;
	}

	public String getLocationTitle() {
		return locationTitle;
	}

	public void setLocationTitle(String locationTitle) {
		this.locationTitle = locationTitle;
	}

	public String getLabelTitle() {
		return labelTitle;
	}

	public void setLabelTitle(String labelTitle) {
		this.labelTitle = labelTitle;
	}

	public String getSaleOrderTitle() {
		return saleOrderTitle;
	}

	public void setSaleOrderTitle(String saleOrderTitle) {
		this.saleOrderTitle = saleOrderTitle;
	}

	public String getPurchaseOrderTitle() {
		return purchaseOrderTitle;
	}

	public void setPurchaseOrderTitle(String purchaseOrderTitle) {
		this.purchaseOrderTitle = purchaseOrderTitle;
	}

	public String getPackageTypeTitle() {
		return packageTypeTitle;
	}

	public void setPackageTypeTitle(String packageTypeTitle) {
		this.packageTypeTitle = packageTypeTitle;
	}

	public String getFreeString1Title() {
		return freeString1Title;
	}

	public void setFreeString1Title(String freeString1Title) {
		this.freeString1Title = freeString1Title;
	}

	public String getFreeString2Title() {
		return freeString2Title;
	}

	public void setFreeString2Title(String freeString2Title) {
		this.freeString2Title = freeString2Title;
	}

	public String getQuantityTitle() {
		return quantityTitle;
	}

	public void setQuantityTitle(String quantityTitle) {
		this.quantityTitle = quantityTitle;
	}

	public String getFreeDoubleTitle() {
		return freeDoubleTitle;
	}

	public void setFreeDoubleTitle(String freeDoubleTitle) {
		this.freeDoubleTitle = freeDoubleTitle;
	}

	@Override
	public String toString() {
		return "InventoryTerminalForm [inventoryId=" + inventoryId + ", company=" + company + ", code=" + code
				+ ", address=" + address + ", location=" + location + ", label=" + label + ", saleOrder=" + saleOrder
				+ ", purchaseOrder=" + purchaseOrder + ", packageType=" + packageType + ", quantity=" + quantity
				+ ", freeString1=" + freeString1 + ", freeString2=" + freeString2 + ", freeDouble=" + freeDouble
				+ ", currentValue=" + currentValue + ", currentColumnNumber=" + currentColumnNumber
				+ ", currentColumnName=" + currentColumnName + ", currentColumnCode=" + currentColumnCode
				+ ", currentColumnRequired=" + currentColumnRequired + ", currentColumnValidated="
				+ currentColumnValidated + "]";
	}


}

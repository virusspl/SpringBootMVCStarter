package sbs.model.x3;

public class X3AvgPriceLine {

	private String productCode;
	private String productDescription;
	private String category;
	private String gr2;
	private Double quantity;
	private Double avgPrice;
	private Double weightAvgPrice;
	
	// empty on init by repository
	private String lastMachineCode;
	private String lastDepartmentCode;
	
	public X3AvgPriceLine() {
	
	}

	public String getLastMachineCode() {
		return lastMachineCode;
	}

	public void setLastMachineCode(String lastMachineCode) {
		this.lastMachineCode = lastMachineCode;
	}

	public String getLastDepartmentCode() {
		return lastDepartmentCode;
	}

	public void setLastDepartmentCode(String lastDepartmentCode) {
		this.lastDepartmentCode = lastDepartmentCode;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Double getWeightAvgPrice() {
		return weightAvgPrice;
	}

	public void setWeightAvgPrice(Double weightAvgPrice) {
		this.weightAvgPrice = weightAvgPrice;
	}
	
	

	@Override
	public String toString() {
		return "X3AvgPriceLine [productCode=" + productCode + ", productDescription=" + productDescription
				+ ", category=" + category + ", gr2=" + gr2 + ", quantity=" + quantity + ", avgPrice=" + avgPrice
				+ ", weightAvgPrice=" + weightAvgPrice + "]";
	}


}

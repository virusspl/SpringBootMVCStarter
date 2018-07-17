package sbs.model.x3;

public class X3ShipmentStockLineWithPrice {

	private String code;
	private String description;
	private String category;
	private String gr1;
	private String gr2;
	private double quantity;
	private String unit;
	private double averagePrice;
	private String location;
	private double lineValue;
	
	public X3ShipmentStockLineWithPrice() {
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGr1() {
		return gr1;
	}

	public void setGr1(String gr1) {
		this.gr1 = gr1;
	}

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLineValue() {
		return lineValue;
	}

	public void setLineValue(double lineValue) {
		this.lineValue = lineValue;
	}

	@Override
	public String toString() {
		return "X3ShipmentStockLineWithPrice [code=" + code + ", description=" + description + ", category=" + category
				+ ", gr1=" + gr1 + ", gr2=" + gr2 + ", quantity=" + quantity + ", unit=" + unit + ", averagePrice="
				+ averagePrice + ", location=" + location + ", lineValue=" + lineValue + "]";
	}
	
	
}

package sbs.model.x3;

public class X3SaleInfo {

	private String code;
	private double valuePln;
	private int quantity;
	
	public X3SaleInfo() {
	
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getValuePln() {
		return valuePln;
	}

	public void setValuePln(double valuePln) {
		this.valuePln = valuePln;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "X3SaleInfo [code=" + code + ", valuePln=" + valuePln + ", quantity="
				+ quantity + "]";
	}
	
	
	
}

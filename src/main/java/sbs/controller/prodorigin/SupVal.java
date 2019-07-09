package sbs.controller.prodorigin;

public class SupVal {
	
	private String supplier;
	private double value;
	private boolean euMember;
	
	public SupVal() {
		
	}

	public SupVal(String supplier, double value, boolean euMember) {
		super();
		this.supplier = supplier;
		this.value = value;
		this.euMember = euMember;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public boolean isEuMember() {
		return euMember;
	}

	public void setEuMember(boolean euMember) {
		this.euMember = euMember;
	}

	@Override
	public String toString() {
		return "SupVal [supplier=" + supplier + ", value=" + value + "]";
	}

	
	
}

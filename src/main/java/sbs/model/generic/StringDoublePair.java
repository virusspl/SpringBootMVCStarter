package sbs.model.generic;

public class StringDoublePair {
			 
	private String stringValue;
	private double doubleValue;
	
	public StringDoublePair() {
	
	}
	
	public StringDoublePair(String stringValue, double doubleValue) {
		super();
		this.stringValue = stringValue;
		this.doubleValue = doubleValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	@Override
	public String toString() {
		return "StringDoublePair [stringValue=" + stringValue + ", doubleValue=" + doubleValue + "]";
	}

	
}

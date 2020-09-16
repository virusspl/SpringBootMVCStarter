package sbs.model.generic;

public class StringIntPair {
			 
	private String stringValue;
	private int intValue;
	
	public StringIntPair() {
	
	}
	

	public StringIntPair(String stringValue, int intValue) {
		super();
		this.stringValue = stringValue;
		this.intValue = intValue;
	}


	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	@Override
	public String toString() {
		return "StringIntPair [stringValue=" + stringValue + ", intValue=" + intValue + "]";
	}
	
	
	
	
}

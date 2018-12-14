package sbs.model.x3;

public class X3KeyValString {

	String key;
	String value;
	
	public X3KeyValString(){
		
	}

	public X3KeyValString(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "X3KeyValString [key=" + key + ", value=" + value + "]";
	}
	
	
	
}

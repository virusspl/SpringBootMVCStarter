package sbs.model.geode;

public class GeodeQuantityObject {

	private String code;
	private int quantity;
	private int count;
	
	public GeodeQuantityObject() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "GeodeQuantityObject [code=" + code + ", quantity=" + quantity + ", count=" + count + "]";
	}
	
	
	
}

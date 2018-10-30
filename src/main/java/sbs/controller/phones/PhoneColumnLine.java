package sbs.controller.phones;

public class PhoneColumnLine {

	boolean category;
	String name;
	String position;
	String number;
	
	
	public PhoneColumnLine() {

	}

	public boolean isCategory() {
		return category;
	}

	public void setCategory(boolean category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "PhoneColumnLine [category=" + category + ", name=" + name + ", number=" + number + "]";
	}
	
	
	
}

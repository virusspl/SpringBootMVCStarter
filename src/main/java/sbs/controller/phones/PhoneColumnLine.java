package sbs.controller.phones;

public class PhoneColumnLine {

	boolean category;
	String name;
	int number;
	
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "PhoneColumnLine [category=" + category + ", name=" + name + ", number=" + number + "]";
	}
	
	
	
}

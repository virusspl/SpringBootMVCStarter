package sbs.controller.phones;

public class PhoneColumnLine {

	boolean category;
	String name;
	String position;
	String number;
	String numberShort;
	String numberInternal;
	String numberInternalPortable;
	String voip;
	String note;
	
	
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

	public String getNumberShort() {
		return numberShort;
	}

	public void setNumberShort(String numberShort) {
		this.numberShort = numberShort;
	}

	public String getNumberInternal() {
		return numberInternal;
	}

	public void setNumberInternal(String numberInternal) {
		this.numberInternal = numberInternal;
	}

	public String getNumberInternalPortable() {
		return numberInternalPortable;
	}

	public void setNumberInternalPortable(String numberInternalPortable) {
		this.numberInternalPortable = numberInternalPortable;
	}

	public String getVoip() {
		return voip;
	}

	public void setVoip(String voip) {
		this.voip = voip;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "PhoneColumnLine [category=" + category + ", name=" + name + ", number=" + number + "]";
	}
	
	
	
}

package sbs.controller.phones;


import javax.validation.constraints.Size;

public class PhoneEditForm {
	
	@Size(min=0, max = 45)
	String number;
	@Size(min=0, max = 10)
	String numberShort;
	@Size(min=0, max = 10)
	String numberInternal;
	@Size(min=0, max = 10)
	String numberInternalPortable;
	@Size(min=1, max = 50)
	String name;
	@Size(min=0, max = 50)
	String position;
	@Size(min=0, max = 75)
	String email;
	@Size(min=0, max = 20)
	String voip;
	@Size(min=0, max = 50)
	String note;
	Integer categoryId;
	String version;
	int id;
	boolean active;
	

	
	public PhoneEditForm() {
		
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVoip() {
		return voip;
	}

	public void setVoip(String voip) {
		this.voip = voip;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setNumberInternalPortable(String numberInternalPortable) {
		this.numberInternalPortable = numberInternalPortable;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}	
	

	


}

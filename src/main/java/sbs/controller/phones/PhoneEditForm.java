package sbs.controller.phones;


import javax.validation.constraints.Size;

public class PhoneEditForm {
	
	@Size(min=1, max = 45)
	String number;
	@Size(min=1, max = 50)
	String name;
	@Size(min=0, max = 50)
	String position;
	@Size(min=0, max = 50)
	String email;
	Integer categoryId;
	String version;

	
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
	


}

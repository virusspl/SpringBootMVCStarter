package sbs.controller.phones;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PhoneEditForm {
	
	@NotNull
	Integer number;
	@Size(min=1, max = 50)
	String name;
	Integer categoryId;

	
	public PhoneEditForm() {
		
	}


	public Integer getNumber() {
		return number;
	}


	public void setNumber(Integer number) {
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


}

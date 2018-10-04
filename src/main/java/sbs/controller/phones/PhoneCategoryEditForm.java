package sbs.controller.phones;


import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class PhoneCategoryEditForm {
	
	Integer id;
	@NotEmpty
	@Size(max = 50)
	String name;
	Integer order;
	
	public PhoneCategoryEditForm() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	

}

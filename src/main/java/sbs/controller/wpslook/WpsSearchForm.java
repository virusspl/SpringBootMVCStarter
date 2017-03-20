package sbs.controller.wpslook;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class WpsSearchForm {

	@NotNull
	@NotEmpty
	private String product;

	public String getProduct() {
		return product;
	}
	
	public void setProduct(String product) {
		this.product = product;
	}
	
	
}

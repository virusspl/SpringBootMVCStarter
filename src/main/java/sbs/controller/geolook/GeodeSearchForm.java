package sbs.controller.geolook;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class GeodeSearchForm {

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

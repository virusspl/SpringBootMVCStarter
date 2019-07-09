package sbs.controller.prodorigin;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ProdOriginForm {

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

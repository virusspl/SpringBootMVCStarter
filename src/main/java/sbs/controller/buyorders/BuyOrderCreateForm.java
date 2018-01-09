package sbs.controller.buyorders;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class BuyOrderCreateForm {
	@NotEmpty
	@Size(min = 3, max = 10)
	String client;
	@NotEmpty
	@Size(min = 3, max = 30)
	String order;
	@NotEmpty
	@Size(min = 3, max = 30)
	String product;
	String comment;
	@NotNull @Min(1)
	Integer quantity;
	
	
	public BuyOrderCreateForm() {
	
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "BuyOrderCreateForm [client=" + client + ", order=" + order + ", product=" + product + ", comment="
				+ comment + ", quantity=" + quantity + "]";
	}

	


	
	
	
}

package sbs.controller.proprog;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class OrderForm {
	
	int id;
	@NotEmpty
	@Size(min = 3, max = 30)
	String orderNumber;
	@NotNull 
	@Min(1)
	Integer orderQuantity;
	
	public OrderForm(int id) {
		this.id = id;
	}
	
	public OrderForm() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	@Override
	public String toString() {
		return "OrderForm [id=" + id + ", orderNumber=" + orderNumber + ", orderQuantity=" + orderQuantity + "]";
	}
	
	
	
	
	


}

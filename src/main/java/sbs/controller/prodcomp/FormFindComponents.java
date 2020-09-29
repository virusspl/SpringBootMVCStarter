package sbs.controller.prodcomp;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FormFindComponents {

	@Size(min=3)
	private String item;
	@NotNull
	@Min(1)
	private int quantity;
	@NotNull
	private java.util.Date tillDate;
	
	public FormFindComponents() {
	
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

	public java.util.Date getTillDate() {
		return tillDate;
	}

	public void setTillDate(java.util.Date tillDate) {
		this.tillDate = tillDate;
	}

	@Override
	public String toString() {
		return "FormFindComponents [item=" + item + ", quantity=" + quantity + ", tillDate=" + tillDate + "]";
	}

	
}

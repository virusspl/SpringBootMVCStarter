package sbs.controller.qualitysurveys;

import org.hibernate.validator.constraints.NotEmpty;

public class ProductionOrderForm {
	
	@NotEmpty
	private String number;

	public ProductionOrderForm() {
	
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
}

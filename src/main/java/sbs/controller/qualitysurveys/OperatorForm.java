package sbs.controller.qualitysurveys;

import org.hibernate.validator.constraints.NotEmpty;

public class OperatorForm {
	
	@NotEmpty
	private String id;

	public OperatorForm() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}

package sbs.controller.qualitysurveys;

import org.hibernate.validator.constraints.NotEmpty;

public class SurveyDetailsForm {	
	
	@NotEmpty
	private String productCode;
	@NotEmpty
	private String productDescription;
	@NotEmpty
	private String productionOrder;
	@NotEmpty
	private String clientCode;
	@NotEmpty
	private String clientName;
	@NotEmpty
	private String salesOrder;
	@NotEmpty
	private String type;
	
	public SurveyDetailsForm() {

	}
	
	
	
	
}

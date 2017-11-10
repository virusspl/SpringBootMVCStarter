package sbs.controller.wakesurvey;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class WakeQuestionCreateForm {

	@NotEmpty
	@Size(min = 10, max = 250)
	private String text;
	 
	public WakeQuestionCreateForm() {
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
	
}








	
		

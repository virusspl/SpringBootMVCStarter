package sbs.controller.qualitysurveys;

import java.util.ArrayList;
import java.util.List;

public class ParameterSurveyForm {

	List<ParameterSurveyItem> items;
	
	public ParameterSurveyForm() {
		items = new ArrayList<>();
	}

	public List<ParameterSurveyItem> getItems() {
		return items;
	}

	public void setItems(List<ParameterSurveyItem> items) {
		this.items = items;
	}
	
	
	
}

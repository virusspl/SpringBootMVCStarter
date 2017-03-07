package sbs.controller.qualitysurveys;

import java.util.ArrayList;
import java.util.List;

public class ParameterSurveyForm {

	List<ParameterSurveyItem> items;
	private final char booleanType = 'b';
	private final char textType = 't';
	
	public ParameterSurveyForm() {
		items = new ArrayList<>();
	}

	public List<ParameterSurveyItem> getItems() {
		return items;
	}

	public void setItems(List<ParameterSurveyItem> items) {
		this.items = items;
	}

	public char getBooleanType() {
		return booleanType;
	}

	public char getTextType() {
		return textType;
	}

	@Override
	public String toString() {
		return "ParameterSurveyForm [items=" + items + ", booleanType=" + booleanType + ", textType=" + textType + "]";
	}
	
	
	
	
}

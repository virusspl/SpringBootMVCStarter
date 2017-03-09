package sbs.controller.qualitysurveys;

import java.util.ArrayList;
import java.util.List;

public class BomSurveyForm {

	List<BomSurveyFormItem> items;

	public BomSurveyForm() {
		items = new ArrayList<>();
	}

	public List<BomSurveyFormItem> getItems() {
		return items;
	}

	public void setItems(List<BomSurveyFormItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "BomSurveyForm [items=" + items + "]";
	}
	
}

package sbs.controller.qsurveys;

import java.util.ArrayList;
import java.util.List;

public class FormTemplate {

	List<QuestionItem> items;

	public FormTemplate() {
		items = new ArrayList<>();
	}

	public List<QuestionItem> getItems() {
		return items;
	}

	public void setItems(List<QuestionItem> items) {
		this.items = items;
	}
	
	

}

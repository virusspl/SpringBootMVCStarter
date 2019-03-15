package sbs.controller.qsurveys;

import java.util.ArrayList;
import java.util.List;

public class FormBom {

	List<BomItem> items;

	public FormBom() {
		items = new ArrayList<>();
	}

	public List<BomItem> getItems() {
		return items;
	}

	public void setItems(List<BomItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "FormBom [items=" + items + "]";
	}
	
}

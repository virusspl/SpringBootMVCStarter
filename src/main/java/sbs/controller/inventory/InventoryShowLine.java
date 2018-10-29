package sbs.controller.inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryShowLine {
	List<String> values;
	
	public InventoryShowLine() {
		values = new ArrayList<>();
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

}

package sbs.controller.inventory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InventoryColumnCreateForm {

	@Size(min = 1, max = 50)
	String name;
	int typeId;
	boolean required;
	boolean validated;
	int order;
	@NotNull
	int inventoryId;
	
	public InventoryColumnCreateForm() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	

	
	
}

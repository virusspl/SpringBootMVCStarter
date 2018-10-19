package sbs.controller.inventory;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class InventoryCreateForm {

	@Size(min = 1, max = 255)
	String title;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date inventoryDate;
	/* null-able */
	String description;
	
	public InventoryCreateForm() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getInventoryDate() {
		return inventoryDate;
	}

	public void setInventoryDate(Date inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "InventoryCreateForm [title=" + title + ", inventoryDate=" + inventoryDate + ", description="
				+ description + "]";
	}
	
	
	
	
}

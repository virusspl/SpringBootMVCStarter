package sbs.controller.proprog;

public class ChecklistForm {
	
	int id;
	String orderNumber;
	Integer orderQuantity;
	String drawingNumber;

	public ChecklistForm(int id) {
		this.id = id;
	}
	
	public ChecklistForm() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getDrawingNumber() {
		return drawingNumber;
	}

	public void setDrawingNumber(String drawingNumber) {
		this.drawingNumber = drawingNumber;
	}
	
	
	
}

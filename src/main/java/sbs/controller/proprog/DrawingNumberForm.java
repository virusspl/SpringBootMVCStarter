package sbs.controller.proprog;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class DrawingNumberForm {
	
	@NotEmpty
	@Size(min = 3, max = 30)
	String drawingNumber;
	int id;
	
	public DrawingNumberForm() {
		
	}
	
	public DrawingNumberForm(int id) {
		this.id =  id;
	}

	public String getDrawingNumber() {
		return drawingNumber;
	}

	public void setDrawingNumber(String drawingNumber) {
		this.drawingNumber = drawingNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}




}

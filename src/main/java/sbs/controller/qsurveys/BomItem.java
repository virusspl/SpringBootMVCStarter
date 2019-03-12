package sbs.controller.qsurveys;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import sbs.model.x3.X3BomItem;

public class BomItem {
	private int sequence;
	private String partCode;
	private String partDescription;
	private String modelUnit;
	private Double modelQuantity;
	@NotNull
	@NotEmpty
	private String answerQuantity;
	private String comment;
	
	public BomItem() {

	}
	
	public BomItem(X3BomItem bomItem){
		this.sequence = bomItem.getSequence();
		this.partCode = bomItem.getPartCode();
		this.partDescription = bomItem.getPartDescription();
		this.modelUnit = bomItem.getModelUnit();
		this.modelQuantity = bomItem.getModelQuantity();
	}
	
	public BomItem(X3BomItem bomItem, int multiplier){
		this.sequence = bomItem.getSequence();
		this.partCode = bomItem.getPartCode();
		this.partDescription = bomItem.getPartDescription();
		this.modelUnit = bomItem.getModelUnit();
		this.modelQuantity = bomItem.getModelQuantity()*multiplier;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getPartDescription() {
		return partDescription;
	}

	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}

	public String getModelUnit() {
		return modelUnit;
	}

	public void setModelUnit(String modelUnit) {
		this.modelUnit = modelUnit;
	}

	public Double getModelQuantity() {
		return modelQuantity;
	}

	public void setModelQuantity(Double modelQuantity) {
		this.modelQuantity = modelQuantity;
	}

	public String getAnswerQuantity() {
		return answerQuantity;
	}

	public void setAnswerQuantity(String answerQuantity) {
		this.answerQuantity = answerQuantity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "BomSurveyItem [sequence=" + sequence + ", partCode=" + partCode + ", partDescription=" + partDescription
				+ ", modelUnit=" + modelUnit + ", modelQuantity=" + modelQuantity + ", answerQuantity=" + answerQuantity
				+ ", comment=" + comment + "]";
	}
	
	
	

}


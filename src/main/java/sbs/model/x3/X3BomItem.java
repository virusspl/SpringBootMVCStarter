package sbs.model.x3;

import java.io.Serializable;

public class X3BomItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int sequence;
	private String partCode;
	private String partDescription;
	private String modelUnit;
	private Double modelQuantity;
	
	public X3BomItem() {

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

	@Override
	public String toString() {
		return "X3BomItem [sequence=" + sequence + ", partCode=" + partCode + ", partDescription=" + partDescription
				+ ", modelUnit=" + modelUnit + ", modelQuantity=" + modelQuantity + "]";
	}
	
	
	

}

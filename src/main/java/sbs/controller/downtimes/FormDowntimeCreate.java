package sbs.controller.downtimes;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FormDowntimeCreate {

	@NotNull
	private String typeInternalTitle;
	@NotNull
	private int causeId;
	@Size(min=1, max=10)
	private String machineCode;
	@Size(max=255)
	private String comment;
	
	private String productCode;
	private String x3FailureNumber;
	
	public FormDowntimeCreate() {
	
	}
	
	public FormDowntimeCreate(String type ) {
		this.typeInternalTitle = type;
	}

	public int getCauseId() {
		return causeId;
	}

	public void setCauseId(int causeId) {
		this.causeId = causeId;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getX3FailureNumber() {
		return x3FailureNumber;
	}

	public void setX3FailureNumber(String x3FailureNumber) {
		this.x3FailureNumber = x3FailureNumber;
	}

	public String getTypeInternalTitle() {
		return typeInternalTitle;
	}

	public void setTypeInternalTitle(String typeInternalTitle) {
		this.typeInternalTitle = typeInternalTitle;
	}

	@Override
	public String toString() {
		return "FormDowntimeCreate [typeInternalTitle=" + typeInternalTitle + ", causeId=" + causeId + ", machineCode="
				+ machineCode + ", comment=" + comment + ", productCode=" + productCode + ", x3FailureNumber="
				+ x3FailureNumber + "]";
	}


	
}

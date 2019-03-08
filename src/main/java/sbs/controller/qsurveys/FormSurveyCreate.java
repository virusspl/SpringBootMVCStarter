package sbs.controller.qsurveys;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class FormSurveyCreate {
	
	@NotNull
	private int templateId;
	@NotEmpty
	private String operatorId;
	@NotEmpty
	private String orderNumberAndOperation;
	
	private boolean operatorManual;
	private String manualOperatorId;
	private String manualOperatorFirstName;
	private String manualOperatorLastName;
	
	@NotNull
	@Min(1)
	private int quantityChecked;
	
	private String comment;
	
	public FormSurveyCreate() {
	
	}
	
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOrderNumberAndOperation() {
		return orderNumberAndOperation;
	}
	public void setOrderNumberAndOperation(String orderNumberAndOperation) {
		this.orderNumberAndOperation = orderNumberAndOperation;
	}
	public boolean isOperatorManual() {
		return operatorManual;
	}
	public void setOperatorManual(boolean operatorManual) {
		this.operatorManual = operatorManual;
	}
	public String getManualOperatorId() {
		return manualOperatorId;
	}
	public void setManualOperatorId(String manualOperatorId) {
		this.manualOperatorId = manualOperatorId;
	}
	public String getManualOperatorFirstName() {
		return manualOperatorFirstName;
	}
	public void setManualOperatorFirstName(String manualOperatorFirstName) {
		this.manualOperatorFirstName = manualOperatorFirstName;
	}
	public String getManualOperatorLastName() {
		return manualOperatorLastName;
	}
	public void setManualOperatorLastName(String manualOperatorLastName) {
		this.manualOperatorLastName = manualOperatorLastName;
	}
	public int getQuantityChecked() {
		return quantityChecked;
	}
	public void setQuantityChecked(int quantityChecked) {
		this.quantityChecked = quantityChecked;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "FormSurveyCreate [templateId=" + templateId + ", operatorId=" + operatorId
				+ ", orderNumberAndOperation=" + orderNumberAndOperation + ", operatorManual=" + operatorManual
				+ ", manualOperatorId=" + manualOperatorId + ", manualOperatorFirstName=" + manualOperatorFirstName
				+ ", manualOperatorLastName=" + manualOperatorLastName + ", quantityChecked=" + quantityChecked + "]";
	}
	
	
	
	

	
	
	
	

}

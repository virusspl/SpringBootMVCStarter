package sbs.controller.qsurveys;

public class FormSurveyCreate {
	
	private int templateId;
	private String operatorId;
	private String orderNumberAndOperation;
	
	private boolean operatorManual;
	private String manualOperatorId;
	private String manualOperatorFirstName;
	private String manualOperatorLastName;
	
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
	@Override
	public String toString() {
		return "FormSurveyMain [templateId=" + templateId + ", operatorId=" + operatorId + ", orderNumberAndOperation="
				+ orderNumberAndOperation + ", operatorManual=" + operatorManual + ", manualOperatorId="
				+ manualOperatorId + ", manualOperatorFirstName=" + manualOperatorFirstName
				+ ", manualOperatorLastName=" + manualOperatorLastName + "]";
	}
	
	
	
	

}

package sbs.controller.qsurveys;

import sbs.model.qsurveys.QSurveyQuestion;

public class QuestionItem {
	
	private int typeId;
	private int questionId;
	private String typeCode;
	private int order;
	private String shortText;
	private String longText;
	private String referenceValue;
	private String currentValue;
	private boolean booleanValue;
	private String comment;
	
	public QuestionItem() {

	}
	
	public QuestionItem(QSurveyQuestion surveyQuestion){
		this.typeId = surveyQuestion.getType().getId();
		this.typeCode = surveyQuestion.getType().getCode();
		this.order = surveyQuestion.getOrder();
		this.shortText = surveyQuestion.getShortText();
		this.longText = surveyQuestion.getLongText();
		this.questionId = surveyQuestion.getId();
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getLongText() {
		return longText;
	}

	public void setLongText(String longText) {
		this.longText = longText;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	
	
		

}


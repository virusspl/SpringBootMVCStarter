package sbs.controller.qualitysurveys;

public class ParameterSurveyItem {

	private int parameterId;
	private String title;
	private char type;
	private String modelAnswer;
	private String answer;
	private boolean yesnoAnswer;
	private String comment;
	
	public ParameterSurveyItem() {
		
	}

	public int getParameterId() {
		return parameterId;
	}

	public void setParameterId(int parameterId) {
		this.parameterId = parameterId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getModelAnswer() {
		return modelAnswer;
	}

	public void setModelAnswer(String modelAnswer) {
		this.modelAnswer = modelAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isYesnoAnswer() {
		return yesnoAnswer;
	}

	public void setYesnoAnswer(boolean yesnoAnswer) {
		this.yesnoAnswer = yesnoAnswer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
	
}

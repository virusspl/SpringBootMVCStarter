package sbs.model.qsurveys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "qsurvey_answers")
public class QSurveyAnswer {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qsa_survey_id", nullable = false)
	private QSurvey survey;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qsa_question_type_id", nullable = false)
	private QSurveyQuestionType type;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qsa_answer_id")
	private int id;
	
	@Column(name = "qsa_order", nullable = false)
	private int order;
	
	@Column(name = "qsa_long_text", length = 255, nullable = false)
	private String longQuestionText;
	
	@Column(name = "qsa_short_text", length = 50, nullable = false)
	private String shortQuestionText;
	
	@Column(name = "qsa_reference_value", length = 50, nullable = true)
	private String referenceValue;
	
	@Column(name = "qsa_current_value", length = 50, nullable = true)
	private String value;
	
	@Column(name = "qsa_boolean_value", nullable = true)
	private Boolean booleanValue;
	
	@Column(name = "qsa_comment", length = 100, nullable = true)
	private String comment;
	
	public QSurveyAnswer() {
	
	}

	public QSurvey getSurvey() {
		return survey;
	}

	public void setSurvey(QSurvey survey) {
		this.survey = survey;
	}

	public QSurveyQuestionType getType() {
		return type;
	}

	public void setType(QSurveyQuestionType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getLongQuestionText() {
		return longQuestionText;
	}

	public void setLongQuestionText(String longQuestionText) {
		this.longQuestionText = longQuestionText;
	}

	public String getShortQuestionText() {
		return shortQuestionText;
	}

	public void setShortQuestionText(String shortQuestionText) {
		this.shortQuestionText = shortQuestionText;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "QSurveyAnswer [type=" + type + ", id=" + id + ", order=" + order + ", longQuestionText="
				+ longQuestionText + ", shortQuestionText=" + shortQuestionText + ", referenceValue=" + referenceValue
				+ ", value=" + value + ", booleanValue=" + booleanValue + ", comment=" + comment + "]";
	}
	
	
	
}

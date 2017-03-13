package sbs.model.qualitysurveys;

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
@Table(name = "quality_survey_parameter_answers")
public class QualitySurveyParameterAnswer implements Comparable<QualitySurveyParameterAnswer> {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qspa_qs_id", nullable = false)
	private QualitySurvey survey;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qspa_qsp_id", nullable = false)
	private QualitySurveyParameter parameter;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "qspa_id")
	private int id;
	
	@Column(name = "qspa_model_answer", length = 15, nullable=false)
	private String modelAnswer;
	
	@Column(name = "qspa_value_answer", length = 15, nullable=false)
	private String valueAnswer;
	
	@Column(name = "qspa_yesno_answer", nullable=false)
	private Boolean yesnoAnswer;
	
	@Column(name = "qspa_comment", length = 200, nullable=false)
	private String comment;
	
	public QualitySurveyParameterAnswer() {
		
	}

	public QualitySurvey getSurvey() {
		return survey;
	}

	public void setSurvey(QualitySurvey survey) {
		this.survey = survey;
	}

	public QualitySurveyParameter getParameter() {
		return parameter;
	}

	public void setParameter(QualitySurveyParameter parameter) {
		this.parameter = parameter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModelAnswer() {
		return modelAnswer;
	}

	public void setModelAnswer(String modelAnswer) {
		this.modelAnswer = modelAnswer;
	}

	public String getValueAnswer() {
		return valueAnswer;
	}

	public void setValueAnswer(String valueAnswer) {
		this.valueAnswer = valueAnswer;
	}

	public Boolean getYesnoAnswer() {
		return yesnoAnswer;
	}

	public void setYesnoAnswer(Boolean yesnoAnswer) {
		this.yesnoAnswer = yesnoAnswer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int compareTo(QualitySurveyParameterAnswer o) {
		return new Integer(this.id).compareTo(o.getId());
	}
}


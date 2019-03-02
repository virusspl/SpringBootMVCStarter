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
@Table(name = "qsurvey_bom_answers")
public class QSurveyBomAnswer {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qsba_survey_id", nullable = false)
	private QSurvey survey;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qsba_bom_answer_id")
	private int id;
	
	@Column(name = "qsba_seq", nullable = false)
	private int bomSeq;
	
	@Column(name = "qsba_part_code", length = 30, nullable = false)
	private String partCode;
	
	@Column(name = "qsba_part_description", length = 100, nullable = false)
	private String partDescription;
	
	@Column(name = "qsba_model_unit", length = 10, nullable = false)
	private String modelUnit;
	
	@Column(name = "qsba_model_quantity", nullable=false)
	private Double modelQuantity;
	
	@Column(name = "qsba_answer_quantity", nullable=false)
	private Double answerQuantity;
	
	@Column(name = "qsba_comment", length = 100, nullable = false)
	private String comment;
	
	public QSurveyBomAnswer() {
	
	}

	public QSurvey getSurvey() {
		return survey;
	}

	public void setSurvey(QSurvey survey) {
		this.survey = survey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBomSeq() {
		return bomSeq;
	}

	public void setBomSeq(int bomSeq) {
		this.bomSeq = bomSeq;
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

	public Double getAnswerQuantity() {
		return answerQuantity;
	}

	public void setAnswerQuantity(Double answerQuantity) {
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
		return "QSurveyBomAnswer [id=" + id + ", bomSeq=" + bomSeq + ", partCode=" + partCode + ", partDescription="
				+ partDescription + ", modelUnit=" + modelUnit + ", modelQuantity=" + modelQuantity
				+ ", answerQuantity=" + answerQuantity + ", comment=" + comment + "]";
	}
	
	
	
}

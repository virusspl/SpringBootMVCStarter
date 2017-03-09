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
@Table(name = "quality_survey_bom_answers")
public class QualitySurveyBomAnswer {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qsba_qs_id", nullable = false)
	private QualitySurvey survey;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "qsba_id")
	private int id;

	@Column(name = "qsba_seq", length = 10, nullable=false)
	private String sequence;

	@Column(name = "qsba_part_code", length = 25, nullable=false)
	private String partCode;

	@Column(name = "qsba_part_description", length = 100, nullable=false)
	private String partDescription;

	@Column(name = "qsba_model_unit", length = 10, nullable=false)
	private String modelUnit;
	
	@Column(name = "qsba_model_quantity", nullable=false)
	private Double modelQuantity;
	
	@Column(name = "qsba_answer_quantity", nullable=false)
	private Double answerQuantity;
		
	@Column(name = "qsba_comment", length = 200, nullable=false)
	private String comment;
	
	public QualitySurveyBomAnswer() {
		
	}

	public QualitySurvey getSurvey() {
		return survey;
	}

	public void setSurvey(QualitySurvey survey) {
		this.survey = survey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
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
		return "QualitySurveyBomAnswer [survey=" + survey + ", id=" + id + ", sequence=" + sequence + ", partCode="
				+ partCode + ", partDescription=" + partDescription + ", modelUnit=" + modelUnit + ", modelQuantity="
				+ modelQuantity + ", answerQuantity=" + answerQuantity + ", comment=" + comment + "]";
	}


}


package sbs.model.qualitysurveys;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "quality_survey_parameters")
public class QualitySurveyParameter {
	
	public static final int PARAMETER_TEXT = 0;
	public static final int PARAMETER_BOOLEAN = 1;
		
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
	Set<QualitySurveyParameterAnswer> answers;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "qsp_id")
	private int id;
	
	@Column(name = "qsp_active", nullable = false)
	private boolean active;
	
	@Column(name = "qsp_title", length = 45, nullable = false)
	private String title;
	
	@Column(name = "qsp_type", nullable = false)
	private int type;
	
	public QualitySurveyParameter() {
		answers = new HashSet<>();
	}

	public Set<QualitySurveyParameterAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<QualitySurveyParameterAnswer> answers) {
		this.answers = answers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static int getParameterText() {
		return PARAMETER_TEXT;
	}

	public static int getParameterBoolean() {
		return PARAMETER_BOOLEAN;
	}

	
	
}


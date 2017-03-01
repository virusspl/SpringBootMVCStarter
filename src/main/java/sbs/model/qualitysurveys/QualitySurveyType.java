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
@Table(name = "quality_survey_types")
public class QualitySurveyType {
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
	Set<QualitySurvey> surveys;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "qst_id")
	private int id;
	
	@Column(name = "qst_name", length =  45, nullable = false)
	private String name;
	
	public QualitySurveyType() {
		surveys = new HashSet<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<QualitySurvey> getSurveys() {
		return surveys;
	}

	public void setSurveys(Set<QualitySurvey> surveys) {
		this.surveys = surveys;
	}
	
	
}


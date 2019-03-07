package sbs.model.qsurveys;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "qsurvey_templates")
public class QSurveyTemplate {
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
	Set<QSurvey> surveys;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
	Set<QSurveyQuestion> questions;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qst_template_id")
	private int id;
	
	@Column(name = "qst_title", length = 255, nullable = false)
	private String title;
	
	@Column(name = "qst_description", nullable = true)
	@Type(type="text")
	private String description;
	
	@Column(name = "qst_comment", nullable = true)
	@Type(type="text")
	private String comment;
	
	@Column(name = "qst_active", nullable = false)
	private Boolean active;

	public QSurveyTemplate() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<QSurvey> getSurveys() {
		return surveys;
	}

	public void setSurveys(Set<QSurvey> surveys) {
		this.surveys = surveys;
	}

	public Set<QSurveyQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<QSurveyQuestion> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "QSurveyTemplate [id=" + id + ", title=" + title + ", description=" + description + ", comment="
				+ comment + ", active=" + active + "]";
	}
	
	
	
}

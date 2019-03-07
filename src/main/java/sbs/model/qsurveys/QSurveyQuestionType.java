package sbs.model.qsurveys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "qsurvey_question_types")
public class QSurveyQuestionType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qsqt_type_id")
	private int id;
	
	@Column(name = "qsqt_title", length = 255, nullable = false)
	private String title;
	
	@Column(name = "qsqt_type_code", length = 50, nullable = false)
	private String code;

	public QSurveyQuestionType() {
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "QSurveyQuestionType [id=" + id + ", title=" + title + ", code=" + code + "]";
	}
	
	
}

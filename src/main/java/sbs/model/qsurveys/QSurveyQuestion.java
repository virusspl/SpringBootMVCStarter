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
@Table(name = "qsurvey_questions")
public class QSurveyQuestion {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qsq_template_id", nullable = false)
	private QSurveyTemplate template;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qsq_type_id", nullable = false)
	private QSurveyQuestionType type;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qsq_question_id")
	private int id;
	
	@Column(name = "qsq_order", nullable = false)
	private int order;
	
	@Column(name = "qsq_long_text", length = 255, nullable = false)
	private String longText;
	
	@Column(name = "qsq_short_text", length = 50, nullable = false)
	private String shortText;
	
	public QSurveyQuestion() {
	
	}

	public QSurveyTemplate getTemplate() {
		return template;
	}

	public void setTemplate(QSurveyTemplate template) {
		this.template = template;
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

	public String getLongText() {
		return longText;
	}

	public void setLongText(String longText) {
		this.longText = longText;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@Override
	public String toString() {
		return "QSurveyQuestion [type=" + type + ", id=" + id + ", order=" + order + ", longText=" + longText
				+ ", shortText=" + shortText + "]";
	}
	
	
	
}

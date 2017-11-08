package sbs.model.wakesurvey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wake_questions")
public class WakeQuestion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "wq_id")
	private int id;
	
	@Column(name = "wq_text", length = 250, nullable = false)
	private String text;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "WakeQuestion [id=" + id + ", text=" + text + "]";
	}

}

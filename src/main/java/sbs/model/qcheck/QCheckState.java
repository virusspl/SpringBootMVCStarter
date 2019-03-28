package sbs.model.qcheck;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "qcheck_states")
public class QCheckState {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qcs_state_id")
	private int id;
	
	@Column(name = "qcs_internal_title", length = 50, nullable = false)
	private String title;
	
	@Column(name = "qcs_code", length = 50, nullable = false)
	private String code;
	
	@Column(name = "qcs_order", nullable = false)
	private int order;

	public QCheckState() {

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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "QCheckState [id=" + id + ", title=" + title + ", code=" + code + ", order=" + order + "]";
	}
	
	
	
	
	
	
	
	
}

package sbs.model.shipcust;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shipcust_states")
public class ShipCustState {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scs_state_id")
	private int id;
	
	@Column(name = "scs_internal_title", length = 50, nullable = false)
	private String title;
	
	@Column(name = "scs_code", length = 50, nullable = false)
	private String code;
	
	@Column(name = "scs_order", nullable = false)
	private int order;

	public ShipCustState() {

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
		return "ShipCustState [id=" + id + ", title=" + title + ", code=" + code + ", order=" + order + "]";
	}

	
}

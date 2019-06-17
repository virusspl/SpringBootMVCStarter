package sbs.model.shipments;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shipment_states")
public class ShipmentState {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ss_shipment_state_id")
	private int id;

	@Column(name = "ss_code", length = 50, nullable = false)
	private String code;

	@Column(name = "ss_order", nullable = false)
	private int order;
	
	@Column(name = "ss_internal_title", length = 50, nullable = false)
	private String internalTitle;

	
	public ShipmentState() {

	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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


	public String getInternalTitle() {
		return internalTitle;
	}


	public void setInternalTitle(String internalTitle) {
		this.internalTitle = internalTitle;
	}


	@Override
	public String toString() {
		return "ShipmentState [id=" + id + ", code=" + code + ", order=" + order + ", internalTitle=" + internalTitle
				+ "]";
	}
	
}

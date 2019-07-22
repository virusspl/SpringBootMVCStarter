package sbs.model.downtimes;



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
@Table(name = "downtime_causes")
public class DowntimeCause {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dtc_dtt_type_id", nullable = false)
	private DowntimeType downtimeType;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dtc_cause_id")
	private int id;
	
	@Column(name = "dtc_order", nullable = false)
	private int order;
	
	@Column(name = "dtc_text", length = 255, nullable = false)
	private String code;

	@Column(name = "dtc_active", nullable = false)
	private boolean active;
	
	public DowntimeCause() {
	
	}

	public DowntimeType getDowntimeType() {
		return downtimeType;
	}

	public void setDowntimeType(DowntimeType downtimeType) {
		this.downtimeType = downtimeType;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "DowntimeCause [downtimeType=" + downtimeType.getInternalTitle() + ", id=" + id + ", order=" + order + ", code=" + code
				+ ", active=" + active + "]";
	}
	
	

	
}

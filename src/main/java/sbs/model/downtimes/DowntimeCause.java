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
	private String text;
	
	@Column(name = "dtc_text_short", length = 100, nullable = false)
	private String shortText;

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@Override
	public String toString() {
		return "DowntimeCause [downtimeType=" + downtimeType + ", id=" + id + ", order=" + order + ", text=" + text
				+ ", shortText=" + shortText + ", active=" + active + "]";
	}


	
	

	
}

package sbs.model.downtimes;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "downtime_types")
public class DowntimeType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dtt_type_id")
	private int id;
	
	@Column(name = "dtt_code", length = 50, nullable = false)
	private String code;

	@Column(name = "dtt_internal_title", length = 50, nullable = false)
	private String internalTitle;
	
	@Column(name = "bhps_order")
	private int order;

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

	public String getInternalTitle() {
		return internalTitle;
	}

	public void setInternalTitle(String internalTitle) {
		this.internalTitle = internalTitle;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "DowntimeType [id=" + id + ", code=" + code + ", internalTitle=" + internalTitle + ", order=" + order
				+ "]";
	}

	
}

package sbs.model.downtimes;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "downtime_response_types")
public class DowntimeResponseType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dtrt_type_id")
	private int id;
	
	@Column(name = "dtrt_code", length = 50, nullable = false)
	private String code;

	@Column(name = "dtrt_internal_title", length = 50, nullable = false)
	private String internalTitle;
	
	@Column(name = "dtrt_order")
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
		return "DowntimeResponseType [id=" + id + ", code=" + code + ", internalTitle=" + internalTitle + ", order=" + order
				+ "]";
	}

	
}

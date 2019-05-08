package sbs.model.system;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_info_parameters")
public class SystemInfoParameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sip_id")
	private int id;

	@Column(name = "sip_code", length = 50, nullable = false)
	private String code;
	
	@Column(name = "sip_description", length = 255, nullable = false)
	private String description;
	
	@Column(name = "sip_value", length = 255, nullable = false)
	private String value;
	
	@Column(name = "sip_date", nullable = false)
	private Timestamp date;
	
	public SystemInfoParameter() {
		
	}
	
	public SystemInfoParameter(String code, String description, String value, Timestamp date) {
		super();
		this.code = code;
		this.description = description;
		this.value = value;
		this.date = date;
	}
	
	public SystemInfoParameter(String code, String description, String value, java.util.Date date) {
		super();
		this.code = code;
		this.description = description;
		this.value = value;
		this.date = new Timestamp(date.getTime());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SystemInfoParameter [id=" + id + ", code=" + code + ", description=" + description + ", value=" + value
				+ ", date=" + date + "]";
	}
	
}

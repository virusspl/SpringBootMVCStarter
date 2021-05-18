package sbs.model.payterm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payterm_parameters")
public class PaymentTermsParameter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pp_id")
	private int id;

	@Column(name = "pp_code", length = 25, nullable = false)
	private String code;
	
	@Column(name = "pp_description", length = 50, nullable = false)
	private String description;
	
	@Column(name = "pp_value", length = 255, nullable = false)
	private String value;
	
	public PaymentTermsParameter() {
		
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

	@Override
	public String toString() {
		return "PaymentTermsParameters [id=" + id + ", code=" + code + ", description=" + description + ", value="
				+ value + "]";
	}
	
}

package sbs.model.compreq;

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
@Table(name = "component_request_lines")
public class ComponentRequestLine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rc_request_line_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rc_cr_request_id", nullable = false)
	private ComponentRequest request;
	
	@Column(name = "rc_component_code", length = 25, nullable = false)
	private String componentCode;
	
	@Column(name = "rc_component_description", length = 150, nullable = false)
	private String componentDescription;
	
	@Column(name = "rc_component_category", length = 5, nullable = false)
	private String componentCategory;

	@Column(name = "rc_quantity", nullable = false)
	private int quantity;
	
	public ComponentRequestLine() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ComponentRequest getRequest() {
		return request;
	}

	public void setRequest(ComponentRequest request) {
		this.request = request;
	}

	public String getComponentCode() {
		return componentCode;
	}

	public void setComponentCode(String componentCode) {
		this.componentCode = componentCode;
	}

	public String getComponentDescription() {
		return componentDescription;
	}

	public void setComponentDescription(String componentDescription) {
		this.componentDescription = componentDescription;
	}

	public String getComponentCategory() {
		return componentCategory;
	}

	public void setComponentCategory(String componentCategory) {
		this.componentCategory = componentCategory;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ComponentRequestLine [id=" + id + ", componentCode=" + componentCode
				+ ", componentDescription=" + componentDescription + ", componentCategory=" + componentCategory
				+ ", quantity=" + quantity + "]";
	}
	
	 


	
}

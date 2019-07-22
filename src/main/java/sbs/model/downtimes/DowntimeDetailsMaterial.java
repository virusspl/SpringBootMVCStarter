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
@Table(name = "downtime_details_material")
public class DowntimeDetailsMaterial {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dtdm_details_id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dtdm_downtime_id", nullable = false)
	private Downtime downtime;
	
	@Column(name = "dtdm_product_code", length = 25, nullable = false)
	private String productCode;
	
	@Column(name = "dtdm_product_description", length = 150, nullable = false)
	private String productDescription;
	
	@Column(name = "dtdm_product_category", length = 5, nullable = false)
	private String productCategory;
	
	public DowntimeDetailsMaterial() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Downtime getDowntime() {
		return downtime;
	}

	public void setDowntime(Downtime downtime) {
		this.downtime = downtime;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	@Override
	public String toString() {
		return "DowntimeDetailsMaterial [id=" + id + ", productCode=" + productCode + ", productDescription="
				+ productDescription + ", productCategory=" + productCategory + "]";
	}
	
	
}

package sbs.model.shipments;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sbs.model.users.User;

@Entity
@Table(name = "shipment_lines")
public class ShipmentLine {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sl_shipment_line_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sl_sh_id_shipment", nullable = false)
	private Shipment shipment;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sl_usr_id_creator", nullable = false)
	private User creator;	
	 
	@Column(name = "sl_time", nullable = false)
	private Timestamp creationTime;

	@Column(name = "sl_product_code", length = 20, nullable = false)
	private String productCode;
	
	@Column(name = "sl_product_description", length = 110, nullable = false)
	private String productDescription;
	
	@Column(name = "sl_product_category", length = 5, nullable = false)
	private String productCategory;
	
	@Column(name = "sl_sales_order", length = 20, nullable = false)
	private String salesOrder;
	
	@Column(name = "sl_quantity", nullable = false)
	private int quantity;
	
	
	public ShipmentLine() {

	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Shipment getShipment() {
		return shipment;
	}


	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}


	public Timestamp getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
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


	public String getSalesOrder() {
		return salesOrder;
	}


	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

	public User getCreator() {
		return creator;
	}


	public void setCreator(User creator) {
		this.creator = creator;
	}


	@Override
	public String toString() {
		return "ShipmentLine [id=" + id + ", creator=" + creator.getName() + ", creationTime=" + creationTime + ", productCode="
				+ productCode + ", productDescription=" + productDescription + ", productCategory=" + productCategory
				+ ", salesOrder=" + salesOrder + ", quantity=" + quantity + "]";
	}





}

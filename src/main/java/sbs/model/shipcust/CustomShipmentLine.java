package sbs.model.shipcust;

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

@Entity
@Table(name = "shipcust_lines")
public class CustomShipmentLine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scl_line_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scl_shipment_id", nullable = false)
	private CustomShipment shipment;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "scl_shipment_line_state_id", nullable = false)
	private ShipCustLineState state;
	
	@Column(name = "scl_product_code", length = 45, nullable = false)
	private String productCode;
	@Column(name = "scl_product_description", length = 150, nullable = false)
	private String productDescription;
	
	@Column(name = "scl_quantity_demanded", nullable = false)
	private int quantityDemanded;
	@Column(name = "scl_quantity_shipped", nullable = false)
	private int quantityShipped;
	
	@Column(name = "scl_sales_order", length = 25, nullable = false)
	private String salesOrder;
	@Column(name = "scl_waybill", length = 50, nullable = true)
	private String waybill;
	
	@Column(name = "scl_load_addition", nullable = false)
	private boolean addition;
	@Column(name = "scl_quality_certificate", nullable = false)
	private boolean certificate;
	@Column(name = "scl_spare_flag", nullable = false)
	private boolean spare;

	@Column(name = "scl_sales_comment", length = 150, nullable = true)
	private String salesComment;
	@Column(name = "scl_spare_comment", length = 150, nullable = true)
	private String spareComment;
	@Column(name = "scl_shipment_comment", length = 150, nullable = true)
	private String shipmentComment;
	
	@Column(name = "scl_sales_action_date", nullable = false)
	private Timestamp salesActionDate;
	@Column(name = "scl_spare_action_date", nullable = true)
	private Timestamp spareActionDate;
	@Column(name = "scl_shipment_action_date", nullable = true)
	private Timestamp shipmentActionDate;
	
	@Column(name = "scl_sales_action_user_name", length = 30, nullable = false)
	private Timestamp salesActionUserName;
	@Column(name = "scl_spare_action_user_name", length = 30, nullable = true)
	private Timestamp spareActionUserName;
	@Column(name = "scl_shipment_action_user_name", length = 30, nullable = true)
	private Timestamp shipmentActionUserName;	
	
	public CustomShipmentLine() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CustomShipment getShipment() {
		return shipment;
	}

	public void setShipment(CustomShipment shipment) {
		this.shipment = shipment;
	}

	public ShipCustLineState getState() {
		return state;
	}

	public void setState(ShipCustLineState state) {
		this.state = state;
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

	public int getQuantityDemanded() {
		return quantityDemanded;
	}

	public void setQuantityDemanded(int quantityDemanded) {
		this.quantityDemanded = quantityDemanded;
	}

	public int getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(int quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getWaybill() {
		return waybill;
	}

	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}

	public boolean isAddition() {
		return addition;
	}

	public void setAddition(boolean addition) {
		this.addition = addition;
	}

	public boolean isCertificate() {
		return certificate;
	}

	public void setCertificate(boolean certificate) {
		this.certificate = certificate;
	}

	public boolean isSpare() {
		return spare;
	}

	public void setSpare(boolean spare) {
		this.spare = spare;
	}

	public String getSalesComment() {
		return salesComment;
	}

	public void setSalesComment(String salesComment) {
		this.salesComment = salesComment;
	}

	public String getSpareComment() {
		return spareComment;
	}

	public void setSpareComment(String spareComment) {
		this.spareComment = spareComment;
	}

	public String getShipmentComment() {
		return shipmentComment;
	}

	public void setShipmentComment(String shipmentComment) {
		this.shipmentComment = shipmentComment;
	}

	public Timestamp getSalesActionDate() {
		return salesActionDate;
	}

	public void setSalesActionDate(Timestamp salesActionDate) {
		this.salesActionDate = salesActionDate;
	}

	public Timestamp getSpareActionDate() {
		return spareActionDate;
	}

	public void setSpareActionDate(Timestamp spareActionDate) {
		this.spareActionDate = spareActionDate;
	}

	public Timestamp getShipmentActionDate() {
		return shipmentActionDate;
	}

	public void setShipmentActionDate(Timestamp shipmentActionDate) {
		this.shipmentActionDate = shipmentActionDate;
	}

	public Timestamp getSalesActionUserName() {
		return salesActionUserName;
	}

	public void setSalesActionUserName(Timestamp salesActionUserName) {
		this.salesActionUserName = salesActionUserName;
	}

	public Timestamp getSpareActionUserName() {
		return spareActionUserName;
	}

	public void setSpareActionUserName(Timestamp spareActionUserName) {
		this.spareActionUserName = spareActionUserName;
	}

	public Timestamp getShipmentActionUserName() {
		return shipmentActionUserName;
	}

	public void setShipmentActionUserName(Timestamp shipmentActionUserName) {
		this.shipmentActionUserName = shipmentActionUserName;
	}

	@Override
	public String toString() {
		return "CustomShipmentLine [id=" + id + ", shipment=" + shipment + ", state=" + state + ", productCode="
				+ productCode + ", productDescription=" + productDescription + ", quantityDemanded=" + quantityDemanded
				+ ", quantityShipped=" + quantityShipped + ", salesOrder=" + salesOrder + ", waybill=" + waybill
				+ ", addition=" + addition + ", certificate=" + certificate + ", spare=" + spare + ", salesComment="
				+ salesComment + ", spareComment=" + spareComment + ", shipmentComment=" + shipmentComment
				+ ", salesActionDate=" + salesActionDate + ", spareActionDate=" + spareActionDate
				+ ", shipmentActionDate=" + shipmentActionDate + ", salesActionUserName=" + salesActionUserName
				+ ", spareActionUserName=" + spareActionUserName + ", shipmentActionUserName=" + shipmentActionUserName
				+ "]";
	}
	
	
	
	
}

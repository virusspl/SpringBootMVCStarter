package sbs.model.inventory;

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
@Table(name = "inventory_entries")
public class InventoryEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ie_id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ie_inv_id", nullable = false)
	private Inventory inventory;
	
	@Column(name = "ie_line", nullable = false)
	private int line;
	
	@Column(name = "ie_credat", nullable = false)
	private Timestamp creationDate;
	
	@Column(name = "ie_user_code", length = 25, nullable = false)
	private String userCode;
	
	@Column(name = "ie_user_name", length = 50, nullable = false)
	private String userName;	
	
	@Column(name = "ie_code", length = 45, nullable = true)
	private String code;	
	
	@Column(name = "ie_address", length = 15, nullable = true)
	private String address;	
	
	@Column(name = "ie_location", length = 25, nullable = true)
	private String location;	
	
	@Column(name = "ie_label", length = 15, nullable = true)
	private String label;	
	
	@Column(name = "ie_order_sale", length = 30, nullable = true)
	private String saleOrder;	
	
	@Column(name = "ie_order_purchase", length = 30, nullable = true)
	private String purchaseOrder;	
	
	@Column(name = "ie_package_type", length = 15, nullable = true)
	private String packageType;
	
	@Column(name = "ie_quantity", nullable = true)
	private double quantity;
	
	@Column(name = "ie_free_string_1", length = 45, nullable = true)
	private String freeString1;
	
	@Column(name = "ie_free_string_2", length = 45, nullable = true)
	private String freeString2;
	
	@Column(name = "ie_free_double", nullable = true)
	private double freeDouble;	
	
	public InventoryEntry() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(String saleOrder) {
		this.saleOrder = saleOrder;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getFreeString1() {
		return freeString1;
	}

	public void setFreeString1(String freeString1) {
		this.freeString1 = freeString1;
	}

	public String getFreeString2() {
		return freeString2;
	}

	public void setFreeString2(String freeString2) {
		this.freeString2 = freeString2;
	}

	public double getFreeDouble() {
		return freeDouble;
	}

	public void setFreeDouble(double freeDouble) {
		this.freeDouble = freeDouble;
	}
	
	
	
}

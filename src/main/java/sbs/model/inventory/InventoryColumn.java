package sbs.model.inventory;

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
@Table(name = "inventory_columns")
public class InventoryColumn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ic_id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ic_inv_id", nullable = false)
	private Inventory inventory;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ic_idt_id", nullable = false)
	private InventoryDataType inventoryDataType;
	
	@Column(name = "ic_order", nullable = false)
	private int order;
	
	@Column(name = "ic_recquired", nullable = false)
	private Boolean recquired;
	
	@Column(name = "ic_validated", nullable = false)
	private Boolean validated;

	@Column(name = "ic_column_name", length = 50, nullable = false)
	private String columnName;

	public InventoryColumn() {
	
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

	public InventoryDataType getInventoryDataType() {
		return inventoryDataType;
	}

	public void setInventoryDataType(InventoryDataType inventoryDataType) {
		this.inventoryDataType = inventoryDataType;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Boolean getRecquired() {
		return recquired;
	}

	public void setRecquired(Boolean recquired) {
		this.recquired = recquired;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	

}

package sbs.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inventory_data_types")
public class InventoryDataType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idt_id")
	private int id;
	
	@Column(name = "idt_type_code", length = 50, nullable = false)
	private String columnTypeCode;

	public InventoryDataType() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColumnTypeCode() {
		return columnTypeCode;
	}

	public void setColumnTypeCode(String columnTypeCode) {
		this.columnTypeCode = columnTypeCode;
	}
	
}

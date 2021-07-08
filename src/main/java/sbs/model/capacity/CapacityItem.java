package sbs.model.capacity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "capacity_items")
public class CapacityItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpc_id")
	private int id;

	@Column(name = "cpc_date", nullable = false)
	private Timestamp date;
	
	@Column(name = "cpc_quantity", nullable = false)
	private int quantity;
	
	@Column(name = "cpc_department", length = 45, nullable = false)
	private String department;
	
	@Column(name = "cpc_machine", length = 45, nullable = false)
	private String machine;
	
	public CapacityItem() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	@Override
	public String toString() {
		return "CapacityItem [id=" + id + ", date=" + date + ", quantity=" + quantity + ", department=" + department
				+ ", machine=" + machine + "]";
	}
	
	
	
}

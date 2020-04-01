package sbs.model.shipcust;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shipcust_transport")
public class ShipCustTransport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sct_transport_id")
	private int id;
	
	@Column(name = "sct_transport_name", length = 150, nullable = false)
	private String name;

	@Column(name = "sct_active", nullable = false)
	private boolean active;

	public ShipCustTransport() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "ShipCustTransport [id=" + id + ", name=" + name + ", active=" + active + "]";
	}

	
}

package sbs.model.inventory;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import sbs.model.users.User;

@Entity
@Table(name = "inventory")
public class Inventory {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inventory")
	Set<InventoryEntry> entries;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "inventory")
	Set<InventoryColumn> columns;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inv_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "inv_creuser_id", nullable = false)
	private User creator;
	
	@Column(name = "inv_credat", nullable = false)
	private Timestamp creationDate;
	
	@Column(name = "inv_title", length = 255, nullable = false)
	private String title;
	
	@Column(name = "inv_company", length = 15, nullable = false)
	private String company;
	
	@Column(name = "inv_description", nullable = true)
	@Type(type="text")
	private String description;
	
	@Column(name = "inv_inventory_date", nullable = false)
	private Timestamp inventoryDate;
	
	@Column(name = "inv_active", nullable = false)
	private Boolean active;
	
	@Column(name = "inv_next_line", nullable = false)
	private int nextLine;	
	
	public Inventory() {
	
	}

	public Set<InventoryEntry> getEntries() {
		return entries;
	}

	public void setEntries(Set<InventoryEntry> entries) {
		this.entries = entries;
	}

	public Set<InventoryColumn> getColumns() {
		return columns;
	}

	public void setColumns(Set<InventoryColumn> columns) {
		this.columns = columns;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getInventoryDate() {
		return inventoryDate;
	}

	public void setInventoryDate(Timestamp inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public int getNextLine() {
		return nextLine;
	}

	public void setNextLine(int nextLine) {
		this.nextLine = nextLine;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	
	
}

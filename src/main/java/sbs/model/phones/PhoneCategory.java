package sbs.model.phones;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "phone_categories")
public class PhoneCategory {
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	Set<PhoneEntry> phoneEntries;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pc_id")
	private int id;
	
	@Column(name = "pc_name", length = 45, nullable = false)
	private String name;
	
	@Column(name = "pc_order", nullable= false)
	private int order;
	
	@Column(name = "pc_version", length = 5, nullable= false)
	private String version;
	
	public PhoneCategory() {
		phoneEntries = new HashSet<>();
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Set<PhoneEntry> getPhoneEntries() {
		return phoneEntries;
	}

	public void setPhoneEntries(Set<PhoneEntry> phoneEntries) {
		this.phoneEntries = phoneEntries;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "PhoneCategory [id=" + id + ", name=" + name + ", order=" + order + "]";
	}
	
	


}

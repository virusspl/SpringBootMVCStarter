package sbs.model.phones;

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
@Table(name = "phone_entries")
public class PhoneEntry {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pe_category_id", nullable = false)
	private PhoneCategory category;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pe_id")
	private int id;
	
	@Column(name = "pe_name", length = 50, nullable = true)
	private String name;
	
	@Column(name = "pe_position", length = 50, nullable = true)
	private String position;
	
	@Column(name = "pe_email", length = 50, nullable = true)
	private String email;
	
	@Column(name = "pe_number", nullable = true)
	private int number;

	public PhoneCategory getCategory() {
		return category;
	}

	public void setCategory(PhoneCategory category) {
		this.category = category;
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "PhoneEntry [category=" + category + ", id=" + id + ", name=" + name + ", number=" + number + "]";
	}
	

}

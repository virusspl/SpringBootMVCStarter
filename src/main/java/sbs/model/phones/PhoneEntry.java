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
	
	@Column(name = "pe_email", length = 75, nullable = true)
	private String email;
	
	@Column(name = "pe_note", length = 50, nullable = true)
	private String note;
	
	@Column(name = "pe_number", length = 45, nullable = true)
	private String number;
	
	@Column(name = "pe_number_short", length = 10, nullable = true)
	private String numberShort;
	
	@Column(name = "pe_number_internal", length = 10, nullable = true)
	private String numberInternal;
	
	@Column(name = "pe_number_internal_portable", length = 10, nullable = true)
	private String numberInternalPortable;
	
	@Column(name = "pe_voip", length = 20, nullable = true)
	private String voip;
	
	@Column(name = "pc_version", length = 5, nullable= false)
	private String version;

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

	public String getNumber() {
		return number;
	}

	public String getVoip() {
		return voip;
	}

	public void setVoip(String voip) {
		this.voip = voip;
	}

	public void setNumber(String number) {
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNumberShort() {
		return numberShort;
	}

	public void setNumberShort(String numberShort) {
		this.numberShort = numberShort;
	}

	public String getNumberInternal() {
		return numberInternal;
	}

	public void setNumberInternal(String numberInternal) {
		this.numberInternal = numberInternal;
	}

	public String getNumberInternalPortable() {
		return numberInternalPortable;
	}

	public void setNumberInternalPortable(String numberInternalPortable) {
		this.numberInternalPortable = numberInternalPortable;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "PhoneEntry [category=" + category + ", id=" + id + ", name=" + name + ", number=" + number + "]";
	}
	

}

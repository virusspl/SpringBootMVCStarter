package sbs.model.hruafiles;

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

import org.hibernate.annotations.Type;

import sbs.model.users.User;

@Entity
@Table(name = "hrua_files")
public class HrUaInfo {

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hrua_creusr", nullable = false)
	private User creator;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "hrua_id")
	private int id;
	
	@Column(name = "hrua_name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "hrua_last_name", length = 50, nullable = false)
	private String lastName;
	
	@Column(name = "hrua_passport_no", length = 50, nullable = false)
	private String passportNo;
	
	@Column(name = "hrua_vista_start_date", nullable = false)
	private Timestamp visaStartDate;	

	@Column(name = "hrua_vista_end_date", nullable = false)
	private Timestamp visaEndDate;
	
	@Column(name = "hrua_phone", length = 30, nullable = true)
	private String phone;	
	
	@Column(name = "hrua_address", length = 200, nullable = true)
	private String address;
	
	@Column(name = "hrua_comment", nullable = true)
	@Type(type="text")
	private String comment;
	
	@Column(name = "hrua_mother_name", length = 50, nullable = true)
	private String motherName;
	
	@Column(name = "hrua_mother_maiden_name", length = 50, nullable = true)
	private String motherMaidenName;
	
	@Column(name = "hrua_father_name", length = 50, nullable = true)
	private String fatherName;
	
	@Column(name = "hrua_credat", nullable = false)
	private Timestamp creationDate;
	
	@Column(name = "hrua_archive", nullable=false)
	private Boolean archive;

	@Column(name = "hrua_tmp_visa_date", nullable = true)
	private Timestamp temporaryVisaDate;
	
	public HrUaInfo() {
		
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public Timestamp getVisaStartDate() {
		return visaStartDate;
	}

	public void setVisaStartDate(Timestamp visaStartDate) {
		this.visaStartDate = visaStartDate;
	}

	public Timestamp getVisaEndDate() {
		return visaEndDate;
	}

	public void setVisaEndDate(Timestamp visaEndDate) {
		this.visaEndDate = visaEndDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getMotherMaidenName() {
		return motherMaidenName;
	}

	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public Timestamp getTemporaryVisaDate() {
		return temporaryVisaDate;
	}

	public void setTemporaryVisaDate(Timestamp temporaryVisaDate) {
		this.temporaryVisaDate = temporaryVisaDate;
	}

	
	
}

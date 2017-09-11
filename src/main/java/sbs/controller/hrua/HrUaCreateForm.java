package sbs.controller.hrua;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


public class HrUaCreateForm {

	/*
	 * mandatory
	 * 
	 */
	
	@NotEmpty
	private String name;
	@NotEmpty
	private String lastName;
	@NotEmpty
	private String passportNo;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date visaStartDate;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date visaEndDate;

	/*
	 * optional
	 * 
	 */
	
	private String phone;	
	private String address;
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date temporaryVisaDate;
	
	private String fatherName;
	private String motherName;
	private String motherMaidenName;
	
	private Boolean archive;
	private String comment;

	public HrUaCreateForm() {
		
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

	public Date getVisaStartDate() {
		return visaStartDate;
	}

	public void setVisaStartDate(Date visaStartDate) {
		this.visaStartDate = visaStartDate;
	}

	public Date getVisaEndDate() {
		return visaEndDate;
	}

	public void setVisaEndDate(Date visaEndDate) {
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

	public Date getTemporaryVisaDate() {
		return temporaryVisaDate;
	}

	public void setTemporaryVisaDate(Date temporaryVisaDate) {
		this.temporaryVisaDate = temporaryVisaDate;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
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

	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	

}








	
		

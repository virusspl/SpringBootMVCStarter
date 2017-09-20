package sbs.controller.hrua;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import sbs.model.hruafiles.HrUaInfo;

public class HrUaCreateForm {

	private int id;
	/*
	 * mandatory
	 * 
	 */
	@NotEmpty
	@Size(max = 50)
	private String name;
	@NotEmpty
	@Size(max = 50)
	private String lastName;
	@NotEmpty
	@Size(max = 50)
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
	@Size(max = 30)
	private String phone;
	@Size(max = 200)
	private String address;
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date temporaryVisaDate;
	
	@Size(max = 50)
	private String fatherName;
	@Size(max = 50)
	private String motherName;
	@Size(max = 50)
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
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static HrUaCreateForm hrUaCreateFormFromHrUaInfo(HrUaInfo object){
		HrUaCreateForm form = new HrUaCreateForm();
		form.setId(object.getId());
		form.setName(object.getName());
		form.setLastName(object.getLastName());
		form.setPassportNo(object.getPassportNo());
		form.setVisaStartDate(new Date(object.getVisaStartDate().getTime()));
		form.setVisaEndDate(new Date(object.getVisaEndDate().getTime()));
		form.setPhone(object.getPhone());
		if(object.getTemporaryVisaDate()!=null){
			form.setTemporaryVisaDate(new Date(object.getTemporaryVisaDate().getTime()));
		}
		form.setAddress(object.getAddress());
		form.setFatherName(object.getFatherName());
		form.setMotherName(object.getMotherName());
		form.setMotherMaidenName(object.getMotherMaidenName());
		form.setComment(object.getComment());
		form.setArchive(object.getArchive());
		
		return form;
	}

	@Override
	public String toString() {
		return "HrUaCreateForm [id=" + id + ", name=" + name + ", lastName=" + lastName + ", passportNo=" + passportNo
				+ ", visaStartDate=" + visaStartDate + ", visaEndDate=" + visaEndDate + ", phone=" + phone
				+ ", address=" + address + ", temporaryVisaDate=" + temporaryVisaDate + ", fatherName=" + fatherName
				+ ", motherName=" + motherName + ", motherMaidenName=" + motherMaidenName + ", archive=" + archive
				+ ", comment=" + comment + "]";
	}
	
	
	
}








	
		

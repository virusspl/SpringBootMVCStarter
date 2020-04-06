package sbs.controller.shipcust;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class FormCreateCustomShipment {

	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date startDate;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date endDate;
	@NotNull
	private int transportId;
	@Size(min=3, max=10)
	private String clientCode;
	@Size(max=255)
	private String comment;

	
	public FormCreateCustomShipment() {
		this.startDate = new Date();
		this.endDate = new Date();
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public int getTransportId() {
		return transportId;
	}


	public void setTransportId(int transportId) {
		this.transportId = transportId;
	}


	public String getClientCode() {
		return clientCode;
	}


	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	@Override
	public String toString() {
		return "FormCreateCustomShipment [startDate=" + startDate + ", endDate=" + endDate + ", transportId="
				+ transportId + ", clientCode=" + clientCode + ", comment=" + comment + "]";
	}
	
	
	
	
}

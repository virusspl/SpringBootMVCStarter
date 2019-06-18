package sbs.controller.shipments;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class ShipmentCreateForm {

	private String company;
	@Size(min = 3, max = 15)
	String client;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date date;
	String description;
	
	public ShipmentCreateForm(String company, Date date) {
		this.company = company;
		this.date = date;
	}
	
	public ShipmentCreateForm() {
		
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ShipmentCreateForm [company=" + company + ", client=" + client + ", date=" + date + "]";
	}
	
	
	
	
}

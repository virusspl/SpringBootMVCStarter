package sbs.controller.bhptickets;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class BhpTicketsSummaryForm {
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date startDate;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date endDate;
	private String company;
	
	public BhpTicketsSummaryForm() {
		this.company = "ATW";
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}	
	
	
}

package sbs.controller.qsurveys;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class QSurveyListForm {
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date startDate;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date endDate;
	
	public QSurveyListForm() {
	
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
	
}

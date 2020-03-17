package sbs.controller.prodcomp;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class FormComponent {

	@Size(min=3)
	private String component;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date startDate;
	@NotNull
	@DateTimeFormat(pattern= "dd.MM.yyyy")
	private Date endDate;
	
	public FormComponent() {
	
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
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

	@Override
	public String toString() {
		return "FormComponent [component=" + component + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}


	
}

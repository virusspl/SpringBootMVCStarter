package sbs.controller.saleship;

import java.util.Calendar;
import java.util.Date;

public class SaleShipForm {
	
	private Date startDate;
	private Date endDate;
	
	public SaleShipForm() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		startDate = cal.getTime();
		cal.add(Calendar.YEAR, 12);
		//cal.add(Calendar.MONTH, 1);
		endDate = cal.getTime();
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

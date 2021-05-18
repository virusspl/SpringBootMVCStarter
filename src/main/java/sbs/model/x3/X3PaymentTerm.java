package sbs.model.x3;

import java.util.Calendar;
import java.util.Date;

public class X3PaymentTerm {

	String code;
	String description;
	int months;
	int days;
	boolean endOfMonth;
	
	public X3PaymentTerm() {
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEndOfMonth() {
		return endOfMonth;
	}

	public void setEndOfMonth(boolean endOfMonth) {
		this.endOfMonth = endOfMonth;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return "X3PaymentTerm [code=" + code + ", description=" + description + ", months=" + months + ", days=" + days
				+ ", endOfMonth=" + endOfMonth + "]";
	}
	
	public Date getPaymentDate(Date startDate) {
		Calendar cal  = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MONTH, this.months);
		cal.add(Calendar.DAY_OF_MONTH, this.days);
		if(this.endOfMonth) {
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}


}

package sbs.controller.payterm;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class PaymentTermsForm {
	
	@Size(min = 3, max = 255)
	private String mailingList;
	@Size(min = 3, max = 255)
	private String clientsList;
	@Min(0)
	@Max(120)
	private int days;
	
	public PaymentTermsForm() {
		
	}

	public String getMailingList() {
		return mailingList;
	}

	public void setMailingList(String mailingList) {
		this.mailingList = mailingList;
	}

	public String getClientsList() {
		return clientsList;
	}

	public void setClientsList(String clientsList) {
		this.clientsList = clientsList;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return "PaymentTermsForm [mailingList=" + mailingList + ", clientsList=" + clientsList + ", days=" + days + "]";
	}
	

}

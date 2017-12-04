package sbs.controller.utr;

import java.sql.Timestamp;

public class UtrDispatchForm {
	Timestamp startDate;
	Timestamp endDate;
	int critical;
	int stop;
	
	public UtrDispatchForm() {
	
	}


	public Timestamp getStartDate() {
		return startDate;
	}


	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}


	public Timestamp getEndDate() {
		return endDate;
	}


	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}


	public int getCritical() {
		return critical;
	}


	public void setCritical(int critical) {
		this.critical = critical;
	}


	public int getStop() {
		return stop;
	}


	public void setStop(int stop) {
		this.stop = stop;
	}
	
	
	
	
}

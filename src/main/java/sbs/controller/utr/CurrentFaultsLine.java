package sbs.controller.utr;

import java.sql.Timestamp;

import sbs.model.x3.X3UtrMachine;

public class CurrentFaultsLine implements Comparable<CurrentFaultsLine> {

	private X3UtrMachine machine;
	private String faults;
	private Timestamp inputDate;

	public CurrentFaultsLine() {
		this.faults = "";
	}
	
	public CurrentFaultsLine(X3UtrMachine machine) {
		this.machine = machine;
		this.faults = "";
	}

	public void appendFault(String faultNo) {
		if (this.faults.length() > 0) {
			this.faults = this.faults + "; " + faultNo;
		} 
		else {
			this.faults = faultNo;
		}
	}

	public X3UtrMachine getMachine() {
		return machine;
	}

	public void setMachine(X3UtrMachine machine) {
		this.machine = machine;
	}

	public String getFaults() {
		return faults;
	}

	public void setFaults(String faults) {
		this.faults = faults;
	}

	
	public Timestamp getInputDate() {
		return inputDate;
	}

	public void setInputDate(Timestamp inputDate) {
		// set older date
		if(this.inputDate == null || this.inputDate.after(inputDate)){
			this.inputDate = inputDate;
		}
	}

	@Override
	public String toString() {
		return "CurrentFaultsLine [machine=" + machine + ", faults=" + faults + "]";
	}

	@Override
	public int compareTo(CurrentFaultsLine o) {
		return this.inputDate.compareTo(o.getInputDate());
	}
	

	

}

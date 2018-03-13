package sbs.controller.utr;

import sbs.model.x3.X3UtrMachine;

public class ChartLine {

	private int length;
	private X3UtrMachine machine;
	private String[] values;

	
	public ChartLine(X3UtrMachine machine, int days) {
		this.machine = machine;
		this.length = days;
		this.values = new String[this.length];
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public X3UtrMachine getMachine() {
		return machine;
	}


	public void setMachine(X3UtrMachine machine) {
		this.machine = machine;
	}


	public String[] getValues() {
		return values;
	}


	public void setValues(String[] values) {
		this.values = values;
	}
	
	public void setValueAt(int index, String value) {
		this.values[index] = value;
	}

	
}

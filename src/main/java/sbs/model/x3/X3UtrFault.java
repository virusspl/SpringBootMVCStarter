package sbs.model.x3;

import java.sql.Timestamp;
import java.util.ArrayList;

public class X3UtrFault {

	private String faultNumber;
	private String creatorCode;
	private String creatorName;
	private Timestamp creationDate;
	private String machineCode;
	private String locationName;
	
	// filled from maps;
	private X3UtrMachine machine;
	private ArrayList<X3UtrFaultLine> lines;
	
	// methods
	// getInputDateTime;
	// getCloseDateTime;
	// getTotalWorkTimeInMinutes;
	
	public X3UtrFault() {
		lines = new ArrayList<>();
	}

	public String getFaultNumber() {
		return faultNumber;
	}

	public void setFaultNumber(String faultNumber) {
		this.faultNumber = faultNumber;
	}

	public String getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public X3UtrMachine getMachine() {
		return machine;
	}

	public void setMachine(X3UtrMachine machine) {
		this.machine = machine;
	}

	public ArrayList<X3UtrFaultLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<X3UtrFaultLine> lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		return "X3UtrFault [faultNumber=" + faultNumber + ", creatorCode=" + creatorCode + ", creatorName="
				+ creatorName + ", creationDate=" + creationDate + ", machineCode=" + machineCode + ", locationName="
				+ locationName + ", machine=" + machine + ", lines=" + lines + "]";
	}
	
	

	
	
	
}

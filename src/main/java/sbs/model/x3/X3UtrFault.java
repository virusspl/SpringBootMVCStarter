package sbs.model.x3;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class X3UtrFault {

	private String faultNumber;
	private String creatorCode;
	private String creatorName;
	private Timestamp creationDate;
	private String machineCode;
	private String locationName;
	private int faultType;
	public static final int NOSTOP_TYPE = 1;
	public static final int STOP_TYPE = 2;
	
	// filled from maps;
	private X3UtrMachine machine;
	private ArrayList<X3UtrFaultLine> lines;
	
	public X3UtrFault() {
		lines = new ArrayList<>();
	}
	
	/*
	 * GETTERS AND SETTERS
	 */

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

	/**
	 * get fault type
	 * @return 1 non stop, 2 stop
	 */
	public int getFaultType() {
		return faultType;
	}

	public void setFaultType(int faultType) {
		this.faultType = faultType;
	}

	@Override
	public String toString() {
		return "X3UtrFault [faultNumber=" + faultNumber + ", creatorCode=" + creatorCode + ", creatorName="
				+ creatorName + ", creationDate=" + creationDate + ", machineCode=" + machineCode + ", locationName="
				+ locationName + ", machine=" + machine + ", lines=" + lines + "]";
	}

	/*
	 * CALCULATIONS
	 */
	
	// methods
	// getInputDateTime;
	// getCloseDateTime;
	// getTotalWorkTimeInMinutes;
	
	public Timestamp getInputDateTime(){
		for(X3UtrFaultLine line: lines){
			if(line.getState() == 1){
				return line.getStartDateTime();
			}
		}
		return null;
	}
	
	public Timestamp getCloseDateTime(){
		Timestamp result = null;
		for(X3UtrFaultLine line: lines){
			if(line.getState() == 4){
				if(line.getEndDateTime() == null){
					continue;
				}
				if(result==null){
					result = line.getEndDateTime();
				}
				else if(result.before(line.getEndDateTime())){
					result = line.getEndDateTime();
				}
			}
		}
		if (result == null){
			result = new Timestamp(new Date().getTime());
		}
		return result;
	}
	
	public Timestamp getFirstReactionDateTime(){
		Timestamp result = null;
		for(X3UtrFaultLine line: lines){
			if(line.getState() == 2 || line.getState() == 3 || line.getState() == 4){
				if(result==null){
					result = line.getStartDateTime();
				}
				else if(line.getStartDateTime().before(result)){
					result = line.getStartDateTime();
				}
			}
		}
		if (result == null){
			result = new Timestamp(new Date().getTime());
		}
		return result;
	}
	
	public int getTotalWorkTimeInMinutes(){
		long millis = 0;
		for(X3UtrFaultLine line: lines){
			if(line.getState() == 2 || line.getState() == 3 || line.getState() == 4){
				if(line.getStartDateTime() != null){
					if(line.getEndDateTime() == null){
						millis += new Date().getTime() - line.getStartDateTime().getTime(); 
					}
					else{
						millis += line.getEndDateTime().getTime() - line.getStartDateTime().getTime();
					}
					 
				}
			}
		}
		return (int)millis/1000/60;
		
	}
	
	/**
	 * fault duration in minutes
	 * @return integer: 0 if missing data; minutes between start and close of fault 
	 */
	public int getFaultDurationInMinutes() {
		int timeDifferenceInMinutes = 0;
		if((getInputDateTime() != null) && getCloseDateTime() != null){
			long timeDifferenceInMillis = getCloseDateTime().getTime() - getInputDateTime().getTime();
			timeDifferenceInMinutes = (int)timeDifferenceInMillis/1000/60;
		}
		return timeDifferenceInMinutes;
	}

	public int getFirstReactionTimeInMinutes() {
		int timeDifferenceInMinutes = 0;
		if((getInputDateTime() != null) && getFirstReactionDateTime() != null){
			long timeDifferenceInMillis = getFirstReactionDateTime().getTime() - getInputDateTime().getTime();
			timeDifferenceInMinutes = (int)timeDifferenceInMillis/1000/60;
		}
		return timeDifferenceInMinutes;
	}
	
	
	
	

	
	
	
}

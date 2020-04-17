package sbs.model.x3;

import java.io.Serializable;

public class X3RouteLine implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String code;
	int operationNumber;
	String operationDescription;
	String machine;
	String center;
	String squad;
	double setTime;
	double waitTime;
	double personPreparing;
	double frazLavorazione;

	public X3RouteLine() {
	
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getOperationNumber() {
		return operationNumber;
	}

	public void setOperationNumber(int operationNumber) {
		this.operationNumber = operationNumber;
	}

	public String getOperationDescription() {
		return operationDescription;
	}

	public void setOperationDescription(String operationDescription) {
		this.operationDescription = operationDescription;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getSquad() {
		return squad;
	}

	public void setSquad(String squad) {
		this.squad = squad;
	}

	public double getSetTime() {
		return setTime;
	}

	public void setSetTime(double setTime) {
		this.setTime = setTime;
	}

	public double getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}

	public double getPersonPreparing() {
		return personPreparing;
	}

	public void setPersonPreparing(double personPreparing) {
		this.personPreparing = personPreparing;
	}

	public double getFrazLavorazione() {
		return frazLavorazione;
	}

	public void setFrazLavorazione(double frazLavorazione) {
		this.frazLavorazione = frazLavorazione;
	}

	@Override
	public String toString() {
		return "X3RouteLine [code=" + code + ", operationNumber=" + operationNumber + ", operationDescription="
				+ operationDescription + ", machine=" + machine + ", center=" + center + ", squad=" + squad
				+ ", setTime=" + setTime + ", waitTime=" + waitTime + ", personPreparing=" + personPreparing
				+ ", frazLavorazione=" + frazLavorazione + "]";
	}
	
	
	
	
}

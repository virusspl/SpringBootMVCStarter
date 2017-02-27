package sbs.model.hr;

import java.sql.Timestamp;

public class HrUserInfo {

	String id;
	String lastName;
	String firstName;
	String department;
	String rcpNumber;
	String position;
	Timestamp curentJobStart;
	Timestamp currentJobEnd;
	
	public HrUserInfo() {

	}

	public HrUserInfo(String id, String lastName, String firstName, String department, String rcpNumber,
			String position, Timestamp curentJobStart, Timestamp currentJobEnd) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.department = department;
		this.rcpNumber = rcpNumber;
		this.position = position;
		this.curentJobStart = curentJobStart;
		this.currentJobEnd = currentJobEnd;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the rcpNumber
	 */
	public String getRcpNumber() {
		return rcpNumber;
	}

	/**
	 * @param rcpNumber the rcpNumber to set
	 */
	public void setRcpNumber(String rcpNumber) {
		this.rcpNumber = rcpNumber;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the curentJobStart
	 */
	public Timestamp getCurentJobStart() {
		return curentJobStart;
	}

	/**
	 * @param curentJobStart the curentJobStart to set
	 */
	public void setCurentJobStart(Timestamp curentJobStart) {
		this.curentJobStart = curentJobStart;
	}

	/**
	 * @return the currentJobEnd
	 */
	public Timestamp getCurrentJobEnd() {
		return currentJobEnd;
	}

	/**
	 * @param currentJobEnd the currentJobEnd to set
	 */
	public void setCurrentJobEnd(Timestamp currentJobEnd) {
		this.currentJobEnd = currentJobEnd;
	}
	
	
	
}

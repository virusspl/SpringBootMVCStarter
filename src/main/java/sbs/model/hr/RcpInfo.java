package sbs.model.hr;

public class RcpInfo {
	
	String id;
	String lastName;
	String firstName;
	String department;
	String rcpNumber;
	
	public RcpInfo() {

	}

	public RcpInfo(String id, String lastName, String firstName, String department, String rcpNumber) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.department = department;
		this.rcpNumber = rcpNumber;
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
	
	
	
}

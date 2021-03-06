package sbs.model.x3;

import java.sql.Timestamp;

public class X3Supplier {

	String code;
	String name;
	Timestamp firstOrderDate; 
	String country;
	boolean euMember;
	
	public X3Supplier() {
	
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getFirstOrderDate() {
		return firstOrderDate;
	}

	public void setFirstOrderDate(Timestamp firstOrderDate) {
		this.firstOrderDate = firstOrderDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isEuMember() {
		return euMember;
	}

	public void setEuMember(boolean euMember) {
		this.euMember = euMember;
	}
	
	

}

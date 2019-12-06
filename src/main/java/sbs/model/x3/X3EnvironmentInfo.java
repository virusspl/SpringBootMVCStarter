package sbs.model.x3;

import java.sql.Timestamp;

public class X3EnvironmentInfo {

	private String code;
	private String description;
	private Timestamp accDate;
	private double quantity;
	private String movementName;
	private String gr1;
	private String gr2;
	private String unit;
	
	public X3EnvironmentInfo() {
	
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getAccDate() {
		return accDate;
	}

	public void setAccDate(Timestamp accDate) {
		this.accDate = accDate;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getMovementName() {
		return movementName;
	}

	public void setMovementName(String movementName) {
		this.movementName = movementName;
	}

	public String getGr1() {
		return gr1;
	}

	public void setGr1(String gr1) {
		this.gr1 = gr1;
	}

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "X3EnvironmentInfo [code=" + code + ", description=" + description + ", accDate=" + accDate
				+ ", quantity=" + quantity + ", movementName=" + movementName + ", gr1=" + gr1 + ", gr2=" + gr2
				+ ", unit=" + unit + "]";
	}

	
	
}

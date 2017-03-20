package sbs.model.wpslook;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class WpslookRow {
	private String address;
	private BigDecimal chronoNumber;
	private String product;
	private BigDecimal quantity;
	private String unit;
	private Timestamp LastInputDate;
	private Timestamp LastOutputDate;
	
	public WpslookRow() {
		
	}

	public WpslookRow(String address, BigDecimal chronoNumber, String product, BigDecimal quantity, String unit, Timestamp lastInputDate,
			Timestamp lastOutputDate) {
		super();
		this.address = address;
		this.chronoNumber = chronoNumber;
		this.product = product;
		this.quantity = quantity;
		this.unit = unit;
		LastInputDate = lastInputDate;
		LastOutputDate = lastOutputDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Timestamp getLastInputDate() {
		return LastInputDate;
	}

	public void setLastInputDate(Timestamp lastInputDate) {
		LastInputDate = lastInputDate;
	}

	public Timestamp getLastOutputDate() {
		return LastOutputDate;
	}

	public void setLastOutputDate(Timestamp lastOutputDate) {
		LastOutputDate = lastOutputDate;
	}

	public BigDecimal getChronoNumber() {
		return chronoNumber;
	}

	public void setChronoNumber(BigDecimal chronoNumber) {
		this.chronoNumber = chronoNumber;
	}

	@Override
	public String toString() {
		return "WpslookRow [address=" + address + ", chronoNumber=" + chronoNumber + ", product=" + product
				+ ", quantity=" + quantity + ", unit=" + unit + ", LastInputDate=" + LastInputDate + ", LastOutputDate="
				+ LastOutputDate + "]";
	}

	
	
	
	

}

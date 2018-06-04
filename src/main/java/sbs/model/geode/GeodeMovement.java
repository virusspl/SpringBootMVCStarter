package sbs.model.geode;

import java.sql.Timestamp;

public class GeodeMovement {
	private String number;
	private String item;
	private int quantity1;
	private int quantity2;
	private Timestamp creationDateTime;
	private String movementCode;
	private String store;
	private String slot;
	private String creationUserCode;
	private String creationUserName;

	
	public GeodeMovement() {
		
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public String getItem() {
		return item;
	}


	public void setItem(String item) {
		this.item = item;
	}


	public int getQuantity1() {
		return quantity1;
	}


	public void setQuantity1(int quantity1) {
		this.quantity1 = quantity1;
	}


	public int getQuantity2() {
		return quantity2;
	}


	public void setQuantity2(int quantity2) {
		this.quantity2 = quantity2;
	}


	public Timestamp getCreationDateTime() {
		return creationDateTime;
	}


	public void setCreationDateTime(Timestamp creationDateTime) {
		this.creationDateTime = creationDateTime;
	}


	public String getMovementCode() {
		return movementCode;
	}


	public void setMovementCode(String movementCode) {
		this.movementCode = movementCode;
	}


	public String getStore() {
		return store;
	}


	public void setStore(String store) {
		this.store = store;
	}


	public String getSlot() {
		return slot;
	}


	public void setSlot(String slot) {
		this.slot = slot;
	}


	public String getCreationUserCode() {
		return creationUserCode;
	}


	public void setCreationUserCode(String creationUserCode) {
		this.creationUserCode = creationUserCode;
	}


	public String getCreationUserName() {
		return creationUserName;
	}


	public void setCreationUserName(String creationUserName) {
		this.creationUserName = creationUserName;
	}


	@Override
	public String toString() {
		return "GeodeMovement [number=" + number + ", item=" + item + ", quantity1=" + quantity1 + ", quantity2="
				+ quantity2 + ", creationDateTime=" + creationDateTime + ", movementCode=" + movementCode + ", store="
				+ store + ", slot=" + slot + ", creationUserCode=" + creationUserCode + ", creationUserName="
				+ creationUserName + "]";
	}

	

}

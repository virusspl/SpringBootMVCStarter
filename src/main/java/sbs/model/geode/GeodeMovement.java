package sbs.model.geode;

import java.sql.Timestamp;

public class GeodeMovement {
	private String number;
	private String item;
	private int quantity;
	private Timestamp creationDateTime;
	private String movementCode;
	private String store;
	private String slot;
	private String creationUserCode;
	private String creationUserName;
	
	public static final int GEODE_MOVEMENT_LOAD = 1;
	public static final int GEODE_MOVEMENT_UNLOAD = 2;
	public static final int GEODE_MOVEMENT_UNKNOWN = 3;
	

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


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
		return "GeodeMovement [number=" + number + ", item=" + item + ", quantity1=" + quantity + ", creationDateTime=" + creationDateTime + ", movementCode=" + movementCode + ", store="
				+ store + ", slot=" + slot + ", creationUserCode=" + creationUserCode + ", creationUserName="
				+ creationUserName + "]";
	}

	

}

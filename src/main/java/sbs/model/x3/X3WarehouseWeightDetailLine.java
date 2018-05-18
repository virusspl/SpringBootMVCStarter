package sbs.model.x3;

import java.sql.Timestamp;

public class X3WarehouseWeightDetailLine {

	private Timestamp dateTime;
	private String cardNumber;
	private String userName;
	private String parcelNumber;
	
	
	public X3WarehouseWeightDetailLine() {

	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParcelNumber() {
		return parcelNumber;
	}

	public void setParcelNumber(String parcelNumber) {
		this.parcelNumber = parcelNumber;
	}

	@Override
	public String toString() {
		return "X3WarehouseWeightDetailLine [dateTime=" + dateTime + ", cardNumber=" + cardNumber + ", userName="
				+ userName + ", parcelNumber=" + parcelNumber + "]";
	}

	
}

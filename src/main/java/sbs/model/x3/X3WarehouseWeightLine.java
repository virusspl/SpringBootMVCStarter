package sbs.model.x3;

import java.sql.Timestamp;

public class X3WarehouseWeightLine {

	private Timestamp date;
	private String cardNumber;
	private String userName;
	private int numberOfLabels;
	
	public X3WarehouseWeightLine() {

	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
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

	public int getNumberOfLabels() {
		return numberOfLabels;
	}

	public void setNumberOfLabels(int numberOfLabels) {
		this.numberOfLabels = numberOfLabels;
	}

	@Override
	public String toString() {
		return "X3WarehouseWeightLine [date=" + date + ", cardNumber=" + cardNumber + ", userName=" + userName
				+ ", numberOfLabels=" + numberOfLabels + "]";
	}
	
	


	
}

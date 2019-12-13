package sbs.controller.auth;

public class RcpCardForm {
	
	private String cardNumber;

	public RcpCardForm() {
	
	}
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	@Override
	public String toString() {
		return "RcpCardForm [cardNumber=" + cardNumber + "]";
	}

	
	
	
}

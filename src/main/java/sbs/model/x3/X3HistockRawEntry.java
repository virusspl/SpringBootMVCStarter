package sbs.model.x3;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class X3HistockRawEntry {

	private String order;
	private int line;
	private String code;
	private String gr1;
	private String gr2; 
	private String category;
	private int quantityOrdered;
	private int quantityReceived;
	private Timestamp orderDate;
	private Timestamp expectedDeliveryDate;
	private Timestamp lastReceptionDate;
	private int receptionsCounter;
	
	public X3HistockRawEntry() {
	
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public int getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(int quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public Timestamp getLastReceptionDate() {
		return lastReceptionDate;
	}

	public void setLastReceptionDate(Timestamp lastReceptionDate) {
		this.lastReceptionDate = lastReceptionDate;
	}

	public int getReceptionsCounter() {
		return receptionsCounter;
	}

	public void setReceptionsCounter(int receptionsCounter) {
		this.receptionsCounter = receptionsCounter;
	}

	public Timestamp getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Timestamp expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public int getDaysToDelivery() {
		long diff = lastReceptionDate.getTime() - orderDate.getTime();
		return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	public int getDeliveryDeviation() {
		long diff = lastReceptionDate.getTime() - expectedDeliveryDate.getTime();
		return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public String toString() {
		return "X3HistockRawEntry [order=" + order + ", line=" + line + ", code=" + code + ", gr1=" + gr1 + ", gr2="
				+ gr2 + ", category=" + category + ", quantityOrdered=" + quantityOrdered + ", quantityReceived="
				+ quantityReceived + ", orderDate=" + orderDate + ", lastReceptionDate=" + lastReceptionDate
				+ ", receptionsCounter=" + receptionsCounter + ", getDaysToDelivery()=" + getDaysToDelivery() + "]";
	}



	
	
	
	
	
}

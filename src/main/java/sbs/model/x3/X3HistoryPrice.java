package sbs.model.x3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "history_price_list")
public class X3HistoryPrice {

	@Id
	@Column(name = "hpl_code", length = 30)
	private String code;
	@Column(name = "hpl_price", nullable = false)
	private double price;
	
	
	public X3HistoryPrice() {
		
	}

	public X3HistoryPrice(String code, int year, double price) {
		super();
		this.code = code;
		this.price = price;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "X3HistoryPrice [code=" + code + ", price=" + price + "]";
	}


	
	
}

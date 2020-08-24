package sbs.controller.prodcomp;

import java.sql.Timestamp;

public class NoBomCodeInfo {

	private String code;
	private String description;
	private String category;
	private String gr1;
	private String gr2;
	private Timestamp creationDate;
	private Timestamp lastIssueDate;
	private Timestamp lastReceptionDate;
	private int stockX3;
	private int stockGeode;	
	private int saleCurrentYear;
	private int salePreviousYear;
	private int sale2YearsBack;
	
	
	
	public NoBomCodeInfo() {
		
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



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
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



	public Timestamp getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}



	public Timestamp getLastIssueDate() {
		return lastIssueDate;
	}



	public void setLastIssueDate(Timestamp lastIssueDate) {
		this.lastIssueDate = lastIssueDate;
	}



	public Timestamp getLastReceptionDate() {
		return lastReceptionDate;
	}



	public void setLastReceptionDate(Timestamp lastReceptionDate) {
		this.lastReceptionDate = lastReceptionDate;
	}



	public int getStockX3() {
		return stockX3;
	}



	public void setStockX3(int stockX3) {
		this.stockX3 = stockX3;
	}



	public int getStockGeode() {
		return stockGeode;
	}



	public void setStockGeode(int stockGeode) {
		this.stockGeode = stockGeode;
	}



	public int getSaleCurrentYear() {
		return saleCurrentYear;
	}



	public void setSaleCurrentYear(int saleCurrentYear) {
		this.saleCurrentYear = saleCurrentYear;
	}



	public int getSalePreviousYear() {
		return salePreviousYear;
	}



	public void setSalePreviousYear(int salePreviousYear) {
		this.salePreviousYear = salePreviousYear;
	}



	public int getSale2YearsBack() {
		return sale2YearsBack;
	}



	public void setSale2YearsBack(int sale2YearsBack) {
		this.sale2YearsBack = sale2YearsBack;
	}


	
}

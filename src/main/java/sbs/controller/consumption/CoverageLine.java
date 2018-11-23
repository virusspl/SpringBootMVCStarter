package sbs.controller.consumption;

import java.sql.Timestamp;

public class CoverageLine {

	String productCode;
	String description;
	String gr2,reorderPolicy;
	int time;
	int stock;
	int required;
	int ordered;
	int available;
	int theoretical;
	int inRoute;
	int useInYear;
	double averageMonthUse;
	double q1, q2, q3, q4;
	int monthsCount;
	double cover1, cover2;
	String buyerCode, buyerName;
	String supplierCode, supplierName;
	Timestamp firstSupplyDate;
	
	public CoverageLine() {

	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getRequired() {
		return required;
	}

	public void setRequired(int required) {
		this.required = required;
	}

	public int getOrdered() {
		return ordered;
	}

	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getTheoretical() {
		return theoretical;
	}

	public void setTheoretical(int theoretical) {
		this.theoretical = theoretical;
	}

	public int getInRoute() {
		return inRoute;
	}

	public void setInRoute(int inRoute) {
		this.inRoute = inRoute;
	}

	public int getUseInYear() {
		return useInYear;
	}

	public void setUseInYear(int useInYear) {
		this.useInYear = useInYear;
	}

	public double getAverageMonthUse() {
		return averageMonthUse;
	}

	public void setAverageMonthUse(double averageMonthUse) {
		this.averageMonthUse = averageMonthUse;
	}

	public double getQ1() {
		return q1;
	}

	public void setQ1(double q1) {
		this.q1 = q1;
	}

	public double getQ2() {
		return q2;
	}

	public void setQ2(double q2) {
		this.q2 = q2;
	}

	public double getQ3() {
		return q3;
	}

	public void setQ3(double q3) {
		this.q3 = q3;
	}

	public double getQ4() {
		return q4;
	}

	public void setQ4(double q4) {
		this.q4 = q4;
	}

	public int getMonthsCount() {
		return monthsCount;
	}

	public void setMonthsCount(int monthsCount) {
		this.monthsCount = monthsCount;
	}

	public double getCover1() {
		return cover1;
	}

	public void setCover1(double cover1) {
		this.cover1 = cover1;
	}

	public double getCover2() {
		return cover2;
	}

	public void setCover2(double cover2) {
		this.cover2 = cover2;
	}

	public String getBuyerCode() {
		return buyerCode;
	}

	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Timestamp getFirstSupplyDate() {
		return firstSupplyDate;
	}

	public void setFirstSupplyDate(Timestamp firstSupplyDate) {
		this.firstSupplyDate = firstSupplyDate;
	}

	public String getReorderPolicy() {
		return reorderPolicy;
	}

	public void setReorderPolicy(String reorderPolicy) {
		this.reorderPolicy = reorderPolicy;
	}
	
}

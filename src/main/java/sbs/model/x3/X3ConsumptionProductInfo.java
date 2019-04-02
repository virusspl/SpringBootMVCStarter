package sbs.model.x3;

import java.sql.Timestamp;

public class X3ConsumptionProductInfo {
	private String productCode;
	private String productDescriptionPl;
	private int stock;
	private int inOrder;
	private double averageCost;
	private String buyerCode;
	private String buyGroupCode;
	private Timestamp lastIssueDate;
	private Timestamp lastReceptionDate;
	private int	reorderPoint;
	private int safetyStock;
	private int maxStsock;
	private int ewz;
	private int technicalLot;
	private int leadTime;
	
	
	public X3ConsumptionProductInfo() {
	
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescriptionPl() {
		return productDescriptionPl;
	}

	public void setProductDescriptionPl(String productDescriptionPl) {
		this.productDescriptionPl = productDescriptionPl;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getInOrder() {
		return inOrder;
	}

	public void setInOrder(int inOrder) {
		this.inOrder = inOrder;
	}

	public double getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(double averageCost) {
		this.averageCost = averageCost;
	}

	public String getBuyerCode() {
		return buyerCode;
	}

	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}
	

	public String getBuyGroupCode() {
		return buyGroupCode;
	}

	public void setBuyGroupCode(String buyGroupCode) {
		this.buyGroupCode = buyGroupCode;
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

	public int getReorderPoint() {
		return reorderPoint;
	}

	public void setReorderPoint(int reorderPoint) {
		this.reorderPoint = reorderPoint;
	}

	public int getSafetyStock() {
		return safetyStock;
	}

	public void setSafetyStock(int safetyStock) {
		this.safetyStock = safetyStock;
	}

	public int getMaxStsock() {
		return maxStsock;
	}

	public void setMaxStsock(int maxStsock) {
		this.maxStsock = maxStsock;
	}

	public int getEwz() {
		return ewz;
	}

	public void setEwz(int ewz) {
		this.ewz = ewz;
	}

	public int getTechnicalLot() {
		return technicalLot;
	}

	public void setTechnicalLot(int technicalLot) {
		this.technicalLot = technicalLot;
	}

	public int getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}

	@Override
	public String toString() {
		return "X3ConsumptionProductInfo [productCode=" + productCode + ", productDescriptionPl=" + productDescriptionPl
				+ ", stock=" + stock + ", inOrder=" + inOrder + ", averageCost=" + averageCost + ", buyerCode="
				+ buyerCode + ", buyGroupCode=" + buyGroupCode + ", lastIssueDate=" + lastIssueDate
				+ ", lastReceptionDate=" + lastReceptionDate + ", reorderPoint=" + reorderPoint + ", safetyStock="
				+ safetyStock + ", maxStsock=" + maxStsock + ", ewz=" + ewz + ", technicalLot=" + technicalLot
				+ ", leadTime=" + leadTime + "]";
	}

	
}

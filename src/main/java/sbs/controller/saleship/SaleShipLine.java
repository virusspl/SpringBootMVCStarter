package sbs.controller.saleship;

import java.sql.Timestamp;

public class SaleShipLine {

	private String salesOrder;
	private String salesOrderLine;
	private String productionOrderAndLine;
	private String clientCode;
	private String clientName;
	private Timestamp demandedDate;
	private Timestamp originalDate;
	private Timestamp creationDate;
	private Timestamp updateDate;
	private String productCode;
	private String productDescription;
	private String productGr1;
	private String productGr2;
	private String productCategory;
	private String machineCode;
	private String departmentCode;
	private int stockProduction;
	private int stockShipments;
	private int stockQ;
	private int quantityRemainingToShip;
	private int quantityShipped;
	private int quantityToGive;
	private String country;
	private String demandStateCode;
	private String creationUserCode;
	private String creationUserName;
	private double saleMargin;
	private double unitPrice;
	private String orderSource;
	
	
	public SaleShipLine() {
	
	}
	
	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
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

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getSalesOrderLine() {
		return salesOrderLine;
	}

	public void setSalesOrderLine(String salesOrderLine) {
		this.salesOrderLine = salesOrderLine;
	}


	public String getProductionOrderAndLine() {
		return productionOrderAndLine;
	}

	public void setProductionOrderAndLine(String productionOrderAndLine) {
		this.productionOrderAndLine = productionOrderAndLine;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Timestamp getDemandedDate() {
		return demandedDate;
	}

	public void setDemandedDate(Timestamp demandedDate) {
		this.demandedDate = demandedDate;
	}

	
	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public int getStockProduction() {
		return stockProduction;
	}

	public void setStockProduction(int stockProduction) {
		this.stockProduction = stockProduction;
	}

	public int getStockShipments() {
		return stockShipments;
	}

	public void setStockShipments(int stockShipments) {
		this.stockShipments = stockShipments;
	}

	public int getQuantityRemainingToShip() {
		return quantityRemainingToShip;
	}

	public void setQuantityRemainingToShip(int quantityRemainingToShip) {
		this.quantityRemainingToShip = quantityRemainingToShip;
	}

	public int getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(int quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	public int getQuantityToGive() {
		return quantityToGive;
	}

	public void setQuantityToGive(int quantityToGive) {
		this.quantityToGive = quantityToGive;
	}

	public String getProductGr1() {
		return productGr1;
	}

	public void setProductGr1(String productGr1) {
		this.productGr1 = productGr1;
	}

	public String getProductGr2() {
		return productGr2;
	}

	public void setProductGr2(String productGr2) {
		this.productGr2 = productGr2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getStockQ() {
		return stockQ;
	}

	public void setStockQ(int stockQ) {
		this.stockQ = stockQ;
	}

	public Timestamp getOriginalDate() {
		return originalDate;
	}

	public void setOriginalDate(Timestamp originalDate) {
		this.originalDate = originalDate;
	}

	public String getDemandStateCode() {
		return demandStateCode;
	}

	public void setDemandStateCode(String demandStateCode) {
		this.demandStateCode = demandStateCode;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public double getSaleMargin() {
		return saleMargin;
	}

	public void setSaleMargin(double saleMargin) {
		this.saleMargin = saleMargin;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
	
}

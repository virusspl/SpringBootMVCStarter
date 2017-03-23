package sbs.model.x3;

public class X3ProductionOrderDetails {
	
	private String productionOrderNumber;
	private String productCode;
	private String productDescription;
	private String salesOrderNumber;
	private String clientCode;
	private String clientName;
	private int producedQuantity;
	
	public X3ProductionOrderDetails() {
	
	}

	public String getProductionOrderNumber() {
		return productionOrderNumber;
	}

	public void setProductionOrderNumber(String productionOrderNumber) {
		this.productionOrderNumber = productionOrderNumber;
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

	public String getSalesOrderNumber() {
		return salesOrderNumber;
	}

	public void setSalesOrderNumber(String salesOrderNumber) {
		this.salesOrderNumber = salesOrderNumber;
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

	@Override
	public String toString() {
		return "X3ProductionOrderDetails [productionOrderNumber=" + productionOrderNumber + ", productCode="
				+ productCode + ", productDescription=" + productDescription + ", salesOrderNumber=" + salesOrderNumber
				+ ", clientCode=" + clientCode + ", clientName=" + clientName + "]";
	}

	public int getProducedQuantity() {
		return producedQuantity;
	}

	public void setProducedQuantity(int producedQuantity) {
		this.producedQuantity = producedQuantity;
	}


	
	

	/*
	SELECT ATW_MFGITM.MFGNUM_0, ATW_MFGITM.ITMREF_0, ATW_MFGITM.PJT_0, ATW_MFGITM.BPCNUM_0, ATW_ITMMASTER.ITMDES1_0, ATW_ITMMASTER.ITMDES2_0, ATW_SORDER.SOHNUM_0, ATW_BPARTNER.BPRNAM_0, ATW_BPARTNER.BPRNAM_1
	FROM ((ATW_MFGITM INNER JOIN ATW_ITMMASTER ON ATW_MFGITM.ITMREF_0 = ATW_ITMMASTER.ITMREF_0) INNER JOIN ATW_SORDER ON ATW_MFGITM.PJT_0 = ATW_SORDER.SOHNUM_0) INNER JOIN ATW_BPARTNER ON ATW_MFGITM.BPCNUM_0 = ATW_BPARTNER.BPRNUM_0
	WHERE (((ATW_MFGITM.MFGNUM_0)="X31121400099455"));
	*/

	
}

package sbs.model.x3;

import java.io.Serializable;

public class X3BomPart implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * quantity of sub code in current code 
	 * (used only in chain BOM by part, need recalculate finished array)
	 */
	private Double quantityOfSubcode;
	private Double quantityOfSelf;
	private Double quantityDemand;
	private Double currentStock;
	private String parentCode;
	private String partCode;

	
	public X3BomPart() {
		this.quantityDemand = 0.0;
		this.currentStock = 0.0;
	}
	
	/**
	 * cloning constructor
	 * @param part
	 */
	public X3BomPart(X3BomPart part) {
		super();
		this.parentCode = part.getParentCode();
		this.partCode = part.getPartCode();
		this.quantityOfSubcode = part.getQuantityOfSubcode();
		this.quantityOfSelf = part.getQuantityOfSelf();
		this.quantityDemand = part.getQuantityDemand();
		this.currentStock = part.getCurrentStock();
	}


	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public Double getQuantityOfSubcode() {
		return quantityOfSubcode;
	}

	public void setQuantityOfSubcode(Double quantityOfSubcode) {
		this.quantityOfSubcode = quantityOfSubcode;
	}

	public Double getQuantityOfSelf() {
		return quantityOfSelf;
	}

	public void setQuantityOfSelf(Double quantityOfSelf) {
		this.quantityOfSelf = quantityOfSelf;
	}
	
	/**
	 * current stock to fill in separate method (initial 0)
	 * @return
	 */
	public Double getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Double currentStock) {
		this.currentStock = currentStock;
	}

	/**
	 * fill after calculation (initial 0)
	 * @return
	 */
	public Double getQuantityDemand() {
		return quantityDemand;
	}

	public void setQuantityDemand(Double quantityDemand) {
		this.quantityDemand = quantityDemand;
	}

	@Override
	public String toString() {
		return "X3BomPart [quantityOfSubcode=" + quantityOfSubcode + ", quantityOfSelf=" + quantityOfSelf
				+ ", quantityDemand=" + quantityDemand + ", parentCode=" + parentCode + ", partCode=" + partCode + "]";
	}



}

package sbs.controller.prodorigin;

import java.util.List;

import sbs.model.x3.X3Product;

public class ComponentInfo {
	private X3Product product;
	private List<SupplyInfo> supplyInfo;
	private boolean europeOnly;
	private double quantityInBom;
	private String unit;
	
	
	public ComponentInfo() {

	}

	public X3Product getProduct() {
		return product;
	}

	public void setProduct(X3Product product) {
		this.product = product;
	}

	public List<SupplyInfo> getSupplyInfo() {
		return supplyInfo;
	}

	public void setSupplyInfo(List<SupplyInfo> supplyInfo) {
		this.supplyInfo = supplyInfo;
	}
	

	public boolean isEuropeOnly() {
		return europeOnly;
	}

	public void setEuropeOnly(boolean europeOnly) {
		this.europeOnly = europeOnly;
	}
	
	public double getQuantityInBom() {
		return quantityInBom;
	}

	public void setQuantityInBom(double quantityInBom) {
		this.quantityInBom = quantityInBom;
	}
	

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "ComponentInfo [product=" + product.getCode() + ", supplyInfo=" + supplyInfo + "]";
	}
	
	
	
	
}

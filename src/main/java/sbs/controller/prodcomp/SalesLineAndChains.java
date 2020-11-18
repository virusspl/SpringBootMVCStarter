package sbs.controller.prodcomp;

import java.util.ArrayList;
import java.util.List;

import sbs.model.x3.X3BomPart;
import sbs.model.x3.X3SalesOrderLine;

public class SalesLineAndChains {

	private X3SalesOrderLine line;
	private List<List<X3BomPart>> chains;
	private double toSendValue;
	private int baseComponentQuantity;
	
	public SalesLineAndChains() {
		chains = new ArrayList<>();
	}

	public SalesLineAndChains(X3SalesOrderLine line) {
		super();
		chains = new ArrayList<>();
		this.line = line;
		calculateToSendValue();
	}
	
	
	public int getBaseComponentQuantity() {
		return baseComponentQuantity;
	}

	public void setBaseComponentQuantity(int baseComponentQuantity) {
		this.baseComponentQuantity = baseComponentQuantity;
	}

	public X3SalesOrderLine getLine() {
		return line;
	}

	public void setLine(X3SalesOrderLine line) {
		this.line = line;
		calculateToSendValue();
	}

	public List<List<X3BomPart>> getChains() {
		return chains;
	}

	public void setChains(List<List<X3BomPart>> chains) {
		this.chains = chains;
	}
	
	
	public double getToSendValue() {
		return toSendValue;
	}

	public void setToSendValue(double toSendValue) {
		this.toSendValue = toSendValue;
	}

	/**
	 * clone chain and multiply each element selfQuantity by leftToSendQuantity from order line 
	 * @param chain
	 */
	public void addClonedAndCalculatedChain(List<X3BomPart> chain) {
		List<X3BomPart> cloneList = new ArrayList<>();
		X3BomPart clonePart;
		for(X3BomPart part: chain) {
			clonePart = new X3BomPart(part);
			clonePart.setQuantityDemand(clonePart.getQuantityOfSelf()*line.getQuantityLeftToSend());
			cloneList.add(clonePart);
		}
		this.chains.add(cloneList);
	}
	
	private void calculateToSendValue() {
		this.toSendValue = this.line.getExchangeRate() * this.line.getUnitPrice() * this.line.getQuantityLeftToSend();
	}

	public int getTargetProductDemand(String component) {
		int demand = 0;
		for(List<X3BomPart> chain: chains) {
			for(X3BomPart part: chain) {
				if(part.getParentCode().equalsIgnoreCase(component)) {
					demand += part.getQuantityDemand();
				}
			}
		}
		return demand;
	}
	
}

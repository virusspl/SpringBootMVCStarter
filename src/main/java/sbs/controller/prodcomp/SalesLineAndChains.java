package sbs.controller.prodcomp;

import java.util.ArrayList;
import java.util.List;

import sbs.model.x3.X3BomPart;
import sbs.model.x3.X3SalesOrderLine;

public class SalesLineAndChains {

	private X3SalesOrderLine line;
	private List<List<X3BomPart>> chains;
	
	public SalesLineAndChains() {
		chains = new ArrayList<>();
	}

	public SalesLineAndChains(X3SalesOrderLine line) {
		super();
		chains = new ArrayList<>();
		this.line = line;
	}

	public X3SalesOrderLine getLine() {
		return line;
	}

	public void setLine(X3SalesOrderLine line) {
		this.line = line;
	}

	public List<List<X3BomPart>> getChains() {
		return chains;
	}

	public void setChains(List<List<X3BomPart>> chains) {
		this.chains = chains;
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
	
}

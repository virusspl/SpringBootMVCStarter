package sbs.controller.stocksum;

public class StockLine {

	private String code;
	private double stockX3;
	private double stockGeodeProd;
	private double stockGeodeRcp;
	private double demand;
	private String category;
	
	public StockLine() {
	
	}
	

	public StockLine(String code, String category, double stockX3, double stockGeodeProd, double stockGeodeRcp, double demand) {
		super();
		this.code = code;
		this.category = category;
		this.stockX3 = stockX3;
		this.stockGeodeProd = stockGeodeProd;
		this.stockGeodeRcp = stockGeodeRcp;
		this.demand = demand;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getStockX3() {
		return stockX3;
	}

	public void setStockX3(double stockX3) {
		this.stockX3 = stockX3;
	}

	public double getStockGeodeProd() {
		return stockGeodeProd;
	}

	public void setStockGeodeProd(double stockGeodeProd) {
		this.stockGeodeProd = stockGeodeProd;
	}

	public double getStockGeodeRcp() {
		return stockGeodeRcp;
	}

	public void setStockGeodeRcp(double stockGeodeRcp) {
		this.stockGeodeRcp = stockGeodeRcp;
	}

	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	@Override
	public String toString() {
		return "StockLine [code=" + code + ", stockX3=" + stockX3 + ", stockGeodeProd=" + stockGeodeProd
				+ ", stockGeodeRcp=" + stockGeodeRcp + ", demand=" + demand + "]";
	}
	
	
}

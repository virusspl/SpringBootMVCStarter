package sbs.controller.magpart;

public class MagpartInfo {

	private String productCode;
	private String category;
	private String gr2;
	
	private int totalStock;
	private int totalCount;
	
	private int stockA;
	private int countA;
	
	private int stockB;
	private int countB;
	
	private int stockOther;
	private int countOther;
	
	public MagpartInfo() {
		
	}
	
	public MagpartInfo(String productCode) {
		super();
		this.productCode = productCode;
		this.category = "N/D";
		this.gr2 = "N/D";
		this.totalStock = 0;
		this.totalCount = 0;
		this.stockA = 0;
		this.stockB = 0;
		this.countA = 0;
		this.countB = 0;
		this.stockOther = 0;
		this.countOther = 0;
	}
	
	public MagpartInfo(String productCode, int totalStock, int totalCount) {
		super();
		this.productCode = productCode;
		this.category = "N/D";
		this.gr2 = "N/D";
		this.totalStock = totalStock;
		this.totalCount = totalCount;
		this.stockA = 0;
		this.stockB = 0;
		this.countA = 0;
		this.countB = 0;
		this.stockOther = 0;
		this.countOther = 0;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}
	

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getStockA() {
		return stockA;
	}

	public void setStockA(int stockA) {
		this.stockA = stockA;
	}

	public int getCountA() {
		return countA;
	}

	public void setCountA(int countA) {
		this.countA = countA;
	}

	public int getStockB() {
		return stockB;
	}

	public void setStockB(int stockB) {
		this.stockB = stockB;
	}

	public int getCountB() {
		return countB;
	}

	public void setCountB(int countB) {
		this.countB = countB;
	}

	public int getStockOther() {
		return stockOther;
	}

	public void setStockOther(int stockOther) {
		this.stockOther = stockOther;
	}

	public int getCountOther() {
		return countOther;
	}

	public void setCountOther(int countOther) {
		this.countOther = countOther;
	}
	

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}

	@Override
	public String toString() {
		return "MagpartInfo [productCode=" + productCode + ", category=" + category + ", gr2=" + gr2 + ", totalStock="
				+ totalStock + ", totalCount=" + totalCount + ", stockA=" + stockA + ", countA=" + countA + ", stockB="
				+ stockB + ", countB=" + countB + ", stockOther=" + stockOther + ", countOther=" + countOther + "]";
	}


	
}

package sbs.model.x3;

public class X3CoverageData {

	String code;
	int time;
	int stock;
	int inOrder;
	int inRoute;
	String buyerCode;
	String buyerName;
	String gr2;
	String reorderPolicy;
	
	public X3CoverageData() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public int getInOrder() {
		return inOrder;
	}

	public void setInOrder(int inOrder) {
		this.inOrder = inOrder;
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

	public String getGr2() {
		return gr2;
	}

	public void setGr2(String gr2) {
		this.gr2 = gr2;
	}
	
	public int getInRoute() {
		return inRoute;
	}

	public void setInRoute(int inRoute) {
		this.inRoute = inRoute;
	}
	
	public String getReorderPolicy() {
		return reorderPolicy;
	}

	public void setReorderPolicy(String reorderPolicy) {
		this.reorderPolicy = reorderPolicy;
	}

	@Override
	public String toString() {
		return "X3CoverageData [code=" + code + ", time=" + time + ", stock=" + stock + ", inOrder=" + inOrder
				+ ", buyerCode=" + buyerCode + ", buyerName=" + buyerName + "]";
	}
	
	
	
	
}

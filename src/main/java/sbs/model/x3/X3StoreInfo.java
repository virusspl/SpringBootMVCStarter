package sbs.model.x3;

public class X3StoreInfo {

	private String productCode;
	private int generalStore;
	private int mag;
	private int qgx;
	private int wgx;
	private int geode;
	
	public X3StoreInfo() {
	
	}
	
	public X3StoreInfo(String productCode) {
		super();
		this.productCode = productCode;
	}
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getMag() {
		return mag;
	}

	public void setMag(int mag) {
		this.mag = mag;
	}

	public int getQgx() {
		return qgx;
	}

	public void setQgx(int qgx) {
		this.qgx = qgx;
	}

	public int getWgx() {
		return wgx;
	}

	public void setWgx(int wgx) {
		this.wgx = wgx;
	}

	public int getGeode() {
		return geode;
	}

	public void setGeode(int geode) {
		this.geode = geode;
	}

	public int getGeneralStore() {
		return generalStore;
	}

	public void setGeneralStore(int generalStore) {
		this.generalStore = generalStore;
	}
	
	public void addGeneralStore(int generalStore){
		this.generalStore+= generalStore;
	}
	
	public void addMag(int qty){
		this.mag += qty;
		this.generalStore += qty;
	}
	public void addQgx(int qty){
		this.qgx += qty;
		this.generalStore += qty;
	}
	public void addWgx(int qty){
		this.wgx += qty;
		this.generalStore += qty;
	}
	public void addGeode(int qty){
		this.geode += qty;
		this.generalStore += qty;
	}

	@Override
	public String toString() {
		return "X3StoreInfo [productCode=" + productCode + ", generalStore=" + generalStore + ", mag=" + mag + ", qgx="
				+ qgx + ", wgx=" + wgx + ", geode=" + geode + "]";
	}
	
	
	
}

package sbs.model.x3;

public class X3DeliverySimpleInfo {

	private String documentNr;
	private String documentLine;
	private String productCode;
	private int quantityLeftToGet;
	private int quantityOrdered;
	private int quantityReceived;
	private java.util.Date date;
	private String supplierCode;
	private String supplierName;
	private String country;
	
	public X3DeliverySimpleInfo() {
	
	}

	public String getDocumentNr() {
		return documentNr;
	}

	public void setDocumentNr(String documentNr) {
		this.documentNr = documentNr;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	

	public String getDocumentLine() {
		return documentLine;
	}

	public void setDocumentLine(String documentLine) {
		this.documentLine = documentLine;
	}
	
	public int getQuantityLeftToGet() {
		return quantityLeftToGet;
	}

	public void setQuantityLeftToGet(int quantityLeftToGet) {
		this.quantityLeftToGet = quantityLeftToGet;
	}

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public int getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(int quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Override
	public String toString() {
		return "X3DeliverySimpleInfo [documentNr=" + documentNr + ", documentLine=" + documentLine + ", productCode="
				+ productCode + ", quantityLeftToGet=" + quantityLeftToGet + ", quantityOrdered=" + quantityOrdered
				+ ", quantityReceived=" + quantityReceived + ", date=" + date + ", supplierCode=" + supplierCode
				+ ", supplierName=" + supplierName + ", country=" + country + "]";
	}



	
}

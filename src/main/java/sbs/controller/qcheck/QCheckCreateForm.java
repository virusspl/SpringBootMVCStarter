package sbs.controller.qcheck;

import javax.validation.constraints.Size;

public class QCheckCreateForm {

	@Size(min = 3, max = 30)
	private String productCode;
	@Size(min = 5)
	private String contents;
	
	public QCheckCreateForm() {
		
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "QCheckCreateForm [productCode=" + productCode + ", contents=" + contents + "]";
	}
	
}

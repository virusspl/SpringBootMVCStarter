package sbs.controller.compreq;

import javax.validation.constraints.Size;

public class FormRequestCreate {

	@Size(min=1, max=25)
	String productCode;
	@Size(min=1, max=25)
	String orderNumber;
	@Size(min=1, max=10)
	String workstationCode;
	@Size(max=255)
	String comment;

	public FormRequestCreate() {
		
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getWorkstationCode() {
		return workstationCode;
	}

	public void setWorkstationCode(String workstationCode) {
		this.workstationCode = workstationCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "FormRequestCreate [productCode=" + productCode + ", orderNumber=" + orderNumber + ", workstationCode="
				+ workstationCode + ", comment=" + comment + "]";
	}
	
	
	
}

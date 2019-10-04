package sbs.controller.compreq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FormCompReqLineCreate {

	@NotNull
	int requestId;
	@Size(min=1, max=25)
	String componentCode;
	@NotNull
	@Min(1)
	int quantity;
	
	public FormCompReqLineCreate() {
	
	}
	
	public FormCompReqLineCreate(int requestId) {
		this.requestId = requestId;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getComponentCode() {
		return componentCode;
	}

	public void setComponentCode(String componentCode) {
		this.componentCode = componentCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "FormCompReqLineCreate [requestId=" + requestId + ", componentCode=" + componentCode + ", quantity="
				+ quantity + "]";
	}
	
	
	
}

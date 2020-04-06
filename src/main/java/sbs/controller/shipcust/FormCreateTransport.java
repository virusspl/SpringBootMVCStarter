package sbs.controller.shipcust;

import javax.validation.constraints.Size;

public class FormCreateTransport {

	@Size(min=3, max=150)
	private String transport;

	public FormCreateTransport() {
	
	}
	
	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	@Override
	public String toString() {
		return "FormCreateTransport [transport=" + transport + "]";
	}

}

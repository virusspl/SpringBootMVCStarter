package sbs.controller.downtimes;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FormCause {

	@NotNull
	private int id;
	private int typeId;
	@NotNull
	@Min(1)
	private int order;
	@Size(min = 5, max = 255)
	private String text;
	@Size(min = 5, max = 100)
	private String shortText;
	private boolean active;
	
	public FormCause() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@Override
	public String toString() {
		return "FormCause [id=" + id + ", typeId=" + typeId + ", order=" + order + ", text=" + text + ", shortText="
				+ shortText + ", active=" + active + "]";
	}


	
	
	
}

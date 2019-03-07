package sbs.controller.qsurveys;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class FormQuestionCreate {

	@NotNull
	private int templateId;
	private int typeId;
	@NotNull
	private int order;
	@Size(min = 5, max = 255)
	private String longText;
	@Size(min = 3, max = 50)
	private String shortText;
	
	public FormQuestionCreate() {
	
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
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

	public String getLongText() {
		return longText;
	}

	public void setLongText(String longText) {
		this.longText = longText;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@Override
	public String toString() {
		return "FormQuestionCreateForm [templateId=" + templateId + ", typeId=" + typeId + ", order=" + order
				+ ", longText=" + longText + ", shortText=" + shortText + "]";
	}
	
	
	
}

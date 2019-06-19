package sbs.controller.shipments;

public class ShipmentTerminalForm {

	public static final int STEP_CHOOSE_SHIPMENT = 1;
	public static final int STEP_CHOOSE_ACTION = 2;
	public static final int STEP_ENTER_ORDER = 3;
	public static final int STEP_ENTER_CODE = 4;
	public static final int STEP_ENTER_QUANTITY = 5;
	public static final int STEP_SUMMARY = 6;
	
	private int stepChooseShipment = STEP_CHOOSE_SHIPMENT; // enter id
	private int stepChooseAction = STEP_CHOOSE_ACTION; // start new line or set completed
											// or change shipment
	private int stepEnterOrder = STEP_ENTER_ORDER; // enter order
	private int stepEnterCode = STEP_ENTER_CODE; // enter code
	private int stepEnterQuantity = STEP_ENTER_QUANTITY; // enter quantity
	private int stepSummary = STEP_SUMMARY; // save or cancel (both: back to
										// dispatch)
	

	private String currentColumnName;
	private int currentStep;
	private String currentValue;
	private int currentShipment;
	private String code;
	private String description;
	private String category;
	private String order;
	private int quantity;
	private String clientName;
	private String company;
	

	public ShipmentTerminalForm() {
		this.currentStep = 1;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}

	public int getCurrentShipment() {
		return currentShipment;
	}

	public void setCurrentShipment(int currentShipment) {
		this.currentShipment = currentShipment;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	

	public String getCurrentColumnName() {
		return currentColumnName;
	}

	public void setCurrentColumnName(String currentColumnName) {
		this.currentColumnName = currentColumnName;
	}
	

	public int getStepChooseShipment() {
		return stepChooseShipment;
	}

	public void setStepChooseShipment(int stepChooseShipment) {
		this.stepChooseShipment = stepChooseShipment;
	}

	public int getStepChooseAction() {
		return stepChooseAction;
	}

	public void setStepChooseAction(int stepChooseAction) {
		this.stepChooseAction = stepChooseAction;
	}

	public int getStepEnterOrder() {
		return stepEnterOrder;
	}

	public void setStepEnterOrder(int stepEnterOrder) {
		this.stepEnterOrder = stepEnterOrder;
	}

	public int getStepEnterCode() {
		return stepEnterCode;
	}

	public void setStepEnterCode(int stepEnterCode) {
		this.stepEnterCode = stepEnterCode;
	}

	public int getStepEnterQuantity() {
		return stepEnterQuantity;
	}

	public void setStepEnterQuantity(int stepEnterQuantity) {
		this.stepEnterQuantity = stepEnterQuantity;
	}

	public int getStepSummary() {
		return stepSummary;
	}

	public void setStepSummary(int stepSummary) {
		this.stepSummary = stepSummary;
	}
	

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "ShipmentTerminalForm [currentStep=" + currentStep + ", currentShipment=" + currentShipment + ", code="
				+ code + ", description=" + description + ", category=" + category + ", order=" + order + ", quantity="
				+ quantity + "]";
	}


}

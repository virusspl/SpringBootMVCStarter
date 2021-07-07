package sbs.controller.capacity;

public class CapacityLine {
	
	
	
	private String order;
	private String code;
	private String description;
	private String machine;
	private int quantity;
	private int quantityRejected;
	private int operation;
	private int nextOperation;
	private String group;
	private String operationDescription;
	
	public CapacityLine() {

	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantityRejected() {
		return quantityRejected;
	}

	public void setQuantityRejected(int quantityRejected) {
		this.quantityRejected = quantityRejected;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getNextOperation() {
		return nextOperation;
	}

	public void setNextOperation(int nextOperation) {
		this.nextOperation = nextOperation;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getOperationDescription() {
		return operationDescription;
	}

	public void setOperationDescription(String operationDescription) {
		this.operationDescription = operationDescription;
	}

	@Override
	public String toString() {
		return "CapacityLine [order=" + order + ", code=" + code + ", description=" + description + ", machine="
				+ machine + ", quantity=" + quantity + ", quantityRejected=" + quantityRejected + ", operation="
				+ operation + ", nextOperation=" + nextOperation + ", group=" + group + ", operationDescription="
				+ operationDescription + "]";
	}
	
	
	
	
}

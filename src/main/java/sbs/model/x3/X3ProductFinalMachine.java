package sbs.model.x3;

public class X3ProductFinalMachine {

	private String productCode;
	private int operation;
	private String machineCode;
	private String machineName;
	private String machineGroup;
	public static final String MECHANICAL = "MECHANICAL";
	public static final String WELDING = "WELDING";
	public static final String ASSEMBLY = "ASSEMBLY";
	
	
	public X3ProductFinalMachine() {
	
	}
	
	public X3ProductFinalMachine(String productCode, int operation, String machineCode, String machineName,
			String machineGroup) {
		super();
		this.productCode = productCode;
		this.operation = operation;
		this.machineCode = machineCode;
		this.machineName = machineName;
		this.machineGroup = machineGroup;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getMachineGroup() {
		return machineGroup;
	}

	public void setMachineGroup(String machineGroup) {
		this.machineGroup = machineGroup;
	}

	@Override
	public String toString() {
		return "X3ProductFinalMachine [productCode=" + productCode + ", operation=" + operation + ", machineCode="
				+ machineCode + ", machineName=" + machineName + ", machineGroup=" + machineGroup + "]";
	}
	
	public String getMachineDepartment() {
		switch (getMachineGroup()) {
		case "01":
			return MECHANICAL;
		case "02":
			return MECHANICAL;
		case "03":
			return MECHANICAL;
		case "04":
			return MECHANICAL;
		case "06":
			return MECHANICAL;
		case "07":
			return MECHANICAL;
		case "14":
			return MECHANICAL;
		case "15":
			return WELDING;
		case "16":
			return WELDING;
		case "18":
			return WELDING;
		case "19":
			return WELDING;
		case "08":
			return ASSEMBLY;
		case "09":
			return ASSEMBLY;
		case "10":
			return ASSEMBLY;
		case "11":
			return ASSEMBLY;
		case "12":
			return ASSEMBLY;
		case "13":
			return ASSEMBLY;
		default:
			return "00";
		}
	}
	
	
	
	
}

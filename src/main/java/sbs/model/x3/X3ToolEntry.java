package sbs.model.x3;

public class X3ToolEntry {
	
	private String code;
	private int operation;
	private String machine;
	private String tool;
	
	public X3ToolEntry() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	@Override
	public String toString() {
		return "X3ToolEntry [code=" + code + ", operation=" + operation + ", machine=" + machine + ", tool=" + tool
				+ "]";
	}


	
}

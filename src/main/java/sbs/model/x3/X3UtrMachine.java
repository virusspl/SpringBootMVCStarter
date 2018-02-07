package sbs.model.x3;

public class X3UtrMachine {

	private String code;
	private String name;
	private boolean critical;
	
	public X3UtrMachine() {
		
	}

	public X3UtrMachine(String code, String name, boolean critical) {
		super();
		this.code = code;
		this.name = name;
		this.critical =  critical;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	@Override
	public String toString() {
		return "X3UtrMachine [code=" + code + ", name=" + name + ", critical=" + critical + "]";
	}


	
	
	
}

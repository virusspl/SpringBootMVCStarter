package sbs.model.x3;

public class X3Workstation {

	private String code;
	private String name;
	
	public X3Workstation() {
		
	}

	public X3Workstation(String code, String name) {
		super();
		this.code = code;
		this.name = name;
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

	@Override
	public String toString() {
		return "X3Workstation [code=" + code + ", name=" + name + "]";
	}
	
	
	
}

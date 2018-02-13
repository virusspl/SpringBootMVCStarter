package sbs.model.x3;

public class X3UtrWorker {

	private String code;
	private String name;
	private String workTimeInHours;
	
	public X3UtrWorker() {

	}

	public X3UtrWorker(String code, String name) {
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
		return "X3UtrWorker [code=" + code + ", name=" + name + "]";
	}

	public String getWorkTimeInHours() {
		return workTimeInHours;
	}

	public void setWorkTimeInHours(String workTimeInHours) {
		this.workTimeInHours = workTimeInHours;
	}
	
	
	
}

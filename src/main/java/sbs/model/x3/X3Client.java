package sbs.model.x3;

public class X3Client {

	private String code;
	private String name;
	
	public X3Client() {
		
	}

	public X3Client(String code, String name) {
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
		return "X3Client [code=" + code + ", name=" + name + "]";
	}
	
	
	
}

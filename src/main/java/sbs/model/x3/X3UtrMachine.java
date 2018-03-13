package sbs.model.x3;

public class X3UtrMachine {

	private String code;
	private String codeNicim;
	private String name;
	private boolean critical;
	public static final boolean CRITICAL = true;
	public static final boolean NONCRITICAL = false;
	
	public X3UtrMachine() {
	}

	public X3UtrMachine(String code, String codeNicim, String name, boolean critical) {
		super();
		this.code = code;
		this.codeNicim = codeNicim;
		this.name = name;
		this.critical =  critical;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeNicim() {
		return codeNicim;
	}

	public void setCodeNicim(String codeNicim) {
		this.codeNicim = codeNicim;
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

package sbs.model.x3;

public class X3UtrMachine {

	private String code;
	private String codeNicim;
	private String name;
	private boolean critical;
	private int company;
	public static final boolean CRITICAL = true;
	public static final boolean NONCRITICAL = false;
	public static final int ADRP = 1;
	public static final int WPS = 2;
	
	public X3UtrMachine() {
	}

	public X3UtrMachine(String code, String codeNicim, String name, boolean critical, int company) {
		super();
		this.code = code;
		this.codeNicim = codeNicim;
		this.name = name;
		this.critical =  critical;
		this.company =  company;
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

	public int getCompany() {
		return company;
	}

	public void setCompany(int company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "X3UtrMachine [code=" + code + ", codeNicim=" + codeNicim + ", name=" + name + ", critical=" + critical
				+ ", company=" + company + "]";
	}

}

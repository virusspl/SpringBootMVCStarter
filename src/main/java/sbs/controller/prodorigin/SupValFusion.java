package sbs.controller.prodorigin;

public class SupValFusion {

	private SupVal europeLine;
	private SupVal importLine;
	
	public SupValFusion() {
	
	}
	
	public SupValFusion(SupVal europeLine, SupVal importLine) {	
		this.europeLine = europeLine;
		this.importLine = importLine;
	}
	
	public SupVal getEuropeLine() {
		return europeLine;
	}
	public void setEuropeLine(SupVal europeLine) {
		this.europeLine = europeLine;
	}
	public SupVal getImportLine() {
		return importLine;
	}
	public void setImportLine(SupVal importLine) {
		this.importLine = importLine;
	}

	@Override
	public String toString() {
		return "SupValFusion [europeLine=" + europeLine + ", importLine=" + importLine + "]";
	}
	
	
	
	
}

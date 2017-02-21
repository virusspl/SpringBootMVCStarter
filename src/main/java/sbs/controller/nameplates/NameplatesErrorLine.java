package sbs.controller.nameplates;

public class NameplatesErrorLine {

	long lineNo;
	String line;
	
	public NameplatesErrorLine() {

	}
	public NameplatesErrorLine(long lineNo, String line) {
		super();
		this.lineNo = lineNo;
		this.line = line;
	}
	/**
	 * @return the lineNo
	 */
	public long getLineNo() {
		return lineNo;
	}
	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(long lineNo) {
		this.lineNo = lineNo;
	}
	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}
	
	
	
}

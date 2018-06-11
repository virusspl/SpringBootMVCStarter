package sbs.helpers;

import java.util.ArrayList;

public class ExcelContents {

	private String fileName;
	private String sheetName;
	private ArrayList<String> headers;
	private ArrayList<ArrayList<Object>> values;

	public ExcelContents() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public ArrayList<String> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}

	public ArrayList<ArrayList<Object>> getValues() {
		return values;
	}

	public void setValues(ArrayList<ArrayList<Object>> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "ExcelContents [fileName=" + fileName + ", sheetName=" + sheetName + ", headers=" + headers + ", values="
				+ values + "]";
	}



}

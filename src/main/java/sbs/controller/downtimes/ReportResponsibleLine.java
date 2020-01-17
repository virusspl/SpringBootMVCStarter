package sbs.controller.downtimes;

import java.sql.Timestamp;

public class ReportResponsibleLine {

	private int id;
	private Timestamp date;
	private String cause;
	private String type;
	private String responsible;
	private boolean opened;
	private String response;
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	@Override
	public String toString() {
		return "ReportResponsibleLine [date=" + date + ", cause=" + cause + ", type=" + type + ", responsible="
				+ responsible + ", opened=" + opened + ", response=" + response + "]";
	}
	
	
	
	
	
}

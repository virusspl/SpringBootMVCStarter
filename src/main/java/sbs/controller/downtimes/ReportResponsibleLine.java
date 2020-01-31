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
	private String initComment;
	private String respComment;
	private String endComment;
	
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
	
	
	public String getInitComment() {
		return initComment;
	}

	public void setInitComment(String initComment) {
		this.initComment = initComment;
	}

	public String getRespComment() {
		return respComment;
	}

	public void setRespComment(String respComment) {
		this.respComment = respComment;
	}

	public String getEndComment() {
		return endComment;
	}

	public void setEndComment(String endComment) {
		this.endComment = endComment;
	}

	@Override
	public String toString() {
		return "ReportResponsibleLine [date=" + date + ", cause=" + cause + ", type=" + type + ", responsible="
				+ responsible + ", opened=" + opened + ", response=" + response + "]";
	}
	
	
	
	
	
}

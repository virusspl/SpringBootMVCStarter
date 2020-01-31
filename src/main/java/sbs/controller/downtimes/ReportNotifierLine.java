package sbs.controller.downtimes;

import java.sql.Timestamp;
import java.util.Date;

public class ReportNotifierLine {

	private int id;
	private Timestamp date;
	private String machine;
	private String type;
	private String cause;
	private String length;
	private String notifier;
	private String department;
	private boolean opened;
	private String initComment;
	private String respComment;
	private String endComment;
		
	public ReportNotifierLine() {

	}

	
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


	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getNotifier() {
		return notifier;
	}

	public void setNotifier(String notifier) {
		this.notifier = notifier;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public boolean isOpened() {
		return opened;
	}


	public void setOpened(boolean opened) {
		this.opened = opened;
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


	public void setCalculatedLength(Timestamp endDate) {
		
		if(this.date == null) {
			this.length = "N/D";
			// exit
			return;
		}
		
		if(endDate == null) {
			endDate = new Timestamp(new Date().getTime());
		}
		
		Long diff = endDate.getTime() - date.getTime();
		
		long hours = diff / (1000*60*60L);
		long minutes = diff % (1000*60*60L) / (1000*60L);
		
		length = hours + ":" + minutes;
		
	}
	
	
	
}

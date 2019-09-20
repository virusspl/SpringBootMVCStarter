package sbs.controller.downtimes;

import javax.validation.constraints.Size;

public class FormDowntimeClose {

	int downtimeId;
	@Size(max=255)
	String endComment;
	
	public FormDowntimeClose() {
	
	}
	
	public FormDowntimeClose(int downtimeId) {
		this.downtimeId = downtimeId;
	}

	public int getDowntimeId() {
		return downtimeId;
	}

	public void setDowntimeId(int downtimeId) {
		this.downtimeId = downtimeId;
	}

	public String getEndComment() {
		return endComment;
	}

	public void setEndComment(String endComment) {
		this.endComment = endComment;
	}
	
	
	
}

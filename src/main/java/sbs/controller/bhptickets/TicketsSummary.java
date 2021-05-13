package sbs.controller.bhptickets;

import java.util.ArrayList;
import java.util.List;


import sbs.helpers.DateHelper;
import sbs.model.bhptickets.BhpTicket;

public class TicketsSummary {
	
	
	DateHelper dateHelper;

	List<BhpTicket> list;
	// all in period
	private int created;
	// closed (closed = closed, cancelled, archived) 
	private int closed;
	// state reopened
	private int reopened;
	// average time: sum / number of closed
	private long averageCloseTime;
	// date format
	private String averageCloseTimeString;
	// closed / created
	private int closedPercent;
	// sum of pending time in closed tickets
	private long pendingTime;
	
	public TicketsSummary() {
		this.dateHelper = new DateHelper();
		this.list = new ArrayList<>();
		this.averageCloseTimeString = "";
	}
	
	public void addTicket(BhpTicket ticket) {
		this.list.add(ticket);
	}
	
	public void calculateResults() {
		// reset
		this.resetCalculationResults();
		
		for(BhpTicket t: list) {
			// created (!= cancelled)
			if(t.getState().getOrder()!=90) {
				this.created++;
			}
			
			if(t.getState().getOrder() >= 40 & t.getState().getOrder() != 90) {
				// closed, archived
				this.closed++;	
				this.pendingTime += t.getUpdateDate().getTime() - t.getCreationDate().getTime();
			}
			else if (t.getReopened()) {
				// reopened
				this.reopened++;
			}
		}
		if(this.closed > 0) {
			this.averageCloseTime = this.pendingTime/this.closed;
			this.averageCloseTimeString = this.dateHelper.convertMillisToTimeString(averageCloseTime);
		}
		else {
			this.averageCloseTime = -1;
			this.averageCloseTimeString = "N/D";
		}
		
		if(this.created > 0) {
			this.closedPercent = this.closed * 100 / this.created;
		}
		else {
			this.closedPercent = 0;
		}
	}

	private void resetCalculationResults() {
		this.created = 0;
		this.closed = 0;
		this.reopened = 0;
		this.averageCloseTime = 0;
		this.averageCloseTimeString ="";
		this.closedPercent = 0;
		this.pendingTime = 0;
	}

	public List<BhpTicket> getList() {
		return list;
	}

	public int getCreated() {
		return created;
	}

	public int getClosed() {
		return closed;
	}

	public int getClosedPercent() {
		return closedPercent;
	}

	public int getReopened() {
		return reopened;
	}

	public long getAverageCloseTime() {
		return averageCloseTime;
	}

	public String getAverageCloseTimeString() {
		return averageCloseTimeString;
	}

	@Override
	public String toString() {
		return "TicketsSummary [list=[" + list.size() + "], created=" + created + ", closed=" + closed + ", closedPercent="
				+ closedPercent + ", reopened=" + reopened + ", averageCloseTime=" + averageCloseTime
				+ ", averageCloseTimeString=" + averageCloseTimeString + "]";
	}
	
	
	
	
	
}

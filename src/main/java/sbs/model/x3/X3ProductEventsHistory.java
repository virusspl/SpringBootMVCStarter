package sbs.model.x3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sbs.helpers.DateHelper;

public class X3ProductEventsHistory implements Serializable{

	private static final long serialVersionUID = -6928601982151291537L;
	
	private String productCode;
	private List<X3ProductEvent> events;
	private int zeroCounter;
	private int minimumCounter;
	private int zeroDays;
	private int minimumDays;
	private String zeroDates;
	private String minimumDates;
	private DateHelper dateHelper;
	
	public X3ProductEventsHistory(String productCode) {
		this.productCode = productCode;
		this.events = new ArrayList<>(); 
		this.dateHelper = new DateHelper();
	}
	
	private void resetStats() {
		this.zeroCounter = 0;
		this.minimumCounter = 0;
		this.zeroDays = 0;
		this.minimumDays = 0;
		this.zeroDates = "";
		this.minimumDates = "";
	}
	
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public List<X3ProductEvent> getEvents() {
		return events;
	}
	public void setEvents(List<X3ProductEvent> events) {
		this.events = events;
	}
	
	public void addEvent(X3ProductEvent event) {
		this.events.add(event);
	}

	public static long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public void countDrops(int minStock) {
		this.resetStats();
		
		Date zeroStart = new java.util.Date();
		Date zeroEnd = new java.util.Date();
		
		Date minimumStart = new java.util.Date();
		Date minimumEnd = new java.util.Date();
		
		boolean duringZero = false;
		boolean duringMinimum = false;
		
		for(int i = 0; i<events.size(); i++) {
			if(events.get(i).getAfter() <= 0 && !duringZero && i > 0 && i < (events.size()-1)) {
				this.addZeroDate(dateHelper.formatDdMmYyyy(events.get(i).getDate()));
				this.zeroCounter++;
				zeroStart = events.get(i).getDate();
				duringZero = true;
			}
			else if(events.get(i).getAfter() > 0) {
				if(duringZero) {
					zeroEnd = events.get(i).getDate();
					this.zeroDays+= getDifferenceDays(zeroStart, zeroEnd);
					duringZero = false;
				}
			}

			// count below minimum days
			if(events.get(i).getAfter() < minStock && !duringMinimum && i > 0 && i < (events.size()-1)) {
				this.addMinimumDate(dateHelper.formatDdMmYyyy(events.get(i).getDate()));
				this.minimumCounter++;
				minimumStart = events.get(i).getDate();
				duringMinimum = true;
			}
			else if(events.get(i).getAfter() > minStock) {
				if(duringMinimum) {
					minimumEnd = events.get(i).getDate();
					this.minimumDays+= getDifferenceDays(minimumStart, minimumEnd);
					duringMinimum = false;
				}
			}
		}
	}

	private void addZeroDate(String dateString) {
		this.zeroDates += dateString + "; ";
	}
	
	private void addMinimumDate(String dateString) {
		this.minimumDates += dateString + "; ";
	}
	
	public String getZeroDates() {
		return zeroDates;
	}
	
	public String getMinimumDates() {
		return minimumDates;
	}

	public int getZeroCounter() {
		return zeroCounter;
	}

	public int getMinimumCounter() {
		return minimumCounter;
	}

	public int getZeroDays() {
		return zeroDays;
	}

	public int getMinimumDays() {
		return minimumDays;
	}


}

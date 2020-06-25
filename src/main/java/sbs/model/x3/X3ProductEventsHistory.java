package sbs.model.x3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class X3ProductEventsHistory implements Serializable{

	private static final long serialVersionUID = -6928601982151291537L;
	
	private String productCode;
	private List<X3ProductEvent> events;
	private int zeroCounter;
	private int minimumCounter;
	private int zeroDays;
	
	public X3ProductEventsHistory(String productCode) {
		this.productCode = productCode;
		this.events = new ArrayList<>(); 
	}
	
	private void resetStats() {
		this.zeroCounter = 0;
		this.minimumCounter = 0;
		this.zeroDays = 0;
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
		
		boolean duringZero = false;
		boolean duringMinimum = false;
		for(int i = 0; i<events.size(); i++) {
			if(events.get(i).getAfter() <= 0 && !duringZero && i > 0 && i < (events.size()-1)) {
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
				this.minimumCounter++;
				duringMinimum = true;
			}
			else if(events.get(i).getAfter() > minStock) {
				if(duringMinimum) {
					duringMinimum = false;
				}
			}
		}
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

	
}

package sbs.model.cebs;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "cebs_events")
public class CebsEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ce_id")
	private int eventId;

	@Column(name = "ce_location_code", length = 10, nullable = false)
	private String locationCode;
	
	@Column(name = "ce_location", length = 60, nullable = false)
	private String location;
	
	@Column(name = "ce_active", nullable = false )
	private boolean active;
	
	@Column(name = "ce_confirmed", nullable = false )
	private boolean confirmed;
	
	@Column(name = "ce_sent", nullable = false )
	private boolean sent;
	
	@Column(name = "ce_action_date", nullable = false)
	private Timestamp actionDate;
		
	@Column(name = "ce_day_code", length = 30, nullable = false)
	private String dayCode;
	
	@Column(name = "ce_organizer", length = 50, nullable = false)
	private String organizer;
	
	
	public CebsEvent() {

	}


	public int getEventId() {
		return eventId;
	}


	public void setEventId(int eventId) {
		this.eventId = eventId;
	}


	public String getLocationCode() {
		return locationCode;
	}


	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public boolean isConfirmed() {
		return confirmed;
	}


	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}


	public boolean isSent() {
		return sent;
	}


	public void setSent(boolean sent) {
		this.sent = sent;
	}


	public Timestamp getActionDate() {
		return actionDate;
	}


	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}


	public String getDayCode() {
		return dayCode;
	}


	public void setDayCode(String dayCode) {
		this.dayCode = dayCode;
	}


	public String getOrganizer() {
		return organizer;
	}


	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}


	@Override
	public String toString() {
		return "CebsEvent [eventId=" + eventId + ", locationCode=" + locationCode + ", location=" + location
				+ ", active=" + active + ", confirmed=" + confirmed + ", sent=" + sent + ", actionDate=" + actionDate
				+ ", dayCode=" + dayCode + ", organizer=" + organizer + "]";
	}
	
	

}

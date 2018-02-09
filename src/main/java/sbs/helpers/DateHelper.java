package sbs.helpers;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.stereotype.Component;
@Component
public class DateHelper {
	
	
	public Timestamp getCurrentTime(){
		return new Timestamp(new java.util.Date().getTime());
	}
	
	public Timestamp getTime(Date date){
		return new Timestamp(date.getTime());
	}
	
	public String convertMinutesToHours(int minutes){
		return (minutes/60) + ":" + (minutes%60); 
	}
	
}

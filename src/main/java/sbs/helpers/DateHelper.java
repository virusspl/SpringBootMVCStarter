package sbs.helpers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
@Component
public class DateHelper {
	
	private SimpleDateFormat ddMmYyyyFormat;
	
	public DateHelper(){
		ddMmYyyyFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public Timestamp getCurrentTime(){
		return new Timestamp(new java.util.Date().getTime());
	}
	
	public Timestamp getTime(Date date){
		return new Timestamp(date.getTime());
	}
	
	public String convertMinutesToHours(int minutes){
		return (minutes/60) + ":" + (minutes%60); 
	}
	
	public String formatDdMmYyyy(Date date){
		return ddMmYyyyFormat.format(date);
	}
	
	public String formatDdMmYyyy(Timestamp date){
		return ddMmYyyyFormat.format(date);
	}
	
}

package sbs.helpers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
@Component
public class DateHelper {
	
	private SimpleDateFormat ddMmYyyyFormat;
	private SimpleDateFormat ddMmYyFormat;
	
	
	public DateHelper(){
		ddMmYyyyFormat = new SimpleDateFormat("dd/MM/yyyy");
		ddMmYyFormat = new SimpleDateFormat("dd/MM/yy");
	}
	
	public Timestamp getCurrentTime(){
		return new Timestamp(new java.util.Date().getTime());
	}
	
	public Timestamp getTime(Date date){
		return new Timestamp(date.getTime());
	}
	
	public String convertMinutesToHours(int minutes){
		return String.format("%02d", minutes/60) + ":" + String.format("%02d", minutes%60); 
	}
	
	public String formatDdMmYyyy(Date date){
		return ddMmYyyyFormat.format(date);
	}
	
	public String formatDdMmYyyy(Timestamp date){
		return ddMmYyyyFormat.format(date);
	}
	
	public String formatDdMmYy(Date date){
		return ddMmYyFormat.format(date);
	}
	
	public String formatDdMmYy(Timestamp date){
		return ddMmYyFormat.format(date);
	}
	
}

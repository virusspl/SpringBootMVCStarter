package sbs.helpers;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;
@Component
public class DateHelper {

	public Timestamp getCurrentTime(){
		return new Timestamp(new java.util.Date().getTime());
	}
	
}

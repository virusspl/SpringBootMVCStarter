package sbs.helpers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;
@Component
public class DateHelper implements Serializable{
	

	private static final long serialVersionUID = 3797390353639381515L;
	
	private SimpleDateFormat YyyyMmDdFormat;
	private SimpleDateFormat YyyyMmDdFormatDot;
	private SimpleDateFormat ddMmYyyyFormat;
	private SimpleDateFormat ddMmYyyyFormatDot;
	private SimpleDateFormat ddMmYyFormat;
	private SimpleDateFormat ddMmYyyyHhMmFormat;
	private SimpleDateFormat YyyyMmDdHhMmFormat;
	private SimpleDateFormat ddMmYyyyHhMmFormatDot;
	private SimpleDateFormat YyyyMmDdHhMmFormatDot;
	private SimpleDateFormat YyyyMmDdHhMmSsNoSpecialFormat;
	
	//TODO
	/*
	Long diff = endDate.getTime() - startDate.getTime();
	long seconds = diff / 1000;
	long minutes = seconds / 60;
	long hours = minutes / 60;
	long days = hours / 24;
	length = days + "d " + hours % 24 + "h " + minutes % 60 + "m " + seconds % 60 + "s ";
	*/
	
	public DateHelper(){
		YyyyMmDdFormat = new SimpleDateFormat("yyyy/MM/dd");
		YyyyMmDdFormatDot = new SimpleDateFormat("yyyy.MM.dd");
		ddMmYyyyFormat = new SimpleDateFormat("dd/MM/yyyy");
		ddMmYyyyFormatDot = new SimpleDateFormat("dd.MM.yyyy");
		ddMmYyFormat = new SimpleDateFormat("dd/MM/yy");
		ddMmYyyyHhMmFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		YyyyMmDdHhMmFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		ddMmYyyyHhMmFormatDot = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		YyyyMmDdHhMmFormatDot = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		YyyyMmDdHhMmSsNoSpecialFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	}
	
	public int extractMonth12(Timestamp date){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return (cal.get(Calendar.MONTH)+1);
	}
	
	public int extractMonth12(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return (cal.get(Calendar.MONTH)+1);
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
	
	/**
	 * 
	 * @param millis
	 * @return days, hours, minutes
	 */
	public String convertMillisToTimeString(long millis){
        Duration duration = Duration.ofMillis(millis);
        return String.format("%dd %dh %dm", 
                duration.toDays(),
                duration.toHours() % 24, 
                duration.toMinutes() % 60);
	}
	
	public String formatDdMmYyyy(Date date){
		return ddMmYyyyFormat.format(date);
	}
	
	public String formatDdMmYyyyDot(Date date){
		return ddMmYyyyFormatDot.format(date);
	}
	
	public String formatDdMmYyyy(Timestamp date){
		return ddMmYyyyFormat.format(date);
	}
	
	public String formatDdMmYyyyDot(Timestamp date){
		return ddMmYyyyFormatDot.format(date);
	}
	
	public String formatYyyyMmDd(Date date){
		return YyyyMmDdFormat.format(date);
	}
	
	public String formatYyyyMmDdDot(Date date){
		return YyyyMmDdFormatDot.format(date);
	}
	
	public String formatYyyyMmDd(Timestamp date){
		return YyyyMmDdFormat.format(date);
	}
	
	public String formatYyyyMmDdDot(Timestamp date){
		return YyyyMmDdFormatDot.format(date);
	}	

	public String formatYyyyMmDdHhMm(Date date) {
		return YyyyMmDdHhMmFormat.format(date);
	}

	public String formatYyyyMmDdHhMm(Timestamp date) {
		return YyyyMmDdHhMmFormat.format(date);
	}
	
	public String formatYyyyMmDdHhMmDot(Date date) {
		return YyyyMmDdHhMmFormatDot.format(date);
	}
	
	public String formatYyyyMmDdHhMmDot(Timestamp date) {
		return YyyyMmDdHhMmFormatDot.format(date);
	}

	public String formatYyyyMmDdHhMmSsNoSpecial(Date date) {
		return YyyyMmDdHhMmSsNoSpecialFormat.format(date);
	}
	
	public String formatDdMmYy(Date date){
		return ddMmYyFormat.format(date);
	}
	
	public String formatDdMmYy(Timestamp date){
		return ddMmYyFormat.format(date);
	}
	
	public String formatDdMmYyyyHhMm(Timestamp date){
		return ddMmYyyyHhMmFormat.format(date);
	}
	
	public String formatDdMmYyyyHhMm(Date date){
		return ddMmYyyyHhMmFormat.format(date);
	}
	
	public String formatDdMmYyyyHhMmDot(Timestamp date){
		return ddMmYyyyHhMmFormatDot.format(date);
	}
	
	public String formatDdMmYyyyHhMmDot(Date date){
		return ddMmYyyyHhMmFormatDot.format(date);
	}
	
	public boolean dateBeforeOrEqual(Calendar date, Calendar reference) {
		return (date.before(reference) || date.equals(reference));
	}
	
	public boolean dateBefore(Calendar date, Calendar reference) {
		return (date.before(reference));
	}

	public boolean dateAfterOrEqual(Calendar date, Calendar reference) {
		return (date.after(reference) || date.equals(reference));
	}
	
	public boolean dateAfter(Calendar date, Calendar reference) {
		return (date.after(reference));
	}

	public boolean dateInRange(Calendar date, Calendar start, Calendar end) {
		if ((date.after(start) || date.equals(start)) && (date.before(end) || date.equals(end))) {
			return true;
		}
		return false;
	}
	
	public boolean dateBeforeOrEqual(Timestamp date, Timestamp reference) {
		return (date.before(reference) || date.equals(reference));
	}
	
	public boolean dateBefore(Timestamp date, Timestamp reference) {
		return (date.before(reference));
	}

	public boolean dateAfterOrEqual(Timestamp date, Timestamp reference) {
		return (date.after(reference) || date.equals(reference));
	}
	
	public boolean dateAfter(Timestamp date, Timestamp reference) {
		return (date.after(reference));
	}
	
	public boolean isDateInRange(Timestamp date, Timestamp start, Timestamp end ){
		if ((date.after(start) || date.equals(start)) && (date.before(end) || date.equals(end))) {
			return true;
		}
		return false;
	}

	
}

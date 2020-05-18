package sbs.helpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Component;
@Component
public class TextHelper {
	
	NumberFormat formatterDotTwoDigits = new DecimalFormat("#0.00");     
	NumberFormat formatterIntegerRoundNoSpace = new DecimalFormat("#0");     
	

	public TextHelper() {
	}
	
	public String numberFormatDotNoSpace(double number){
		return formatterDotTwoDigits.format(number).replace(',', '.');
	}
	
	public String numberFormatIntegerRoundNoSpace(double number){
		return formatterIntegerRoundNoSpace.format(number);
	}
	
	public String numberFormatIntegerRoundWithSpace(double number){
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.getDefault());
		formatter.format("%(,.0f", number);
		formatter.close();
		return(sb.toString());
	}
	
	
	public String capitalize(String input){
		return WordUtils.capitalize(input);
	}

	public String capitalizeFull(String input){
		return WordUtils.capitalizeFully(input);
	}
	
	public String cut(String text, int numberOfChar){
		if(text.length()> numberOfChar){
			text = text.substring(0, numberOfChar) + "...";
		}
		
		return text;
	}
	
	public String newLine(){
		return System.getProperty("line.separator");
	}
	
	public String fillWithLeadingZero(String text, int targetCharacters){
		for (int i = 0; i < targetCharacters - text.length(); i++){
			text = "0" + text;
		}
		return text;
	}

	public String formatDouble2Digits(double quantity) {
		return String.format("%.2f", quantity);
	}

	public String formatDouble2OrNoDigits(double quantity) {
		if(quantity%1==0){
			return String.format("%.0f", quantity);
		}
		else{
			return String.format("%.2f", quantity);
		}
		
	}
	
	public boolean isMailValid(String entry) {
		 try {
		      InternetAddress emailAddr = new InternetAddress(entry);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      return false;
		   }
		return true;
	}
	
}

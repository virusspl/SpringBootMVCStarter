package sbs.helpers;

import org.springframework.stereotype.Component;
import org.apache.commons.lang3.text.WordUtils;
@Component
public class TextHelper {

	public TextHelper() {
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
	
}

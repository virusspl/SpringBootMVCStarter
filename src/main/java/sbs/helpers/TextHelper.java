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
	
	/*
	 * 
	 */
	public String capitalizeFull(String input){
		return WordUtils.capitalizeFully(input);
	}
	
	
	
}

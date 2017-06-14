package sbs.config.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

public class SimpleDateTimeFormatter implements Formatter<Date> {
    public static final String US_PATTERN = "MM.dd.yyyy";
    public static final String NORMAL_PATTERN = "dd.MM.yyyy";
    private SimpleDateFormat sdf;
    
    public SimpleDateTimeFormatter(){
    	sdf = new SimpleDateFormat();
    }

    @Override 
    public Date parse(String text, Locale locale) throws ParseException {
    	sdf.applyPattern(getPattern(locale));
    	return sdf.parse(text);
    }

    @Override 
    public String print(Date object, Locale locale) {
    	sdf.applyPattern(getPattern(locale));
    	return sdf.format(object);
    }

    public static String getPattern(Locale locale) {
        return isUnitedStates(locale) ? US_PATTERN : NORMAL_PATTERN;
    }

    private static boolean isUnitedStates(Locale locale) {
        return Locale.US.getCountry().equals(locale.getCountry());
    }


}

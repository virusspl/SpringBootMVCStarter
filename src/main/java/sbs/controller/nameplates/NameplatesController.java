package sbs.controller.nameplates;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("nameplates")
public class NameplatesController {

	@Autowired
	MessageSource messageSource;
	
	@RequestMapping("/list")
	public String list(Model model, Locale locale) throws FileNotFoundException {
		ArrayList<NameplatesLogLine> logLines = new ArrayList<>();
		ArrayList<NameplatesErrorLine> errorLines = new ArrayList<>();
		ArrayList<NameplatesLogLine> backlistLog;
		ArrayList<NameplatesErrorLine> backlistError;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"\\\\192.168.1.13\\atwsystem\\User_file\\Produkcja\\RF_ID\\MIFARE_Ultralight_Log.txt"));
			
			String line;
			List<String> split;
			NameplatesLogLine logLine;
			long i = 1;
			
			while ((line = in.readLine()) != null) {
				if (line.split(",").length != 7){
					errorLines.add(new NameplatesErrorLine(i,line));
				}
				else{
					logLine= new NameplatesLogLine();
					logLine.setLineNo(i);
					line = line.replace("\"","");
					split = Arrays.asList(line.split(","));
					logLine.setActionDate(split.get(0));
					logLine.setAction(split.get(1));
					logLine.setRfid(split.get(2));
					logLine.setProduct(split.get(3));
					logLine.setDate(split.get(4));
					logLine.setFlag(split.get(5));
					logLine.setComment(split.get(6));
					logLines.add(logLine);
				}
				i++;
			}
			in.close();
			
			backlistLog = new ArrayList<>();
			for (ListIterator<NameplatesLogLine> iterator = logLines.listIterator(logLines.size()); iterator.hasPrevious();) {
				backlistLog.add(iterator.previous());
			}

			backlistError = new ArrayList<>();
			for (ListIterator<NameplatesErrorLine> iterator = errorLines.listIterator(errorLines.size()); iterator.hasPrevious();) {
				backlistError.add(iterator.previous());
			}
			
			model.addAttribute("logLines", backlistLog);
			model.addAttribute("errorLines", backlistError);
		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException(
					messageSource.getMessage("error.file.not.found",null, locale)
					+ " "
					+ ex.getMessage());
		} catch (IOException ignore){}
		return "nameplates/list";
	}

}

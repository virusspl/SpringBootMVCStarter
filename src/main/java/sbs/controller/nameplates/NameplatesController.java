package sbs.controller.nameplates;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("nameplates")
public class NameplatesController {

	@RequestMapping("/list")
	public String jdbc(Model model) {

		
		ArrayList<NameplatesLogLine> logLines = new ArrayList<>();
		ArrayList<NameplatesErrorLine> errorLines = new ArrayList<>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"\\\\192.168.1.13\\atwsystem\\User_file\\Produkcja\\RF_ID\\MIFARE_Ultralight_Log.txt"));
			
			String line;
			List<String> split;
			NameplatesLogLine logLine;
			long i = 1;
			
			while ((line = in.readLine()) != null) {
				if (line.length()<90){
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
			model.addAttribute("logLines", logLines);
			model.addAttribute("errorLines", errorLines);
		} catch (Exception ex) {

		}
		return "nameplates/list";
	}

}

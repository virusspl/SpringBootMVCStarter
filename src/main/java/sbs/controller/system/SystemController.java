package sbs.controller.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sbs.service.system.SystemInfoParametersService;

@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	SystemInfoParametersService parametersService;
	
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	String logFilePath;

	public SystemController(Environment env) {
		logFilePath = env.getRequiredProperty("logging.file");
	}
	
	@RequestMapping("/log")
	public String home(Model model) {
		model.addAttribute("parameters", parametersService.findAll());
		model.addAttribute("logfile", logFilePath);
		//model.addAttribute("logContent", getLogContent());
		return "system/log";
	}

	@SuppressWarnings("unused")
	private String getLogContent() {
		String log = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(logFilePath)), "UTF8"));
			String st;
			while ((st = br.readLine()) != null) {
				log += st + System.getProperty("line.separator");
			}
			br.close();

		} catch (Exception ex) {
			log = ex.getMessage();
		}
		return log;
	}
}
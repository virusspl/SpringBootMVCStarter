package sbs.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import sbs.service.users.UserService;

@Component
public class FileHelper {

	private String tmpLogFilePath;
	@Autowired 
	UserService userService;
	

	public FileHelper() {
		this.tmpLogFilePath = "C:\\_APACHE_TOMCAT\\adr-app.log\\tmpLog.log";
	}

	public void appendToTmpLog(String text) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(tmpLogFilePath, true));
			writer.append(new java.util.Date() + " - ");
			writer.append(text + " - ");
			writer.append(userService.getAuthenticatedUser().getName());
			writer.append(System.getProperty("line.separator"));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Cacheable(value = "pdfListFromDirectory")
	public Map<String, String> getPdfMap(File directory) {
		Map<String, String> map = new TreeMap<>();
		getPdfListFromDirectory(directory, map);
		return map;
	}

	private void getPdfListFromDirectory(File directory, Map<String, String> result) {
		if (directory == null || !directory.isDirectory()) {
			return;
		}
		File[] files = directory.listFiles();

		if (files == null) {
			return;
		}

		for (int i = 0; i < files.length; i++) {

			if (files[i].isDirectory()) {
				getPdfListFromDirectory(files[i], result);
			} else {
				if (files[i].getName().endsWith(".pdf") || files[i].getName().endsWith(".PDF")) {
					result.put(files[i].getName().substring(0, files[i].getName().length() - 4),
							files[i].getAbsolutePath());
				}
			}
		}
	}

}

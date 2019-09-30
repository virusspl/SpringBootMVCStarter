package sbs.helpers;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class FileHelper {

	/*
	 * return Map<String,String> = Map<fileName,absolutePath>
	 */
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

		if(files == null){
			return;
		}
		
		for (int i = 0; i < files.length; i++) {

			if (files[i].isDirectory()) {
				getPdfListFromDirectory(files[i], result);
			} else {
				if (files[i].getName().endsWith(".pdf") || files[i].getName().endsWith(".PDF")) {
					result.put(files[i].getName().substring(0, files[i].getName().length()-4), files[i].getAbsolutePath());
				}
			}
		}
	}

}

package sbs.controller.docview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javassist.NotFoundException;
import sbs.helpers.FileHelper;

@Controller
@RequestMapping("docview")
public class DocViewController {
	
	public static final String TYPE_WORK_MANUALS = "workmanuals";
	public static final String TYPE_INFO_FORMS = "infoforms";

	@Autowired
	private FileHelper fileHelper;

	private String workManualsPath;
	private String infoFormsPath;
	
	private Map<String, String> workManuals;
	private Map<String, String> infoForms;

	/* inject PDF into HTML */
	// https://stackoverflow.com/questions/2740297/display-adobe-pdf-inside-a-div
	// https://scalified.com/2018/01/16/injecting-pdf-html-page/

	public DocViewController(Environment env) {
		workManualsPath = env.getRequiredProperty("industry.dir.workmanuals");
		infoFormsPath = env.getRequiredProperty("industry.dir.infoforms");
	}

	@RequestMapping("/dispatch")
	public String dispatch() {
		return "docview/dispatch";
	}

	@RequestMapping("/list/{type}")
	public String type(@PathVariable String type, Model model) throws NotFoundException {

		Map<String,String> currentMap;
		String currentTypeCode;
		
		switch (type) {
		case TYPE_WORK_MANUALS:
			workManuals = fileHelper.getPdfMap(new File(workManualsPath));
			currentMap = workManuals;
			currentTypeCode = "docview.type.workmanuals";
			break;
		case TYPE_INFO_FORMS:
			infoForms = fileHelper.getPdfMap(new File(infoFormsPath));
			currentMap = infoForms;
			currentTypeCode = "docview.type.infoforms";
			break;
		default:
			throw new NotFoundException("Document type unknown: '" + type + "'");
		}
		
		model.addAttribute("type", type);
		model.addAttribute("files", currentMap);
		model.addAttribute("typeCode", currentTypeCode);
		
		return "docview/list";
	}

	@RequestMapping(value = "/get/{type}", params = { "fileName" }, method = RequestMethod.POST)
	public void getFile(@PathVariable String type, @RequestParam String fileName, HttpServletResponse response,
			Locale locale) throws NotFoundException {

		Map<String,String> currentMap;

		switch (type) {
		case TYPE_WORK_MANUALS:
			currentMap = workManuals;
			break;
		case TYPE_INFO_FORMS:
			currentMap = infoForms;
			break;
		default:
			throw new NotFoundException("Document type unknown: '" + type + "'");
		}
		
		if (currentMap.containsKey(fileName)) {
			try {
				File file = new File(currentMap.get(fileName));
				InputStream is = FileUtils.openInputStream(file);
				org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
				response.setContentType("application/pdf");
				is.close();
			} catch (IOException ex) {
				throw new RuntimeException("I/O error while reading file '" + fileName + "': " + ex.getMessage());
			}
		}
	}
}

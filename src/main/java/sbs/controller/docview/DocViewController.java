package sbs.controller.docview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	public static final String TYPE_DRAWINGS_NOT_MERIDIAN = "notmeriddraw";

	@Autowired
	private FileHelper fileHelper;

	private String workManualsPath;
	private String infoFormsPath;
	private String notMeridianDrawingsPath;

	private Map<String, String> workManuals;
	private Map<String, String> infoForms;
	private Map<String, String> notMeridianDrawings;

	/* inject PDF into HTML */
	// https://stackoverflow.com/questions/2740297/display-adobe-pdf-inside-a-div
	// https://scalified.com/2018/01/16/injecting-pdf-html-page/

	public DocViewController(Environment env) {
		workManualsPath = env.getRequiredProperty("industry.dir.workmanuals");
		infoFormsPath = env.getRequiredProperty("industry.dir.infoforms");
		notMeridianDrawingsPath = env.getRequiredProperty("industry.dir.notmeriddraw");
	}

	@RequestMapping("/dispatch")
	public String dispatch() {
		return "docview/dispatch";
	}

	@RequestMapping("/list/{type}")
	public String type(@PathVariable String type, Model model) throws NotFoundException {

		Map<String, String> currentMap;
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
		case TYPE_DRAWINGS_NOT_MERIDIAN:
			notMeridianDrawings = fileHelper.getPdfMap(new File(notMeridianDrawingsPath));
			currentMap = notMeridianDrawings;
			currentTypeCode = "docview.type.notmeriddraw";
			break;
		default:
			throw new NotFoundException("Document type unknown: '" + type + "'");
		}

		model.addAttribute("type", type);
		model.addAttribute("files", currentMap);
		model.addAttribute("typeCode", currentTypeCode);

		return "docview/list";
	}

	@RequestMapping(value = "/get/{type}", params = { "fileName" }, method = RequestMethod.GET)
	// HttpServletResponse response, in arguments
	public ResponseEntity<byte[]> getFile(@PathVariable String type, @RequestParam String fileName, Locale locale)
			throws NotFoundException {

		Map<String, String> currentMap;

		switch (type) {
		case TYPE_WORK_MANUALS:
			currentMap = workManuals;
			break;
		case TYPE_INFO_FORMS:
			currentMap = infoForms;
			break;
		case TYPE_DRAWINGS_NOT_MERIDIAN:
			currentMap = notMeridianDrawings;
			break;
		default:
			throw new NotFoundException("Document type unknown: '" + type + "'");
		}

		if (currentMap.containsKey(fileName)) {
			try {
				File file = new File(currentMap.get(fileName));
				InputStream is = FileUtils.openInputStream(file);
				byte[] bytes = IOUtils.toByteArray(is);
				is.close();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.add("content-disposition", "inline;filename=" + fileName + ".pdf");
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
				return response;

			} catch (IOException ex) {
				throw new RuntimeException("I/O error while reading file '" + fileName + "': " + ex.getMessage());
			}
			
			/*
			try {
				File file = new File(currentMap.get(fileName));
				InputStream is = FileUtils.openInputStream(file);
				org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());

				response.addHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("application/pdf");

				// response.flushBuffer();
			} catch (IOException ex) {
				throw new RuntimeException("I/O error while reading file '" + fileName + "': " + ex.getMessage());
			}
			*/
			
		} else {
			throw new NotFoundException("File not found: '" + fileName + "'");
		}
	}
}

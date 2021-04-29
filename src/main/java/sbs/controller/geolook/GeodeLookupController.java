package sbs.controller.geolook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.controller.upload.UploadController;
import sbs.helpers.DateHelper;
import sbs.model.geode.GeolookRow;
import sbs.model.x3.X3Product;
import sbs.service.geode.JdbcOracleGeodeService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("/geolook")
public class GeodeLookupController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service jdbcOracleX3Service;
	@Autowired
	JdbcOracleGeodeService jdbcOracleGeodeService;
	@Autowired
	DateHelper dateHelper;
	@Autowired
	UploadController uploadController;

	@RequestMapping("/map")
	public String map(Model model) {
		//File[] root = uploadController.listFilesAsObjects("/static/images/map/");
		File[] root = uploadController.listFilesAsObjects(uploadController.getMapPhotoPath());
		HashMap<String, ArrayList<String>> directories = new HashMap<>();
		File[] tmpFileList;
		ArrayList<String> tmpNamesList;
		for(File dir: root){
			if(dir.isDirectory()){
				tmpFileList = dir.listFiles();
				tmpNamesList = new ArrayList<>();
				for(int i=0; i<tmpFileList.length; i++){
					if(FilenameUtils.getExtension(tmpFileList[i].getName()).equalsIgnoreCase("png")){
						tmpNamesList.add(FilenameUtils.removeExtension(tmpFileList[i].getName()));
					}
				}
				directories.put(dir.getName(), tmpNamesList);
			}
			
		}
		model.addAttribute("directories", directories);
		return "geolook/map";
	}

	@RequestMapping("/search")
	public String search(GeodeSearchForm geodeSearchForm) {
		return "geolook/search";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String performSearch(@Valid GeodeSearchForm geodeSearchForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale) {
		String itemDesc;
		if (bindingResult.hasErrors()) {
			return "geolook/search";
		}

		geodeSearchForm.setProduct(geodeSearchForm.getProduct().toUpperCase());
		itemDesc = jdbcOracleX3Service.findItemDescription("ATW", geodeSearchForm.getProduct());

		if (itemDesc == null) {
			bindingResult.rejectValue("product", "error.no.such.product", "ERROR");
			return "geolook/search";
		}
		redirectAttrs.addFlashAttribute("geodeList",
				jdbcOracleGeodeService.findLocationsOfProduct(geodeSearchForm.getProduct()));
		redirectAttrs.addFlashAttribute("itemCode", geodeSearchForm.getProduct());
		redirectAttrs.addFlashAttribute("itemDescription", itemDesc);

		return "redirect:/geolook/search";
	}

	@RequestMapping(value = "/search", params = { "csv" }, method = RequestMethod.POST)
	@Transactional
	public void getCsv(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// create temp file
		// https://www.mkyong.com/java/how-to-write-data-to-temporary-file-in-java/
		File downloadFile = File.createTempFile("geode", ".csv");
		List<GeolookRow> list = jdbcOracleGeodeService.findAllLocationsOfProducts();
		Map<String, X3Product> products = jdbcOracleX3Service.findAllActiveProductsMap("ATW");
		BufferedWriter bw = new BufferedWriter(new FileWriter(downloadFile));
		bw.write("store;address;product;description;quantity;unit;object;date" + System.getProperty("line.separator"));
		for (GeolookRow row : list) {
			bw.write(row.getStore() + ";");
			bw.write(row.getAddress() + ";");
			bw.write(row.getProduct() + ";");
			bw.write((products.containsKey(row.getProduct()) ? products.get(row.getProduct()).getDescription() : "-") + ";");
			bw.write(row.getQuantity() + ";");
			bw.write(row.getUnit() + ";");
			bw.write(row.getObject() + ";");
			bw.write(dateHelper.formatDdMmYyyy(row.getInputDate()));
			bw.write(System.getProperty("line.separator"));
		}
		bw.close();

		// input stream
		FileInputStream inputStream = new FileInputStream(downloadFile);
		
		// set headers
		String mimeType = "application/octet-stream";
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();

		// return "redirect:/geolook/search";
	}
}

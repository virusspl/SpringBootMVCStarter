package sbs.controller.upload;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.config.UploadProperties;
import sbs.helpers.DateHelper;
import sbs.helpers.ImageHelper;
import sbs.model.tools.ToolsProject;
import sbs.model.users.User;
import sbs.service.tools.ToolsProjectService;
import sbs.service.users.AvatarService;
import sbs.service.users.UserService;

@Controller
public class UploadController {
	
	@SuppressWarnings("unused")
	private final String uploadDir;
	private final String avatarUploadPath;
	private final String bhpPhotoPath;
	private final String toolsPhotoPath;
	private final String qSurveysPhotoPath;	
	private final String mapPhotoPath;
	
	@Autowired 
	UserService userService;
	@Autowired 
	AvatarService avatarService;
	@Autowired 
	MessageSource messageSource;
	@Autowired
	ImageHelper imageHelper;
	@Autowired
	ServletContext servletContext;
	@Autowired
	ToolsProjectService toolsProjectService;
	@Autowired
	DateHelper dateHelper;

    @Autowired
    public UploadController(UploadProperties uploadProperties) {
    	uploadDir = uploadProperties.getAvatarPath();
    	avatarUploadPath = uploadProperties.getAvatarPath();
    	bhpPhotoPath = uploadProperties.getBhpPhotoPath();
    	toolsPhotoPath = uploadProperties.getToolsPhotoPath();
    	mapPhotoPath = uploadProperties.getMapPhotoPath();
    	qSurveysPhotoPath = uploadProperties.getqSurveysPhotoPath();
    }

	public String getAvatarUploadPath() {
		return avatarUploadPath;
	}

	public String getBhpPhotoPath() {
		return bhpPhotoPath;
	}
	
	public String getToolsPhotoPath() {
		return toolsPhotoPath;
	}
	
	public String getMapPhotoPath() {
		return mapPhotoPath;
	}

	public String getqSurveysPhotoPath() {
		return qSurveysPhotoPath;
	}

	public ArrayList<String> listFiles(String path){
		ArrayList<String> files = new ArrayList<>();
		try {
			File[] filesList = (new DefaultResourceLoader()).getResource(path).getFile().listFiles();
			for(File file: filesList){
				files.add(file.getName());
			}
		} catch (IOException e) {
		} catch (NullPointerException np){
			files.clear();
		}
		
		return files;
	}
	
	public File[] listFilesAsObjects(String path){
		try {
			File[] filesList = (new DefaultResourceLoader()).getResource(path).getFile().listFiles();
			return filesList;
		} catch (IOException e) {
			return null;
		}
		
	}

	@RequestMapping(value = "/upload/prodcomp",  method = RequestMethod.POST)
	public String onUploadProdComp(MultipartFile file, RedirectAttributes redirectAttrs,
			Locale locale) {
		// is empty
		if (file.isEmpty()) {
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("action.choose.file", null, locale));
			return "redirect:/prodcomp/main";
		}
		// copy file
		try {
		    File convFile = File.createTempFile("prodcom", ".tmp");
		    convFile.deleteOnExit();
		    		//new File(file.getOriginalFilename());
		    //convFile.createNewFile();
		    FileOutputStream fos = new FileOutputStream(convFile); 
		    fos.write(file.getBytes());
		    fos.close(); 
			redirectAttrs.addFlashAttribute("file", convFile);
			return "redirect:/prodcomp/make";
		} catch (IOException ioex) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.io.exception", null, locale) + ": " + ioex.getMessage());
			return "redirect:/prodcomp/main";
		}
	}
	
	@RequestMapping(value = "/upload/prodcomp/plan",  method = RequestMethod.POST)
	public String onUploadProdComp(MultipartFile filePlan, @RequestParam String days, RedirectAttributes redirectAttrs,
			Locale locale) {
		
		int timeDays = 0;
		
		try{
			timeDays = Integer.parseInt(days);
		}
		catch (NumberFormatException ex) {
			redirectAttrs.addFlashAttribute("daysError", messageSource.getMessage("error.mustbeanumber", null, locale));
			return "redirect:/prodcomp/main";
		}
		
		// is empty
		if (filePlan.isEmpty()) {
			redirectAttrs.addFlashAttribute("warning", messageSource.getMessage("action.choose.file", null, locale));
			return "redirect:/prodcomp/main";
		}
		// copy file
		try {
			File convFile = File.createTempFile("prodcom", ".tmp");
			convFile.deleteOnExit();
			//new File(file.getOriginalFilename());
			//convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(filePlan.getBytes());
			fos.close(); 
			redirectAttrs.addFlashAttribute("file", convFile);
			redirectAttrs.addFlashAttribute("days", timeDays);
			return "redirect:/prodcomp/makeplan";
		} catch (IOException ioex) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.io.exception", null, locale) + ": " + ioex.getMessage());
			return "redirect:/prodcomp/main";
		}
	}
	
	@RequestMapping(value = "/upload/avatar/{id}",  method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String onUpload(@PathVariable("id") long id, MultipartFile file, RedirectAttributes redirectAttrs,
			Locale locale) {
		// is image?
		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.bad.file.type", null, locale));
			return "redirect:/users/edit/" + id;
		}
		// copy file
		try {
			String fileExtension = getFileExtension(file.getOriginalFilename());
			File tempFile = File.createTempFile("avatar_", fileExtension,
					(new DefaultResourceLoader()).getResource(avatarUploadPath).getFile());
			try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
				IOUtils.copy(in, out);
			}

			// update user info
			User user = userService.findById(id);
			user.setAvatarfilename(tempFile.getName());
			userService.saveOrUpdate(user);
			// say ok
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("upload.success", null, locale));
		} catch (IOException ioex) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.io.exception", null, locale));
			return "redirect:/users/edit/" + id;
		}
		return "redirect:/users/edit/" + id;
	}

	@RequestMapping(value = "/currentavatar")
	public void getCurrentUserAvatar(HttpServletResponse response) throws IOException {
		Resource picture;
		// get picture
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
				picture = avatarService.getAvatarResourceByUsername(auth.getName());
		}else{
			picture = avatarService.getAnonymousPicture();
		}
        // response
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picture.getFilename()));
		IOUtils.copy(picture.getInputStream(), response.getOutputStream());
	}
	
	@RequestMapping(value="/useravatar/{id}")
	public void getUserAvatar(HttpServletResponse response, @PathVariable Long id) throws IOException {
		// get picture
		Resource res = avatarService.getAvatarResourceById(id);
		// response
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(res.getFilename()));
		IOUtils.copy(res.getInputStream(), response.getOutputStream());
	}
	
	@RequestMapping(value="/bhptickets/getphoto/{name:.+}")
	public void getBhpPhoto(HttpServletResponse response, @PathVariable String name) throws IOException {
		Resource picture;
				picture = (new DefaultResourceLoader()).getResource(bhpPhotoPath + "/" + name);
				response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picture.getFilename()));
				IOUtils.copy(picture.getInputStream(), response.getOutputStream());
	}
	
	@RequestMapping(value="/tools/getpdf/{id}")
	public void getToolsPdf(HttpServletResponse response, @PathVariable int id) throws IOException, NotFoundException {
		ToolsProject project = toolsProjectService.findById(id);
		if (project == null) {
			throw new NotFoundException("Project not found");
		}
		Resource file;
		file = (new DefaultResourceLoader()).getResource("file://"+project.getPdfUrl());
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(file.getFilename()));
		IOUtils.copy(file.getInputStream(), response.getOutputStream());
	}
	
		
	/*
	@RequestMapping(value="/tools/getphoto-OK/{name:.+}",  method = RequestMethod.GET)
	public @ResponseBody byte[] getImage(@PathVariable String name) throws IOException {
	    InputStream in = (new DefaultResourceLoader()).getResource(toolsPhotoPath + "/" + name).getInputStream();
	    return IOUtils.toByteArray(in);
	}
	
	@RequestMapping(value="/tools/getphoto/{name:.+}",  method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable String name) throws IOException {
		Resource resource = (new DefaultResourceLoader()).getResource(toolsPhotoPath + "/" + name);
		ResponseEntity<byte[]> responseEntity;
        final HttpHeaders headers = new HttpHeaders();
        final InputStream in = resource.getInputStream();
        byte[] media = IOUtils.toByteArray(in);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
    return responseEntity;
	}
	*/

	@RequestMapping(value = "/upload/bhptickets/{id}",  method = RequestMethod.POST)
	@Secured({"ROLE_BHPMANAGER", "ROLE_ADMIN"})
	public String onBhpTicketPhotoUpload(@PathVariable("id") int id, MultipartFile file, RedirectAttributes redirectAttrs,
			Locale locale) {
					
		// is image?
		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.bad.file.type", null, locale));
			return "redirect:/bhptickets/edit/photos/" + id;
		}
		// copy file
		try {
			String fileExtension = getFileExtension(file.getOriginalFilename());
			File tempFile = File.createTempFile("bhp_" + id + "_" + dateHelper.formatYyyyMmDdHhMmSsNoSpecial(Calendar.getInstance().getTime())+ "_", fileExtension,
					(new DefaultResourceLoader()).getResource(bhpPhotoPath).getFile());
			try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
				IOUtils.copy(in, out);
			}

			// say ok
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("upload.success", null, locale));
		} catch (IOException ioex) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.io.exception", null, locale));
			return "redirect:/bhptickets/edit/photos/" + id;
		}
		
		return "redirect:/bhptickets/edit/photos/" + id;
	}
	
	@RequestMapping(value = "/upload/tools/{id}",  method = RequestMethod.POST)
	@Secured({"ROLE_TOOLSMANAGER", "ROLE_ADMIN"})
	public String onToolsPhotoUpload(@PathVariable("id") int id, MultipartFile file, RedirectAttributes redirectAttrs,
			Locale locale) throws IOException {
		
		ToolsProject project = toolsProjectService.findById(id);
		
		// is image?
		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.bad.file.type", null, locale));
			return "redirect:/tools/editproject/photos/" + id;
		}
		
		ArrayList<String> fileList = listFiles(getToolsPhotoPath());
		String tmpPath = getToolsPhotoPath().replaceAll("file:","").replaceAll("/", "\\\\");
		for (int i = fileList.size() - 1; i >= 0; i--) {
			if (fileList.get(i).startsWith("tool_" + id + "_")) {
				
				File hddFile = new File(tmpPath +"\\" + fileList.get(i));
				System.out.println(tmpPath +"\\" + fileList.get(i));
				hddFile.delete();
			}
		}
		
		BufferedImage img = null;
		BufferedImage scale = null;
		String fileExtension;
		try {
		    img = ImageIO.read(file.getInputStream());
		    scale = imageHelper.toBufferedImage(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
		    fileExtension = getFileExtension(file.getOriginalFilename());
			File tempFile = File.createTempFile("tool_" + id + "_", fileExtension,
					(new DefaultResourceLoader()).getResource(toolsPhotoPath).getFile());
			try (OutputStream out = new FileOutputStream(tempFile)) {
				ImageIO.write(scale, "png", tempFile);
			}
			
			
			project.setPhotoName(tempFile.getName());
			toolsProjectService.update(project);
			
			// say ok
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("upload.success", null, locale));
		
		} catch (IOException ioex) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.io.exception", null, locale));
			return "redirect:/tools/edit/photos/" + id;
		}
		
		return "redirect:/tools/editproject/" + id;
	}
	
	
	@RequestMapping(value = "/upload/qsurveys/{id}",  method = RequestMethod.POST)
	public String onQSurveysPhotoUpload(@PathVariable("id") int id, MultipartFile file, RedirectAttributes redirectAttrs,
			Locale locale) throws IOException {
		
		// is image?
		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.bad.file.type", null, locale));
			return "redirect:/qsurveys/photos/" + id;
		}
		
		
		BufferedImage img = null;
		BufferedImage scale = null;
		String fileExtension;
		try {
			File dir = null;
			try{
				System.out.println(qSurveysPhotoPath+"/"+id);
				dir = (new DefaultResourceLoader()).getResource(qSurveysPhotoPath+"/"+id).getFile();
				if(!dir.exists()){
					dir.mkdir();
				}
			}
			catch (IOException io){
				System.out.println("IOERROR");
				
			}
			img = ImageIO.read(file.getInputStream());
						
			double dimensionMultiplier = imageHelper.getDimensionMultiplier(
					img.getWidth(), img.getHeight(), 800, 600);
			
			scale = imageHelper.toBufferedImage(
					img.getScaledInstance(
							(int)(img.getWidth()*dimensionMultiplier), 
							(int)(img.getHeight()*dimensionMultiplier), 
							Image.SCALE_SMOOTH));
			
			fileExtension = getFileExtension(file.getOriginalFilename());
			File tempFile = File.createTempFile("qs_" + id + "_", fileExtension, dir);
			try (OutputStream out = new FileOutputStream(tempFile)) {
				ImageIO.write(scale, "png", tempFile);
			}
			
			
			// say ok
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("upload.success", null, locale));
			
		} catch (IOException ioex) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.io.exception", null, locale));
			return "redirect:/qsurveys/photos/" + id;
		}
		
		return "redirect:/qsurveys/photos/" + id;
	}
		
	/*
	 * HELPERS
	 */

	private boolean isImage(MultipartFile file) {
		return file.getContentType().startsWith("image");
	}

	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}

	// for size exception mapped from servlet in config
	@RequestMapping("/uploadError")
	public ModelAndView onUploadError(Locale locale) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("message", messageSource.getMessage("upload.file.too.big", null, locale));
		return modelAndView;
	}
}
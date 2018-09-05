package sbs.controller.upload;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.config.UploadProperties;
import sbs.helpers.ImageHelper;
import sbs.model.users.User;
import sbs.service.users.AvatarService;
import sbs.service.users.UserService;

@Controller
public class UploadController {
	
	@SuppressWarnings("unused")
	private final String uploadDir;
	private final String avatarUploadPath;
	private final String bhpPhotoPath;
	private final String toolsPhotoPath;
	
	@Autowired 
	UserService userService;
	@Autowired 
	AvatarService avatarService;
	@Autowired 
	MessageSource messageSource;
	@Autowired
	ImageHelper imageHelper;

    @Autowired
    public UploadController(UploadProperties uploadProperties) {
    	uploadDir = uploadProperties.getAvatarPath();
    	avatarUploadPath = uploadProperties.getAvatarPath();
    	bhpPhotoPath = uploadProperties.getBhpPhotoPath();
    	toolsPhotoPath = uploadProperties.getToolsPhotoPath();
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
	
	@Override
	public String toString() {
		return "UploadController [toolsPhotoPath=" + toolsPhotoPath + "]";
	}

	public ArrayList<String> listFiles(String path){
		ArrayList<String> files = new ArrayList<>();
		try {
			File[] filesList = (new DefaultResourceLoader()).getResource(path).getFile().listFiles();
			for(File file: filesList){
				files.add(file.getName());
			}
		} catch (IOException e) {
		}
		return files;
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
	
	@RequestMapping(value="/tools/getphoto/{name:.+}")
	public void getToolsPhoto(HttpServletResponse response, @PathVariable String name) throws IOException {
		Resource picture;
		picture = (new DefaultResourceLoader()).getResource(toolsPhotoPath + "/" + name);
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picture.getFilename()));
		IOUtils.copy(picture.getInputStream(), response.getOutputStream());
	}

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
			File tempFile = File.createTempFile("bhp_" + id + "_", fileExtension,
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
			Locale locale) {
		
		// is image?
		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.bad.file.type", null, locale));
			return "redirect:/tools/editproject/photos/" + id;
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
			
			// say ok
			redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("upload.success", null, locale));
		
		} catch (IOException ioex) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.io.exception", null, locale));
			return "redirect:/tools/edit/photos/" + id;
		}
		
		return "redirect:/tools/editproject/" + id;
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
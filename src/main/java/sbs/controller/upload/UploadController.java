package sbs.controller.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.config.UploadProperties;
import sbs.model.User;
import sbs.service.AvatarService;
import sbs.service.UserService;

@Controller
public class UploadController {
	private final Resource uploadDir;
	
	@Autowired 
	UserService userService;
	@Autowired 
	AvatarService avatarService;
	@Autowired 
	MessageSource messageSource;;

    @Autowired
    public UploadController(UploadProperties uploadProperties) {
    	uploadDir = uploadProperties.getAvatarPath();
    }

    
    
	@RequestMapping(value = "/upload/avatar/{id}",  method = RequestMethod.POST)
	@Secured("ROLE_USER")
	public String onUpload(@PathVariable("id") long id, MultipartFile file, RedirectAttributes redirectAttrs, Locale locale) throws IOException {
		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.bad.file.type", null, locale));
			return "redirect:/users/edit/"+id;
		}
		
		String fileExtension = getFileExtension(file.getOriginalFilename());
		File tempFile = File.createTempFile("avatar_", fileExtension, uploadDir.getFile());
		try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		
		//String path = new FileSystemResource(tempFile).getPath();
		User user = userService.findById(id);
		user.setAvatarPath(tempFile.getName());
		userService.saveOrUpdate(user);
		redirectAttrs.addFlashAttribute("ok");
		
		return "redirect:/users/edit/"+id;
	}

	@RequestMapping(value = "/useravatar")
	public void getUploadedPicture(HttpServletResponse response) throws IOException {
		Resource picturePath = avatarService.getCurrentPicturePath();
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
		IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
	}
	
	@RequestMapping(value="/useravatar/{id}")
	public void getUserAvatar(HttpServletResponse response, @PathVariable Long id) throws IOException {
		Resource res = avatarService.getAvatarResourceById(id);
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(res.getFilename()));
		IOUtils.copy(res.getInputStream(), response.getOutputStream());
	}

	
	// ---------------- helpers

	private boolean isImage(MultipartFile file) {
		return file.getContentType().startsWith("image");
	}

	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}

	
	// exceptions
   @ExceptionHandler(IOException.class)
	    public ModelAndView handleIOException(Locale locale, Exception ex) {
	   ex.getMessage();
	   ex.printStackTrace();
	        ModelAndView modelAndView = new ModelAndView("profileedit");
	        modelAndView.addObject("error", messageSource.getMessage("upload.io.exception", null, locale));
	        return modelAndView;
	    }
	
	// for size exception mapped from servlet in config
	@RequestMapping("uploadError")
	public ModelAndView onUploadError(Locale locale) {
		ModelAndView modelAndView = new ModelAndView("profileedit");
		modelAndView.addObject("error", messageSource.getMessage("upload.file.too.big", null, locale));
		return modelAndView;
	}
	
}
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
	private final Resource picturesDir;
	private final MessageSource messageSource;
	
	@Autowired UserService userService;
	@Autowired AvatarService avatarService;

    @Autowired
    public UploadController(UploadProperties uploadProperties,
                                   MessageSource messageSource) {
        picturesDir = uploadProperties.getPicturesUploadPath();
        this.messageSource = messageSource;
    }

	@RequestMapping(value = "/profile/uploadAvatar", params = {"upload"},  method = RequestMethod.POST)
	@Secured("ROLE_USER")
	public String onUpload(MultipartFile file, RedirectAttributes redirectAttrs, Locale locale) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", messageSource.getMessage("upload.bad.file.type", null, locale));
			return "redirect:/profile/edit";
		}		
		String path = copyFileToPictures(file);
		User user = userService.findByUsername(auth.getName());
		user.setAvatarPath(path);
		userService.saveOrUpdate(user);
		
		return "redirect:/profile/show";
	}

	@RequestMapping(value = "/useravatar")
	public void getUploadedPicture(HttpServletResponse response) throws IOException {
		Resource picturePath = avatarService.getCurrentPicturePath();
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
		IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
	}
	
	@RequestMapping(value="/useravatar/{username}")
	public void getUserAvatar(HttpServletResponse response, @PathVariable String username) throws IOException {
		Resource res = avatarService.getAvatarResourceByUsername(username);
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(res.getFilename()));
		IOUtils.copy(res.getInputStream(), response.getOutputStream());
	}

	// ---------------- helpers
	private String copyFileToPictures(MultipartFile file) throws IOException {
		String fileExtension = getFileExtension(file.getOriginalFilename());
		File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());
		try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return new FileSystemResource(tempFile).getPath();
	}

	private boolean isImage(MultipartFile file) {
		return file.getContentType().startsWith("image");
	}

	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}

	
	// exceptions
   @ExceptionHandler(IOException.class)
	    public ModelAndView handleIOException(Locale locale) {
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
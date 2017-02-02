package sbs.config.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlers {

	public static final String DEFAULT_ERROR_VIEW = "error";

	@ExceptionHandler(value = org.springframework.security.access.AccessDeniedException.class)
	public String accessDeniedHandler(HttpServletRequest req, Exception e) throws Exception {
		return "various/noaccess";
	}
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let the framework handle it
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}
		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName(DEFAULT_ERROR_VIEW);

		return mav;
	}
}

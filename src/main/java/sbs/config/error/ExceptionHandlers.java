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
	
	@ExceptionHandler(value = org.springframework.web.multipart.MultipartException.class)
	public String MultipartExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
		return "redirect:/uploadError";
	}
	
	@ExceptionHandler(value = org.springframework.web.socket.sockjs.SockJsTransportFailureException.class)
	public void MessageBrokerExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
		/* ignore */
	}
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let the framework handle it
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}
		e.printStackTrace();
		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName(DEFAULT_ERROR_VIEW);

		return mav;
	}
}



/*
@ControllerAdvice
public class ErrorController {

    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

}


<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Error page</title>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="css/main.css" th:href="@{/css/main.css}" />
    </head>
    <body th:with="httpStatus=${T(org.springframework.http.HttpStatus).valueOf(#response.status)}">
        <h1 th:text="|${httpStatus} - ${httpStatus.reasonPhrase}|">404</h1>
        <p th:utext="${errorMessage}">Error java.lang.NullPointerException</p>
        <a href="index.html" th:href="@{/index.html}">Back to Home Page</a>
    </body>
</html>





*/
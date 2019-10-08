package sbs.controller.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import sbs.singleton.LiveFeedSingleton;

@ControllerAdvice
public class GlobalControllerAdvice {

	@Autowired
	LiveFeedSingleton lvs;
	@Autowired
	Environment env;
	
    @ModelAttribute("live")
    public String getLiveFeedMessage() {
        return lvs.getMessage();
    }
    
    @ModelAttribute("build")
    public String getBuildVersion() {
        return env.getRequiredProperty("build.version");
    }
}
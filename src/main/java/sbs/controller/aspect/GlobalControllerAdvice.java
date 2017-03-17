package sbs.controller.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import sbs.singleton.LiveFeedSingleton;

@ControllerAdvice
public class GlobalControllerAdvice {

	@Autowired
	LiveFeedSingleton lvs;
	
    @ModelAttribute("live")
    public String getLiveFeedMessage() {
        return lvs.getMessage();
    }
    
}
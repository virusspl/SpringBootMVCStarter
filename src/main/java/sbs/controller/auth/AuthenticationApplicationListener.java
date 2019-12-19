package sbs.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import sbs.controller.auth.HrUserInfoSessionHolder;
import sbs.model.hr.HrUserInfo;
import sbs.model.users.User;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.users.UserService;

@Component
public class AuthenticationApplicationListener {
	AuthenticationTrustResolverImpl  rsl;
	
	@Autowired
	HrUserInfoSessionHolder userHolder;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	JdbcAdrOptimaService optimaService;
	@Autowired
	UserService userService;
	
	@EventListener
	public void handleInteractiveAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {

		// https://stackoverflow.com/questions/11575860/remember-me-and-authentication-success-handler
		// get RCP info
		User current = userService.getAuthenticatedUser();
		if (current != null && current.getRcpNumber().length() > 0) {
			HrUserInfo hrInfo = optimaService.findCurrentlyEmployedByCardNo(current.getRcpNumber());
			if (hrInfo != null) {
				userHolder.setInfo(hrInfo);
			}
		}
		
		if (RememberMeAuthenticationToken.class.isAssignableFrom(event.getAuthentication().getClass())) {
			
		}
	}
}

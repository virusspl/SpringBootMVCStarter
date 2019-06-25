package sbs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@Order(1)
public class WebSecurityTerminalConfig extends WebSecurityConfigurerAdapter {

	public WebSecurityTerminalConfig() {
        super();
    }
	
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    	http
	    	.formLogin()
	    	.loginPage("/tlogin").and()
	    	.antMatcher("/terminal/**").authorizeRequests()
	    	
	    	.antMatchers("/terminal/auth")
	    	.hasAnyRole("ADMIN","TERMINAL")
	    	
	    	.antMatchers("/terminal/inventory/**")
	  		.hasAnyRole("ADMIN","INVENTORYTERMINAL")
	    	
	  		.antMatchers("/terminal/shipments/**")
	  		.hasAnyRole("ADMIN","SHIPMENTSTERMINAL","SHIPMENTSMANAGER")
	  		
	    	;			
 
	    }
}

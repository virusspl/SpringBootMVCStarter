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
	    	
	    	.antMatchers("/terminal/inventory/**")
	  		.hasAnyRole("ADMIN","INVENTORYTERMINAL")
	    	
	  		.antMatchers("/terminal/shipments/**")
	  		.hasAnyRole("ADMIN","SHIPMENTSMANAGER","SHIPMENTSTERMINAL", "TERMINAL")
	  		
	    	;			
	    	
	    	/*.authorizeRequests()
	    	.anyRequest()
	  		.hasAnyRole("ADMIN","TERMINAL")
	  		.and()
	  		.formLogin()
	  		.loginPage("/terminallogin")
	        .failureUrl("/terminallogin?error=loginError")
	        .defaultSuccessUrl("/terminal")
	        .and()
	        .logout()
	        .logoutUrl("/admin_logout")
	        .logoutSuccessUrl("/protectedLinks")
	        .deleteCookies("JSESSIONID")
	        .and()
	        .exceptionHandling()
	        .accessDeniedPage("/403")
	        .and()
	        .csrf().disable(); 
	        */ 
	    }
}

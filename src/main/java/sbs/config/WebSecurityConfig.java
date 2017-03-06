package sbs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;



@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Autowired
	PersistentTokenRepository persistentTokenRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password")
		.successHandler(savedRequestAwareAuthenticationSuccessHandler())
		.and().logout().logoutSuccessUrl("/logout")
		.and().exceptionHandling().accessDeniedPage("/noaccess")
		.and().authorizeRequests()
		.antMatchers("/", "/init",
				"/bootstrap/**",
				"/selectize/**",
				"/css/**",
				"/images/**",
				"/js/**",
				"/error", 
				"/login",
				"/logout",
				"/contact/**",
				"/users/showcurrent",
				"/nameplates/list",
				"/geolook/**"
				)
		.permitAll()
		.antMatchers("/qualitysurveys/create")
		.hasAnyRole("ADMIN","QUALITYUSER")
		.antMatchers(
				"/admin", 
				"/users/**"
				)
		.hasRole("ADMIN")
		.anyRequest().authenticated().and()
		.csrf()
		.and()
		.rememberMe().tokenRepository(persistentTokenRepository)
		.tokenValiditySeconds(1209600);
	}
	
	
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
		SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
		auth.setTargetUrlParameter("targetUrl");
		return auth;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

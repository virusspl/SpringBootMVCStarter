package sbs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@Order(2)
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
		.successHandler(savedRequestAwareAuthenticationSuccessHandler()).and()
		.logout().logoutSuccessUrl("/logout").and()
		.exceptionHandling().accessDeniedPage("/noaccess").and()
		.authorizeRequests()
		.antMatchers(
				"/users/showcurrent",
				"/wakesurvey/dosurvey/**",
				"/bhptickets/show/**",
				"/bhptickets/getphoto/**"
				)
		.permitAll()
		.antMatchers(
				"/bhptickets/edit/**", 
				"/bhptickets/create/**",
				"/bhptickets/sendemails/**",
				"/wakesurvey/**",
				"/upload/bhptickets/**" )
		.hasAnyRole(
				"ADMIN",
				"BHPMANAGER"
				)
		.antMatchers(
				"/bhptickets/**")
		.hasAnyRole(
				"ADMIN",
				"BHPMANAGER", 
				"BHPSUPERVISOR", 
				"BHPTICKETSUSER"
				)	
		.antMatchers(
				"/qsurveys/templates/**", 
				"/qsurveys/questions/**")
		.hasAnyRole(
				"ADMIN",
				"QSURVEYMANAGER"
				)
		.antMatchers(
				"/qsurveys/**")
		.hasAnyRole(
				"ADMIN",
				"QSURVEYMANAGER", 
				"QSURVEYUSER"
				)
		.antMatchers(
				"/buyorders/**")
		.hasAnyRole(
				"ADMIN", 
				"BUYORDMANAGER"
				)
		.antMatchers(
				"/movements/**")
		.hasAnyRole(
				"ADMIN", 
				"MOVEMENTSUSER",
				"MOVEMENTSSUPERUSER"
				)
		.antMatchers(
				"/proprog/sendemails/**")
		.hasAnyRole(
				"ADMIN", 
				"PROPROGSUPERVISOR"
				)
		.antMatchers(
				"/utr/**")
		.hasAnyRole(
				"ADMIN", 
				"UTR_NORMALUSER"
				)
		.antMatchers(
				"/dirrcpship/**")
		.hasAnyRole(
				"ADMIN", 
				"RCPMANAGER"
				)
		.antMatchers(
				"/prodtosale/**",
				"/ordtools/**")
		.hasAnyRole(
				"ADMIN", 
				"PRODTOSALEUSER"
				)
		.antMatchers(
				"/tools/editproject/**", 
				"/tools/createproject/**"
				)
		.hasAnyRole(
				"ADMIN", 
				"TOOLSMANAGER"
				)
		.antMatchers(
				"/tools/**")
		.hasAnyRole(
				"ADMIN", 
				"TOOLSMANAGER",
				"TOOLSPRODMANAGER",
				"TOOLSNORMALUSER",
				"TOOLSMAILINGUSER"
				)
		.antMatchers(
				"/timer/**"
				)
		.hasAnyRole(
				"ADMIN", 
				"TIMERUSER"
				)
		.antMatchers(
				"/consumption/**"
				)
		.hasAnyRole(
				"ADMIN", 
				"CONSUMPTIONUSER"
				)
		.antMatchers(
				"/phones/createentry/**",
				"/phones/editentry/**",
				"/phones/createcategory/**",
				"/phones/editcategory/**"
				)
		.hasAnyRole(
				"ADMIN", 
				"LIGHT_ADMIN",
				"PHONESMANAGER"
				)
		.antMatchers(
				"/prodcomp/**"
				)
		.hasAnyRole(
				"ADMIN", 
				"COMPONENTSUSER"
				)
		.antMatchers(
				"/qcheck/**"
				)
		.hasAnyRole(
				"ADMIN", 
				"QCHECKMANAGER",
				"QCHECKUSER"
				)
		.antMatchers(
				"/admin", 
				"/users/**",
				"/system/**",
				"/inventory/createinventory/**",
				"/inventory/editinventory/**",
				"/inventory/managecollumns/**"
				)		
		.hasRole("ADMIN")
		 // ** old modules to del **
		.antMatchers(
				"/qualitysurveys/**")
		.hasAnyRole(
				"ADMIN"
				)
		.antMatchers(
				"/hrua/**")
		.hasAnyRole(
				"ADMIN"
				)		
		 // ^^ old modules to del ^^
		.antMatchers("/**")
		.permitAll()
		.anyRequest().authenticated().and()
		.csrf().and()
		.rememberMe().tokenRepository(persistentTokenRepository)
		.tokenValiditySeconds(1209600).and()
		.sessionManagement()
		.maximumSessions(1)
		.expiredUrl("/expired")
		.sessionRegistry(sessionRegistry());
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
	


    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }
	
}

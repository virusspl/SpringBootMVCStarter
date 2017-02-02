package sbs.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import sbs.config.date.SimpleDateTimeFormatter;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
public class WebConfig extends WebMvcConfigurerAdapter {
	

	// ----------> date formatter <----------
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new SimpleDateTimeFormatter());
	}

	// ----------> i18n <----------
	@Bean
	public LocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	// ----------> swagger 2 <----------
	@Bean
	public Docket userApi() {
	  return new Docket(DocumentationType.SWAGGER_2)
	    .select()
	      .paths(path -> path.startsWith("/api/"))
	        .build();
	}
	
	
}
package sbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import sbs.config.UploadProperties;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({UploadProperties.class})
public class SpringBootMvcStarterApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootMvcStarterApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootMvcStarterApplication.class, args);
	}
}

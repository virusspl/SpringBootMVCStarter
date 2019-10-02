package sbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import sbs.scheduling.DocViewCacheScheduler;
import sbs.scheduling.StandardCostsScheduler;

@Configuration
@EnableScheduling
public class SchedulingConfig {
	
	@Bean
	public StandardCostsScheduler stdCostATW(){
		return new StandardCostsScheduler();
	}
	
	@Bean
	public DocViewCacheScheduler docViewCache(){
		return new DocViewCacheScheduler();
	}
	
}

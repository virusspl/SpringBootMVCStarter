package sbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import sbs.scheduling.AverageDeliveryDaysScheduler;
import sbs.scheduling.DocViewCacheScheduler;
import sbs.scheduling.RmgtScheduler;
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
	
	@Bean
	public AverageDeliveryDaysScheduler deliveryDaysScheduler(){
		return new AverageDeliveryDaysScheduler();
	}
	
	//@Bean
	public RmgtScheduler scheduleRMGT(){
		return new RmgtScheduler();
	}
	
}

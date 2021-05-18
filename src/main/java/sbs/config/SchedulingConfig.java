package sbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import sbs.scheduling.AverageDeliveryDaysScheduler;
import sbs.scheduling.DocViewCacheScheduler;
import sbs.scheduling.GarbageCollectorScheduler;
import sbs.scheduling.PaymentTermsScheduler;
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
	
	@Bean
	public GarbageCollectorScheduler garbageCollectorScheduler(){
		return new GarbageCollectorScheduler();
	}
	
	@Bean
	public PaymentTermsScheduler paymentTermsScheduler(){
		return new PaymentTermsScheduler();
	}
	
	
	/*
	@Bean
	public RmgtScheduler scheduleRMGT(){
		return new RmgtScheduler();
	}
	*/
	
}

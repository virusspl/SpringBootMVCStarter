package sbs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/sbs-websocket").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
	     config.setApplicationDestinationPrefixes("/app");
		ThreadPoolTaskScheduler pingScheduler = new ThreadPoolTaskScheduler();
		pingScheduler.initialize();
		config.enableSimpleBroker("/topic").setHeartbeatValue(new long[] { 20000, 0 })
				.setTaskScheduler(pingScheduler);
	}
}
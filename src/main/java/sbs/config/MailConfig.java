package sbs.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration 
public class MailConfig {
/*
    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private Integer port;
*/	
	
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("192.168.1.11");
		javaMailSender.setPort(25);
		javaMailSender.setJavaMailProperties(getMailProperties());
		return javaMailSender;
	}
	
	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "false");
		properties.setProperty("mail.smtp.debug", "true");
		properties.setProperty("mail.mime.charset", "utf8");
		
		return properties;
	}
    @Bean
    @Profile("gmail")
    public JavaMailSender GjavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        		
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(25);
        javaMailSender.setUsername("username");
        javaMailSender.setPassword("pass");
        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    @Profile("gmail")
    private Properties GgetMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.debug", "true");

        return properties;
    }
}
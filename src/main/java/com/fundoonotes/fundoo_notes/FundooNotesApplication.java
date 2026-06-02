package com.fundoonotes.fundoo_notes;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FundooNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooNotesApplication.class, args);

	}

	@org.springframework.beans.factory.annotation.Autowired
	private org.springframework.core.env.Environment env;

	@jakarta.annotation.PostConstruct
	public void debugRabbitMQ() {
		System.out.println("========== ENVIRONMENT DIAGNOSTICS ==========");
		
		// MySQL
		System.out.println("--- MySQL ---");
		System.out.println("spring.datasource.url: " + env.getProperty("spring.datasource.url"));
		System.out.println("spring.datasource.username: " + env.getProperty("spring.datasource.username"));
		System.out.println("spring.datasource.password (obfuscated): " + getObfuscated(env.getProperty("spring.datasource.password")));
		
		// Redis
		System.out.println("--- Redis ---");
		System.out.println("spring.data.redis.host: " + env.getProperty("spring.data.redis.host"));
		System.out.println("spring.data.redis.port: " + env.getProperty("spring.data.redis.port"));
		System.out.println("spring.data.redis.password (obfuscated): " + getObfuscated(env.getProperty("spring.data.redis.password")));
		
		// RabbitMQ
		System.out.println("--- RabbitMQ ---");
		System.out.println("spring.rabbitmq.host: " + env.getProperty("spring.rabbitmq.host"));
		System.out.println("spring.rabbitmq.port: " + env.getProperty("spring.rabbitmq.port"));
		System.out.println("spring.rabbitmq.username: " + env.getProperty("spring.rabbitmq.username"));
		System.out.println("spring.rabbitmq.virtual-host: " + env.getProperty("spring.rabbitmq.virtual-host"));
		System.out.println("spring.rabbitmq.password (obfuscated): " + getObfuscated(env.getProperty("spring.rabbitmq.password")));
		
		System.out.println("=============================================");
	}

	private String getObfuscated(String val) {
		if (val == null) return "null";
		int len = val.length();
		if (len > 6) {
			return val.substring(0, 3) + "..." + val.substring(len - 3) + " (Length: " + len + ")";
		}
		return "*** (Length: " + len + ")";
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
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

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public org.springframework.boot.CommandLineRunner debugRabbitMQ(org.springframework.core.env.Environment env) {
		return args -> {
			System.out.println("========== RABBITMQ CONFIG DIAGNOSTICS ==========");
			System.out.println("spring.rabbitmq.host: " + env.getProperty("spring.rabbitmq.host"));
			System.out.println("spring.rabbitmq.port: " + env.getProperty("spring.rabbitmq.port"));
			System.out.println("spring.rabbitmq.username: " + env.getProperty("spring.rabbitmq.username"));
			System.out.println("spring.rabbitmq.virtual-host: " + env.getProperty("spring.rabbitmq.virtual-host"));
			System.out.println("RABBITMQ_VIRTUAL_HOST env: " + env.getProperty("RABBITMQ_VIRTUAL_HOST"));
			System.out.println("=================================================");
		};
	}
}
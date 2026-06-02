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
}
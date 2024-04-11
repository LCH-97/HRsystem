package com.example.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.web.client.RestTemplate;

@EnableBatchProcessing	// 배치기능을 사용하기 위함
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class BatchTestApplication {


//	@Bean
//	public RestTemplate restTemplate() {　// Rest api를 사용하기위함
//		return new RestTemplate();
//	}

	public static void main(String[] args) {
		SpringApplication.run(BatchTestApplication.class, args);
	}

}
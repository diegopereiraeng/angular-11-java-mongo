package com.bezkoder.spring.data.mongodb;

import com.bezkoder.spring.data.mongodb.model.providers.Github;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


import io.harness.cf.client.api.CfClient;
import io.harness.cf.client.api.Config;
import io.harness.cf.client.dto.Target;

@SpringBootApplication
public class SpringBootDataMongodbApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringBootDataMongodbApplication.class);

	@Value("${ff.apiKey}")
	String apiKey;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}


	/*@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Github quote = restTemplate.getForObject(
					"https://quoters.apps.pcfone.io/api/random", Github.class);
			log.info(quote.toString());
		};
	}*/

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			CfClient cfClient = new CfClient(apiKey, Config.builder().build());

		};
	}


	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataMongodbApplication.class, args);
	}

}

package io.wegetit.documently;

import io.wegetit.sau.logger.EnableHttpRequestLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@SpringBootApplication
@EnableHttpRequestLogger
public class DocumentlyMicroservice {

	public static void main(String[] args) {
		SpringApplication.run(DocumentlyMicroservice.class, args);
	}
}

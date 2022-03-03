package io.wegetit.documently;

import io.wegetit.documently.exception.EntityNotFoundException;
import io.wegetit.sau.core.errorhandler.EnableErrorHandler;
import io.wegetit.sau.core.errorhandler.ErrorHandlerService;
import io.wegetit.sau.core.errorhandler.ExceptionType;
import io.wegetit.sau.core.log.http.EnableHttpRequestLogger;
import io.wegetit.sau.core.log.http.HttpRequestFilter;
import io.wegetit.sau.mongo.EnableMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;

@PropertySource("classpath:application.properties")
@SpringBootApplication
@EnableErrorHandler
@EnableHttpRequestLogger
@EnableMongo
@Configuration
public class DocumentlyRestServiceApplication {

	@Autowired
	private ErrorHandlerService errorHandlerService;

	@PostConstruct
	private void init() {
		errorHandlerService.registerType(ExceptionType.builder().errorClass(EntityNotFoundException.class).status(
				HttpStatus.NOT_FOUND).logTrace(false).build());
	}

	@Bean
	public HttpRequestFilter httpRequestFilter() {
		return new HttpRequestFilter() {
			@Override
			public boolean logUrl(String url, long time, int status) {
				return url.matches(".*/api.*");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DocumentlyRestServiceApplication.class, args);
	}
}

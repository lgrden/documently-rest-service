package io.wegetit.documently.config;

import io.wegetit.sau.logger.HttpRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpRequestFilterConfiguration {

	@Bean
	public HttpRequestFilter httpRequestFilter() {
		return new HttpRequestFilter() {
			@Override
			public boolean logUrl(String url, long time, int status) {
				return url.matches(".*/api.*");
			}
		};
	}
}

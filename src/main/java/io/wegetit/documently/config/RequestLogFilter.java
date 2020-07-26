package io.wegetit.documently.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Configuration
@Order(0)
public class RequestLogFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		long start = System.currentTimeMillis();
		chain.doFilter(httpRequest, httpResponse);
		long end = System.currentTimeMillis();
		log.info(format("%s in %d ms. Status %s.", buildUrlInfo(httpRequest), (end-start), httpResponse.getStatus()));
	}
	
	private static String buildUrlInfo(HttpServletRequest httpRequest, String... headers) {
		StringBuilder fullUrl = new StringBuilder();
		fullUrl.append(httpRequest.getMethod());
		fullUrl.append(" ");
		fullUrl.append(httpRequest.getRequestURL());
		if (httpRequest.getQueryString() != null) {
			fullUrl.append('?').append(httpRequest.getQueryString());
		}
		if (!ArrayUtils.isEmpty(headers)) {
			List<String> list = Arrays.asList(headers);
			Enumeration<String> headerNames = httpRequest.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				if (list.contains(headerName)) {
					fullUrl.append("\n ").append(headerName).append(" : ").append(Collections.list(httpRequest.getHeaders(headerName)));
				}
			}
		}

		return fullUrl.toString();
	}
}

package io.wegetit.documently.config;

import io.wegetit.documently.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

	@Getter
	@AllArgsConstructor
	@ToString
	@Builder
	public static class ErrorResponse {
		private long timestamp;
		private int status;
		private String error;
		private String code;
		private String message;
		private String path;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class ExceptionType {
		private Class<? extends Throwable> errorClass;
		private HttpStatus status;
		private boolean logException;

		public String getCode() {
			String code = errorClass.getSimpleName();
			code = code.replaceAll("()([A-Z])", "$1_$2");
			code = StringUtils.substringAfter(code, "_");
			return code.toUpperCase();
		}
	}

	private static final ExceptionType DEFAULT = ExceptionType.builder().errorClass(Exception.class).status(HttpStatus.INTERNAL_SERVER_ERROR).logException(true).build();

	private final Map<Class<? extends Throwable>, ExceptionType> types = new HashMap<>();

	public ExceptionHandlerAdvice() {
		registerType(ExceptionType.builder().errorClass(EntityNotFoundException.class).status(
				HttpStatus.NOT_FOUND).logException(false).build());
	}

	public void registerType(ExceptionType type) {
		types.put(type.getErrorClass(), type);
	}

	private Optional<ExceptionType> findExceptionType(Throwable e) {
		return Optional.ofNullable(types.get(e.getClass()));
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Throwable e, HttpServletRequest request) {
		ExceptionType type = findExceptionType(e).orElse(DEFAULT);
		if (type.isLogException()) {
			log.error(type.getStatus().getReasonPhrase() + " : " + e.getMessage(), e);
		} else {
			log.error(type.getStatus().getReasonPhrase() + " : " + e.getMessage());
		}
		return new ResponseEntity<>(ErrorResponse.builder()
				.timestamp(System.currentTimeMillis())
				.status(type.getStatus().value())
				.error(type.getStatus().getReasonPhrase())
				.code(type.getCode())
				.message(e.getMessage())
				.path(request.getContextPath() + request.getServletPath())
				.build(), type.getStatus());
	}
}
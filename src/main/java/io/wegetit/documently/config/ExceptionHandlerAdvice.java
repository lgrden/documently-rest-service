package io.wegetit.documently.config;

import io.wegetit.documently.exception.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

	private enum Level {
		WARNING,
		INFO,
		ERROR
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class ErrorResponse {
		private long timestamp;
		private int status;
		private String error;
		private String code;
		private String message;
		private String path;
		private Object details;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	private static class ExceptionTypeResponse {
		private HttpStatus status;
		private String type;
		private Throwable throwable;
		private String message;
		private String code;
		private Level logLevel;
		private boolean logException;
		private Object details;
	}

	private enum ExceptionType {
		CONSTRAINT_VIOLATION(ConstraintViolationException.class, HttpStatus.BAD_REQUEST, Level.INFO, false) {
			@Override
			public Object getDetails(Throwable t) {
				ConstraintViolationException ce = (ConstraintViolationException)t;
				final Map<String, String> details = new HashMap<>();
				ce.getConstraintViolations().stream().forEach(p -> details.put(asProperty(p.getPropertyPath()),  p.getMessage()));
				return details;
			}
		},
		ENTITY_NOT_FOUND(EntityNotFoundException.class, HttpStatus.NOT_FOUND, Level.WARNING, false),
		VALIDATION(ValidationException.class, HttpStatus.BAD_REQUEST, Level.WARNING, false),
		// GLOBAL EXCEPTIONS
		METHOD_NOT_SUPPORTED(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED, null, false),
		HTTP_MEDIA_TYPE_NOT_SUPPORTED(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE, Level.WARNING, false),
		HTTP_MESSAGE_NOT_READABLE(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST, Level.WARNING, false){
			@Override
			public String getMessage(Throwable t) {
				return "Required request body is missing.";
			}
		};

		private Class<? extends Throwable> exception;
		private HttpStatus status;
		private Level logLevel;
		private boolean logException;

		<E extends Throwable> ExceptionType(Class<E> e, HttpStatus status, Level logLevel, boolean logException) {
			this.exception = e;
			this.status = status;
			this.logLevel = logLevel;
			this.logException = logException;
		}

		public static ExceptionTypeResponse getExceptionType(Throwable t) {
			if (t == null) {
				return null;
			}
			for (ExceptionType type : values()) {
				if (type.exception.isInstance(t)) {
					return ExceptionTypeResponse.builder()
						.throwable(t)
						.type(type.name())
						.status(type.status)
						.message(type.getMessage(t))
						.code(type.getErrorCode(t))
						.logLevel(type.getLogLevel())
						.logException(type.logException)
						.details(type.getDetails(t))
						.build();
				}
			}
			return getExceptionType(t.getCause());
		}

		public String getMessage(Throwable t) {
			return t.getMessage();
		}

		public Level getLogLevel() {
			return logLevel;
		}

		public String getErrorCode(Throwable t) {
			return name();
		}

		public Object getDetails(Throwable t) {
			return null;
		}
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Throwable e, HttpServletRequest request) {
		ExceptionTypeResponse etr = ExceptionType.getExceptionType(e);
		if (etr != null) {
			log(etr.getStatus(), etr.getThrowable(), etr.getMessage(), etr.getLogLevel(), etr.isLogException());
			return buildResponse(request, etr.getStatus(), etr.getThrowable(), etr.getMessage(), etr.getCode(), etr.getLogLevel(), etr.getDetails(), etr.isLogException());
		}
		log(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage(), Level.ERROR, true);
		return buildResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage(), null, Level.ERROR, null, true);
	}

	private void log(HttpStatus status, Throwable t, String message, Level logLevel, boolean logException) {
		if (logLevel == null) {
			return;
		}
		switch (logLevel) {
			case INFO: {
				if (logException) {
					log.info(status.getReasonPhrase() + " : " + message, t);
				} else {
					log.info(status.getReasonPhrase() + " : " + message);
				}
				break;
			}
			case WARNING: {
				if (logException) {
					log.warn(status.getReasonPhrase() + " : " + message, t);
				} else {
					log.warn(status.getReasonPhrase() + " : " + message);
				}
				break;
			}
			case ERROR: {
				if (logException) {
					log.error(status.getReasonPhrase() + " : " + message, t);
				} else {
					log.error(status.getReasonPhrase() + " : " + message);
				}
				break;
			}
		}
	}

	private ResponseEntity<ErrorResponse> buildResponse(HttpServletRequest request, HttpStatus status, Throwable t, String message, String errorCode, Level logLevel, Object details, boolean logException) {
		return new ResponseEntity<>(ErrorResponse.builder()
			.timestamp(System.currentTimeMillis())
			.status(status.value())
			.error(status.getReasonPhrase())
			.code(errorCode)
			.message(message)
			.path(request.getContextPath() + request.getServletPath())
			.details(details)
			.build(), status);
	}
	
	private static String asProperty(Path p) {
		return StringUtils.substringAfter(p.toString(), ".");
	}
}
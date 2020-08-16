package io.wegetit.documently;

import io.wegetit.documently.config.ExceptionHandlerAdvice;
import io.wegetit.documently.config.ExceptionHandlerAdvice.ErrorResponse;
import io.wegetit.documently.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerAdviceTest {

    @Mock
    private HttpServletRequest request;

    private ByteArrayOutputStream out;
    private ExceptionHandlerAdvice handler;

    @BeforeEach
    void setUp() {
        handler = new ExceptionHandlerAdvice();
        when(request.getContextPath()).thenReturn("/aaa");
        when(request.getServletPath()).thenReturn("/bbb");
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @Test
    void entityNotFound() {
        ResponseEntity<ErrorResponse> response = handler.exceptionHandler(new EntityNotFoundException("Entity not found"), request);
        assertErrorResponse(response, HttpStatus.NOT_FOUND, "ENTITY_NOT_FOUND_EXCEPTION", "Entity not found", true);
    }

    private void assertErrorResponse(ResponseEntity<ErrorResponse> response, HttpStatus status, String code, String message, boolean hasLog) {
        assertEquals(status, response.getStatusCode());
        assertEquals(status.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(status.value(), response.getBody().getStatus());
        assertEquals(status.getReasonPhrase(), response.getBody().getError());
        assertEquals(code, response.getBody().getCode());
        assertEquals(message, response.getBody().getMessage());
        assertEquals("/aaa/bbb", response.getBody().getPath());
        if (hasLog) {
            assertTrue(out.toString().trim().endsWith(" : " + response.getBody().getMessage()));
        } else {
            assertEquals("", out.toString());
        }
    }
}

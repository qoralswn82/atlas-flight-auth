package org.atlas.flight.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.atlas.flight.core.ApiResponse;
import org.atlas.flight.core.exception.ApiException;
import org.atlas.flight.core.message.ResponseCodeInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = {ApiException.class})
	protected ResponseEntity<?> handleApiException(ApiException ex) {
		ResponseCodeInterface code = ex.getResponseCode();
		ApiResponse<Object> response = ApiResponse.error(code);
		return ResponseEntity.ok(response);
		
	}
}

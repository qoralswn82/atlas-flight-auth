package org.atlas.flight.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.atlas.flight.core.ApiResponse;
import org.atlas.flight.core.exception.ApiException;
import org.atlas.flight.core.message.ResponseCodeGeneral;
import org.atlas.flight.core.message.ResponseCodeInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = {ApiException.class})
	protected ResponseEntity<?> handleApiException(ApiException ex) {
		ResponseCodeInterface code = ex.getResponseCode();
		ApiResponse<Object> response = ApiResponse.error(code);
		return ResponseEntity.ok(response);
		
	}

	/**
	 * {@code @Valid} 검증 실패 시 ATF400 및 필드별 메시지 반환 (401과 구분).
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		log.debug("Validation failed: {}", ex.getMessage());
		var bad = ResponseCodeGeneral.BAD_REQUEST.getResponseCode();

		String firstMessage = ex.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(FieldError::getDefaultMessage)
				.orElse(bad.getResultMessage());

		String detail = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(Collectors.joining("; "));

		ApiResponse<Object> response = ApiResponse.builder()
				.resultCode(bad.getResultCode())
				.resultMessage(firstMessage)
				.resultDetailMessage(detail.isEmpty() ? bad.getResultDetailMessage() : detail)
				.data(null)
				.build();

		return ResponseEntity.ok(response);
	}
}

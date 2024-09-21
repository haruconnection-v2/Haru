package com.backend.global.common.exception;

import org.junit.jupiter.api.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backend.global.common.response.ErrorCode;
import com.backend.global.common.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(HttpServletRequest req, BusinessException e) {
		log.error("Request:" + req.getRequestURI() + " BusinessException: " + e.getErrorCode().getStatus()+ " / " + e.getMessage());
		return createErrorResponse(e.getErrorCode());
	}

	private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {
		return new ResponseEntity<>(
			ErrorResponse.of(errorCode), errorCode.getStatus());
	}
}

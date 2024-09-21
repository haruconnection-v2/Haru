package com.backend.global.common.exception;

import com.backend.global.common.response.ErrorCode;

public class BadRequestException extends BusinessException {
	public BadRequestException(ErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode);
	}
	public BadRequestException() {
		super(ErrorCode.BAD_REQUEST);
	}
}

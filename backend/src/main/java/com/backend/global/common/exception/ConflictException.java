package com.backend.global.common.exception;

import com.backend.global.common.response.ErrorCode;

public class ConflictException extends BusinessException {
	public ConflictException(ErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode);
	}
}

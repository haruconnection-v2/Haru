package com.backend.global.common.exception;

import com.backend.global.common.response.ErrorCode;

public class NotFoundException extends BusinessException {
	public NotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode);
	}
}

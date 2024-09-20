package com.backend.global.common.response;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private final String code;
	private final String message;
	public ErrorResponse(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}
	public static ErrorResponse of(final ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}
}

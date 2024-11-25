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
	public ErrorResponse(ErrorCode errorCode, String message) {
		this.code = errorCode.getCode();
		this.message = message;
	}

	public static ErrorResponse of(final ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}

	public static ErrorResponse of(final ErrorCode errorCode, final String message) {
		return new ErrorResponse(errorCode, message);
	}
}

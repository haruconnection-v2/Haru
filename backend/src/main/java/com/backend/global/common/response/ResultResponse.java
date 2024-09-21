package com.backend.global.common.response;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public class ResultResponse<T> {
	private final String code;
	private final String message;
	private T data;

	@JsonCreator
	public ResultResponse(final ResultCode resultCode) {
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}
	@JsonCreator
	public ResultResponse(final ResultCode resultCode, T data) {
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
		this.data = data;
	}

	public static <T> ResultResponse<T> of(final ResultCode resultCode, final T data) {
		return new ResultResponse<>(resultCode, data);
	}
}

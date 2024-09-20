package com.backend.global.common.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
	// Common
	// Auth
	// Member
	// Diary
	FIND_DIARY_SUCCESS(HttpStatus.OK, "D000", "일기 조회 성공"),
	CREATE_DIARY_SUCCESS(HttpStatus.CREATED, "D001", "일기 생성 성공"),
	UPDATE_DIARY_SUCCESS(HttpStatus.OK, "D002", "일기 변경 성공"),
	FIND_DIARY_SNS_SUCCESS(HttpStatus.OK, "D003", "일기 SNS 링크 조회 성공"),
	// Calendar
	// TextBox

	// Image
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}

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
	SIGN_UP_SUCCESS(HttpStatus.CREATED, "M001", "회원가입 성공"),
	LOGIN_SUCCESS(HttpStatus.OK, "M000", "로그인 성공"),
	LOGOUT_SUCCESS(HttpStatus.OK, "M100", "로그아웃 성공"),
	// Diary
	FIND_DIARY_SUCCESS(HttpStatus.OK, "D000", "일기 조회 성공"),
	CREATE_DIARY_SUCCESS(HttpStatus.CREATED, "D001", "일기 생성 성공"),
	UPDATE_DIARY_SUCCESS(HttpStatus.OK, "D002", "일기 변경 성공"),
	FIND_DIARY_SNS_SUCCESS(HttpStatus.OK, "D003", "일기 SNS 링크 조회 성공"),
	// Sticker
	FIND_ST_STICKER_SUCCESS(HttpStatus.OK, "S000", "기본 스티커 조회 성공"),
	CREATE_STICKER_SUCCESS(HttpStatus.CREATED, "S001", "스티커 생성 성공"),
	// Calendar
	FIND_CALENDAR_SUCCESS(HttpStatus.OK, "C001", "캘린더 조회 성공"),
	UPDATE_CALENDAR_STICKER_SUCCESS(HttpStatus.OK, "C101", "캘린더 스티커 변경 성공"),
	;
	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}

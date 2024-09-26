package com.backend.global.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// Common
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E001", "올바르지 않은 입력값입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "E002", ""),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "E003", ""),
	CONFLICT(HttpStatus.CONFLICT, "E009", "Conflict"),

	// Auth
	// Member
	MEMBER_LOGIN_CONFLICT(HttpStatus.CONFLICT, "M009", "이미 로그인 되어 있습니다."),
	MEMBER_LOGOUT_CONFLICT(HttpStatus.CONFLICT, "M109", "이미 로그아웃 되어 있습니다."),
	MEMBER_LOGIN_ID_CONFLICT(HttpStatus.CONFLICT, "M209", "이미 존재하는 아이디입니다."),
	MEMBER_NICKNAME_CONFLICT(HttpStatus.CONFLICT, "M309", "이미 존재하는 닉네임입니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "해당 회원을 찾을 수 없습니다."),
	// Diary
	DIARY_BAD_REQUEST(HttpStatus.BAD_REQUEST, "D000", "Bad Request"),
	DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "D004", "해당 일기를 찾을 수 없습니다"),
	DIARY_CONFLICT(HttpStatus.CONFLICT, "D009", "해당 일기가 이미 존재합니다"),
	DIARY_GONE(HttpStatus.GONE, "D010", "해당 일기가 만료되었습니다"),

	// Calendar
	CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "C004", "해당 달력을 찾을 수 없습니다."),

	// TextBox
	// Sticker
	NOT_FOUND_STICKER(HttpStatus.NOT_FOUND, "S004", "해당 스티커를 찾을 수 없습니다."),
	;
	private final HttpStatus status;
	private final String message;
	private final String code;

	ErrorCode(final HttpStatus status,  final String code, final String message) {
		this.status = status;
		this.message = message;
		this.code = code;
	}
}

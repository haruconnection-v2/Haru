package com.backend.domain.diary.dto;

import lombok.Getter;

@Getter
public class DiaryForCalendarDisplayDto {
	private final String day;
	private final Boolean isExpiry;

	public DiaryForCalendarDisplayDto(String day, Boolean isExpiry) {
		this.day = day;
		this.isExpiry = isExpiry;
	}
}

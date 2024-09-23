package com.backend.domain.diary.dto;

import lombok.Getter;

@Getter
public class DiaryForCalendarDisplayDto {
	private final String day;
	private final boolean isExpiry;

	public DiaryForCalendarDisplayDto(String day, boolean isExpiry) {
		this.day = day;
		this.isExpiry = isExpiry;
	}
}

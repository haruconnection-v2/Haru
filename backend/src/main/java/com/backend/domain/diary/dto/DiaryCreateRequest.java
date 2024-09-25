package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
public class DiaryCreateRequest {
	private final String monthYear;
	private final Long diaryBgId;
	private final String day;

	@Builder
	public DiaryCreateRequest(String monthYear, Long diaryBgId, String day) {
		this.monthYear = monthYear;
		this.diaryBgId = diaryBgId;
		this.day = day;
	}
}

package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryRequest {
	private final Long diaryId;
	private final String monthYear;
	private final Long diaryBgId;
	private final String day;
	@Builder
	public DiaryRequest(Long diaryId, String monthYear, Long diaryBgId, String day) {
		this.diaryId = diaryId;
		this.monthYear = monthYear;
		this.diaryBgId = diaryBgId;
		this.day = day;
	}
}

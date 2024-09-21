package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryResponse {
	private final Long diaryId;
	private final Long diaryBgId;
	private final String day;
	private final String monthYear;
	private final String snsLink;
	private final Boolean isExpiry;

	@Builder
	public DiaryResponse(Long diaryId, Long diaryBgId, String day, String monthYear, String snsLink, Boolean isExpiry) {
		this.diaryId = diaryId;
		this.diaryBgId = diaryBgId;
		this.day = day;
		this.monthYear = monthYear;
		this.snsLink = snsLink;
		this.isExpiry = isExpiry;
	}
}

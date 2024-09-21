package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiarySnsDto {
	private final Long diaryId;
	private final String monthYear;
	private final String day;
	private final String snsLink;
	private final boolean isExpiry;

	@Builder
	public DiarySnsDto(Long diaryId, String monthYear, String day, String snsLink, boolean isExpiry) {
		this.diaryId = diaryId;
		this.monthYear = monthYear;
		this.day = day;
		this.snsLink = snsLink;
		this.isExpiry = isExpiry;
	}
}

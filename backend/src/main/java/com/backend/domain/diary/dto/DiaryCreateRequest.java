package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
public class DiaryCreateRequest {
	private final Long diaryBgId;
	private final String day;

	@Builder
	public DiaryCreateRequest(Long diaryBgId, String day) {
		this.diaryBgId = diaryBgId;
		this.day = day;
	}
}

package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiaryCreateRequest {
	private String monthYear;
	private Long diaryBgId;
	private String day;
}

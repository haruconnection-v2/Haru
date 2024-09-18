package com.backend.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryResponse {
	private Long diaryId;
	private Long diaryBgId;
	private String day;
	private String monthYear;
	private String snsLink;
	private Boolean isExpiry;
}

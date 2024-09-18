package com.backend.domain.diary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryRequest {
	private Long diaryId;
	private String monthYear;
	private Long diaryBgId;
	@NotNull
	private String day;
}

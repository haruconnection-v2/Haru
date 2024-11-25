package com.backend.domain.diary.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryRequest {
	private final List<DiaryStickerReqDto> diaryStickers;
	private final List<DiaryTextBoxReqDto> diaryTextBoxes;
	@Builder
	public DiaryRequest(List<DiaryStickerReqDto> diaryStickers, List<DiaryTextBoxReqDto> diaryTextBoxes) {
		this.diaryStickers = diaryStickers;
		this.diaryTextBoxes = diaryTextBoxes;
	}
}

package com.backend.domain.diary.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryRequest {
	private final List<DiaryStickerReqDto> stickers;
	private final List<DiaryTextBoxReqDto> textBoxes;
	@Builder
	public DiaryRequest(List<DiaryStickerReqDto> stickers, List<DiaryTextBoxReqDto> textBoxes) {
		this.stickers = stickers;
		this.textBoxes = textBoxes;
	}
}

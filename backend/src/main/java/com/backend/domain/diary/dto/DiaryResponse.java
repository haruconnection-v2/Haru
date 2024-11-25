package com.backend.domain.diary.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryResponse {
	private final Long diaryId;
	private final Long diaryBgId;
	private final String nickname;
	private final String day;
	private final String monthYear;
	private final String snsLink;
	private final Boolean isExpiry;
	private final List<DiaryStickerResDto> stickers;
	private final List<DiaryTextBoxResDto> textBoxes;

	@Builder
	public DiaryResponse(Long diaryId, Long diaryBgId, String nickname, String day, String monthYear, String snsLink, Boolean isExpiry,
							List<DiaryStickerResDto> stickers, List<DiaryTextBoxResDto> textBoxes) {
		this.diaryId = diaryId;
		this.diaryBgId = diaryBgId;
		this.nickname = nickname;
		this.day = day;
		this.monthYear = monthYear;
		this.snsLink = snsLink;
		this.isExpiry = isExpiry;
		this.stickers = stickers;
		this.textBoxes = textBoxes;
	}
}

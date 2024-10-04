package com.backend.domain.diary.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.dto.DiaryStickerReqDto;
import com.backend.domain.diary.dto.DiaryStickerResDto;
import com.backend.domain.diary.dto.DiaryTextBoxReqDto;
import com.backend.domain.diary.dto.DiaryTextBoxResDto;
import com.backend.domain.diary.entity.Diary;
import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.entity.DiaryTextBox;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryMapper {
	public static DiaryResponse toDetailDiaryResponse(Diary diary, String nickname) {
		return DiaryResponse.builder()
			.diaryId(diary.getId())
			.monthYear(diary.getMonthYear())
			.diaryBgId(diary.getDiaryBgId())
			.snsLink(diary.getSnsLink())
			.day(diary.getDay())
			.isExpiry(diary.isExpiry())
			.stickers(toDiaryStickerResDtos(diary.getDiaryStickers()))
			.textBoxes(toDiaryTextBoxResDtos(diary.getDiaryTextBoxes()))
			.nickname(nickname)
			.build();
	}

	private static List<DiaryTextBoxResDto> toDiaryTextBoxResDtos(List<DiaryTextBox> diaryTextBoxes) {
		return diaryTextBoxes.stream()
			.map(DiaryMapper::toDiaryTextBoxResDto).toList();
	}

	private static DiaryTextBoxResDto toDiaryTextBoxResDto(DiaryTextBox diaryTextBox) {
		return DiaryTextBoxResDto.builder()
			.textboxId(diaryTextBox.getId())
			.writer(diaryTextBox.getWriter())
			.content(diaryTextBox.getContent())
			.xcoor(diaryTextBox.getXcoor())
			.ycoor(diaryTextBox.getYcoor())
			.width(diaryTextBox.getWidth())
			.height(diaryTextBox.getHeight())
			.build();
	}

	private static List<DiaryStickerResDto> toDiaryStickerResDtos(List<DiarySticker> stickers) {
		return stickers.stream()
			.map(DiaryMapper::toDiaryStickerResDto).toList();
	}

	private static DiaryStickerResDto toDiaryStickerResDto(DiarySticker sticker) {
		return DiaryStickerResDto.builder()
			.stickerId(sticker.getId())
			.stickerImageUrl(sticker.getStickerImageUrl())
			.top(sticker.getTop())
			.leftPos(sticker.getLeftPos())
			.width(sticker.getWidth())
			.height(sticker.getHeight())
			.rotate(sticker.getRotate())
			.build();
	}

	public static DiaryResponse toCreateDiaryResponse(Diary diary, String nickname) {
		return DiaryResponse.builder()
			.diaryId(diary.getId())
			.snsLink(diary.getSnsLink())
			.nickname(nickname)
			.build();
	}

	public static DiaryResponse toSnsDiaryResponse(DiarySnsDto diarySnsDto, String nickname) {
		return DiaryResponse.builder()
			.diaryId(diarySnsDto.getDiaryId())
			.monthYear(diarySnsDto.getMonthYear())
			.day(diarySnsDto.getDay())
			.snsLink(diarySnsDto.getSnsLink())
			.isExpiry(diarySnsDto.isExpiry())
			.nickname(nickname)
			.build();
	}

	public static Diary updateDiaryTextAndImageBoxes(Diary diary, DiaryRequest diaryRequest) {
		diary.getDiaryStickers().clear();
		diary.getDiaryTextBoxes().clear();
		List<DiarySticker> stickers = diaryRequest.getDiaryStickers().stream()
			.map(sticker -> DiaryMapper.toDiarySticker(sticker, diary)).toList();
		diary.getDiaryStickers().addAll(stickers);
		List<DiaryTextBox> textBoxes = diaryRequest.getDiaryTextBoxes().stream()
			.map(textBox -> DiaryMapper.toDiaryTextBox(textBox, diary)).toList();
		diary.getDiaryTextBoxes().addAll(textBoxes);
		return diary;
	}

	private static DiarySticker toDiarySticker(DiaryStickerReqDto sticker, Diary diary) {
		return DiarySticker.builder()
			.stickerImageUrl(sticker.getStickerImageUrl())
			.top(sticker.getTop())
			.leftPos(sticker.getLeftPos())
			.height(sticker.getHeight())
			.width(sticker.getWidth())
			.rotate(sticker.getRotate())
			.diary(diary)
			.build();
	}

	private static DiaryTextBox toDiaryTextBox(DiaryTextBoxReqDto textBox, Diary diary) {
		return DiaryTextBox.builder()
			.writer(textBox.getWriter())
			.content(textBox.getContent())
			.xcoor(textBox.getXcoor())
			.ycoor(textBox.getYcoor())
			.width(textBox.getWidth())
			.height(textBox.getHeight())
			.diary(diary)
			.build();
	}


}

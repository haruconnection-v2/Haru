package com.backend.global.common.mapper;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.entity.Diary;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryMapper {
	public static DiaryResponse toDetailDiaryResponse(Diary diary) {
		return DiaryResponse.builder()
			.diaryId(diary.getId())
			.monthYear(diary.getMonthYear())
			.diaryBgId(diary.getDiaryBgId())
			.snsLink(diary.getSnsLink())
			.day(diary.getDay())
			.isExpiry(diary.getIsExpiry())
			.build();
	}

	public static DiaryResponse toCreateDiaryResponse(Diary diary) {
		return DiaryResponse.builder()
			.diaryId(diary.getId())
			.snsLink(diary.getSnsLink())
			.build();
	}

	public static DiaryResponse toSnsDiaryResponse(DiarySnsDto diarySnsDto) {
		return DiaryResponse.builder()
			.diaryId(diarySnsDto.getDiaryId())
			.monthYear(diarySnsDto.getMonthYear())
			.day(diarySnsDto.getDay())
			.snsLink(diarySnsDto.getSnsLink())
			.isExpiry(diarySnsDto.isExpiry())
			.build();
	}

	public static Diary toEntity(DiaryRequest diaryRequest, HttpSession session, Calendar calendar, String snsLink) {
		return Diary.builder()
			.calendar(calendar)
			.diaryBgId(diaryRequest.getDiaryBgId())
			.day(diaryRequest.getDay())
			.monthYear(session.getAttribute("monthYear").toString())
			.snsLink(snsLink)
			.build();
	}
}

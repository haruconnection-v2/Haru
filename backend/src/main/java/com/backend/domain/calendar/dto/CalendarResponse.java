package com.backend.domain.calendar.dto;

import java.util.List;

import com.backend.domain.diary.dto.DiaryForCalendarDisplayDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CalendarResponse {
	private final List<DiaryForCalendarDisplayDto> diaries;
	private final List<CalendarStickerResDto> calendarStickers;

	@Builder
	public CalendarResponse(List<DiaryForCalendarDisplayDto> diaryForCalendarDisplayDtos, List<CalendarStickerResDto> calendarStickers) {
		this.diaries = diaryForCalendarDisplayDtos;
		this.calendarStickers = calendarStickers;
	}

}

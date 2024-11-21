package com.backend.domain.calendar.mapper;

import java.util.List;

import com.backend.domain.calendar.dto.CalendarResponse;
import com.backend.domain.calendar.dto.CalendarStickerRequest;
import com.backend.domain.calendar.dto.CalendarStickerResDto;
import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.calendar.entity.CalendarSticker;
import com.backend.domain.diary.dto.DiaryForCalendarDisplayDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CalendarMapper {
	public static CalendarResponse toCalendarResponse(List<DiaryForCalendarDisplayDto> diaryForCalendarDisplayDtos,
													  List<CalendarStickerResDto> calendarStickers) {
		return CalendarResponse.builder()
			.diaryForCalendarDisplayDtos(diaryForCalendarDisplayDtos)
			.calendarStickers(calendarStickers)
			.build();
	}

	public static CalendarSticker toCalendarSticker(CalendarStickerRequest calendarStickerRequest, Calendar calendar) {
		return CalendarSticker.builder()
			.stickerImageUrl(calendarStickerRequest.getStickerImageUrl())
			.topPos(calendarStickerRequest.getTopPos())
			.leftPos(calendarStickerRequest.getLeftPos())
			.width(calendarStickerRequest.getWidth())
			.height(calendarStickerRequest.getHeight())
			.rotate(calendarStickerRequest.getRotate())
			.calendar(calendar)
			.build();
	}

	public static List<CalendarStickerResDto> toCalendarResDto(List<CalendarSticker> calendarSticker) {
		return calendarSticker.stream()
			.map(CalendarMapper::toCalendarStickerResDto)
			.toList();
	}

	private static CalendarStickerResDto toCalendarStickerResDto(CalendarSticker calendarSticker) {
		return CalendarStickerResDto.builder()
			.id(calendarSticker.getId())
			.stickerImageUrl(calendarSticker.getStickerImageUrl())
			.topPos(calendarSticker.getTopPos())
			.leftPos(calendarSticker.getLeftPos())
			.width(calendarSticker.getWidth())
			.height(calendarSticker.getHeight())
			.rotate(calendarSticker.getRotate())
			.build();
	}
}

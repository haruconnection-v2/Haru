package com.backend.domain.calendar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domain.calendar.dto.CalendarResponse;
import com.backend.domain.calendar.dto.CalendarStickerRequest;
import com.backend.domain.calendar.service.CalendarService;
import com.backend.global.common.response.ResultCode;
import com.backend.global.common.response.ResultResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;
	@GetMapping("/calendars/")
	public ResponseEntity<ResultResponse<CalendarResponse>> getCalendar(@RequestParam String monthYear, HttpServletRequest request) {
		CalendarResponse calendarResponse = calendarService.getCalendarData(monthYear, request);
		ResultResponse<CalendarResponse> resultResponse = ResultResponse.of(ResultCode.FIND_CALENDAR_SUCCESS, calendarResponse);
		return ResponseEntity.status(ResultCode.FIND_CALENDAR_SUCCESS.getHttpStatus()).body(resultResponse);
	}

	@PostMapping("/calendars/sticker")
	public ResponseEntity<ResultResponse<Void>> updateCalendarSticker(@RequestBody CalendarStickerRequest calendarStickerRequest,
																	  HttpServletRequest request) {
		calendarService.updateCalendarSticker(calendarStickerRequest, request);
		ResultResponse<Void> resultResponse = ResultResponse.of(ResultCode.UPDATE_CALENDAR_STICKER_SUCCESS);
		return ResponseEntity.status(ResultCode.UPDATE_CALENDAR_STICKER_SUCCESS.getHttpStatus()).body(resultResponse);
	}
}

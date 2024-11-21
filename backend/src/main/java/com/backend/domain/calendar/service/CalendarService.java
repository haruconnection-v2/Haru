package com.backend.domain.calendar.service;

import org.springframework.stereotype.Component;

import com.backend.domain.calendar.dto.CalendarResponse;
import com.backend.domain.calendar.dto.CalendarStickerRequest;
import com.backend.domain.calendar.entity.Calendar;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public interface CalendarService {
	CalendarResponse getCalendarData(String monthYear, HttpServletRequest request);
	void updateCalendarSticker(CalendarStickerRequest calendarStickerRequest, HttpServletRequest request);

	Calendar createAndSessionStoreCalendar(HttpSession session, String monthYear);
}

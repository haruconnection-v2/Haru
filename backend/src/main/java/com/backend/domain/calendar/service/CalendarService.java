package com.backend.domain.calendar.service;

import org.springframework.stereotype.Component;

import com.backend.domain.calendar.entity.Calendar;

import jakarta.servlet.http.HttpSession;

@Component
public interface CalendarService {
	void getCalendarData(Long memberId, Integer year, Integer month);

	Calendar createAndSessionStoreCalendar(HttpSession session, String monthYear);
}

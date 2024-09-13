package com.backend.domain.calendar.service;

import org.springframework.stereotype.Component;

@Component
public interface CalendarService {
	void getCalendarData(Long memberId, Integer year, Integer month);
}

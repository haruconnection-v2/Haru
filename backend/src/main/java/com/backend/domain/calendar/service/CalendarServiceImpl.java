package com.backend.domain.calendar.service;

import org.springframework.stereotype.Service;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.calendar.repository.CalendarRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

	private final CalendarRepository calendarRepository;

	@Override
	public void getCalendarData(Long memberId, Integer year, Integer month) {
		// calendarRepository.findById();

	}

	@Override
	public Calendar createAndSessionStoreCalendar(HttpSession session, String monthYear) {
		Calendar calendar = calendarRepository.save(Calendar.builder().monthYear(monthYear).build());
		session.setAttribute("calendarId", calendar.getId());
		return calendar;
	}
}

package com.backend.domain.calendar.service;

import org.springframework.stereotype.Service;

import com.backend.domain.calendar.repository.CalendarRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

	private final CalendarRepository calendarRepository;

	@Override
	public void getCalendarData(Long memberId, Integer year, Integer month) {
		// calendarRepository.findById();

	}
}

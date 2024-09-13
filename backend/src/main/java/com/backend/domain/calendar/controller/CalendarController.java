package com.backend.domain.calendar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domain.calendar.service.CalendarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;

	@GetMapping("/{id}?year={year}&month={month}")
	public void getCalendar(@PathVariable("id") Long id, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
		calendarService.getCalendarData(id, year, month);
	}
}

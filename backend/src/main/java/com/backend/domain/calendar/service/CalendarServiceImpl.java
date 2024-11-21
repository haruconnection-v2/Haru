package com.backend.domain.calendar.service;

import static com.backend.domain.calendar.mapper.CalendarMapper.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domain.calendar.dto.CalendarResponse;
import com.backend.domain.calendar.dto.CalendarStickerRequest;
import com.backend.domain.calendar.dto.CalendarStickerResDto;
import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.calendar.repository.CalendarRepository;
import com.backend.domain.diary.dto.DiaryForCalendarDisplayDto;
import com.backend.domain.diary.repository.DiaryRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarServiceImpl implements CalendarService {

	private final CalendarRepository calendarRepository;
	private final DiaryRepository diaryRepository;
	private final MemberRepository memberRepository;

	@Override
	public CalendarResponse getCalendarData(String monthYear, HttpServletRequest request) {
		Long memberId = (Long)request.getSession().getAttribute("memberId");
		//findByMemberIdAndMonthYear
		Calendar calendar = calendarRepository.findByMemberIdAndMonthYear(memberId, monthYear)
			.orElseThrow(()-> {
				request.getSession().setAttribute("calendarId", null);
				request.getSession().setAttribute("monthYear", monthYear);
				return new NotFoundException(ErrorCode.CALENDAR_NOT_FOUND);
			});
		List<DiaryForCalendarDisplayDto> diaryForCalendarDisplayDtos = diaryRepository.findAllByCalendarId(calendar.getId());
		List<CalendarStickerResDto> stickers = toCalendarResDto(calendar.getCalendarSticker());
		request.getSession().setAttribute("calendarId", calendar.getId());
		request.getSession().setAttribute("monthYear", monthYear);
		return toCalendarResponse(diaryForCalendarDisplayDtos, stickers);
	}

	@Override
	@Transactional
	public void updateCalendarSticker(CalendarStickerRequest calendarStickerRequest, HttpServletRequest request) {
		Long calendarId = (Long)request.getSession().getAttribute("calendarId");
		Calendar calendar;
		if (calendarId == null) {
			String monthYear = request.getSession().getAttribute("monthYear").toString();
			calendar = createAndStoreCalendar(request.getSession(), monthYear);
		} else {
			calendar = calendarRepository.findById(calendarId).orElseThrow(() -> new NotFoundException(ErrorCode.CALENDAR_NOT_FOUND));
		}
		calendar.getCalendarSticker().add(toCalendarSticker(calendarStickerRequest, calendar));
		calendarRepository.save(calendar);
	}

	@Override
	@Transactional
	public Calendar createAndSessionStoreCalendar(HttpSession session, String monthYear) {
		return createAndStoreCalendar(session, monthYear);
	}

	private Calendar createAndStoreCalendar(HttpSession session, String monthYear) {
		Long memberId = (Long)session.getAttribute("memberId");
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		Calendar calendar = calendarRepository.save(Calendar.builder().monthYear(monthYear).member(member).build());
		session.setAttribute("calendarId", calendar.getId());
		return calendar;
	}
}

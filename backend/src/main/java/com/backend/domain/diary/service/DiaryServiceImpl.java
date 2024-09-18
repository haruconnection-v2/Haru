package com.backend.domain.diary.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.calendar.repository.CalendarRepository;
import com.backend.domain.calendar.service.CalendarService;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.entity.Diary;
import com.backend.domain.diary.repository.DiaryRepository;
import com.backend.global.common.mapper.DiaryMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final CalendarService calendarService;
	private final CalendarRepository calendarRepository;

	//mapper에 member.nickname 추가예정
	//session 로직 추가 예정
	@Override
	public DiaryResponse getDiaryDetialData(Long diaryId) {
		Diary foundDiary = diaryRepository.findById(diaryId).orElseThrow(() -> new IllegalArgumentException("해당 일기가 존재하지 않습니다."));
		DiaryResponse diaryResponse =  DiaryMapper.toDetailDiaryResponse(foundDiary);
		//diaryResponse = setMemberNickname;
		//diaryResponse = setTextAndImageBoxes;
		return diaryResponse;
	}

	@Override
	public DiaryResponse getDiarySnsLink(String day, HttpServletRequest request) {
		Long calendarId = (Long)request.getSession().getAttribute("calendarId");
		if (calendarId == null) {
			throw new IllegalArgumentException("캘린더가 존재하지 않습니다.");
		}
		DiarySnsDto diarySnsDto = diaryRepository.findDiarySnsByCalendarIdAndDay(calendarId, day);
		if (diarySnsDto.isExpiry()) {
			throw new IllegalArgumentException("해당 일기는 만료되었습니다.");
		}
		DiaryResponse diaryResponse = DiaryMapper.toSnsDiaryResponse(diarySnsDto);
		//diaryResponse = setMemberNickname;
		return diaryResponse;
	}

	@Override
	@Transactional
	public DiaryResponse createDiary(DiaryRequest diaryRequest, HttpServletRequest request) {
		HttpSession session = request.getSession();
		//session calendarId
		Calendar calendar = getOrCreateCalendar(diaryRequest, session);
		validateDiaryNotExist((Long)session.getAttribute("calendarId"), diaryRequest.getDay());
		//room create;
		Long roomId = 1L;
		//make snsLink;
        String snsLink = generateSnsLink(request, roomId);
		//diary create;
		Diary diary = diaryRepository.save(createDiaryEntity(diaryRequest, session, calendar, snsLink));
		return DiaryMapper.toCreateDiaryResponse(diary);
	}

	private Diary createDiaryEntity(DiaryRequest diaryRequest, HttpSession session, Calendar calendar, String snsLink) {
		return Diary.builder()
			.calendar(calendar)
			.diaryBgId(diaryRequest.getDiaryBgId())
			.day(diaryRequest.getDay())
			.monthYear(session.getAttribute("monthYear").toString())
			.snsLink(snsLink)
			.isExpiry(false)
			.build();
	}

	private String generateSnsLink(HttpServletRequest request, Long roomId) {
		String host = request.getServerName();
		int port = request.getServerPort();
		return String.format("http://%s:%d/rooms/%d", host, port, roomId);
	}

	private Calendar getOrCreateCalendar(DiaryRequest diaryRequest, HttpSession session) {
		Long calendarId = (Long)session.getAttribute("calendarId");
		if (calendarId == null) {
			return calendarService.createAndSessionStoreCalendar(session, diaryRequest.getMonthYear());
		} else {
			return calendarRepository.findById(calendarId)
				.orElseThrow(() -> new IllegalArgumentException("해당 캘린더가 존재하지 않습니다."));
		}
	}

	private void validateDiaryNotExist(Long calendarId, String day) {
		if (diaryRepository.existsByCalendarIdAndDay(calendarId, day)) {
			throw new IllegalArgumentException("해당 날짜에 일기가 이미 존재합니다.");
		}
	}

	@Override
	@Transactional
	public void saveFinallyDiary(DiaryRequest diaryRequest, HttpServletRequest request) {
		//Textbox, Imagebox 저장 구현 필요
		Diary diary = diaryRepository.findById(diaryRequest.getDiaryId())
			.orElseThrow(() -> new IllegalArgumentException("해당 일기가 존재하지 않습니다."));
		diary.setIsExpiry(true);
		diaryRepository.save(diary);
		// diaryRepository.findById();
	}
}

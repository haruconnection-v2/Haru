package com.backend.domain.diary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.calendar.repository.CalendarRepository;
import com.backend.domain.calendar.service.CalendarService;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.entity.Diary;
import com.backend.domain.diary.repository.DiaryRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class DiaryServiceImplTest {

	@Mock
	private DiaryRepository diaryRepository;
	@Mock
	private CalendarService calendarService;
	@Mock
	private CalendarRepository calendarRepository;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	@InjectMocks
	private DiaryServiceImpl diaryService;

	private Diary foundDiary;
	private Diary expiriedDiary;
	private DiarySnsDto expiredDiarySnsDto;
	private DiarySnsDto diarySnsDto;
	private DiaryRequest diaryRequest;
	private Calendar calendar;
	@BeforeEach
	void setUp() {
		foundDiary = Diary.builder()
			.monthYear("2024-09")
			.day("2024-09-15")
			.snsLink("ws/abcdefg")
			.diaryBgId(1L)
			.isExpiry(false)
			.build();
		expiriedDiary = Diary.builder()
			.monthYear("2024-09")
			.day("2024-09-15")
			.snsLink("ws/abcdefg")
			.diaryBgId(1L)
			.isExpiry(true)
			.build();
		expiredDiarySnsDto = new DiarySnsDto(1L, "2024-09", "2024-09-15", "ws/abcdefg", true);
		diarySnsDto = new DiarySnsDto(1L, "2024-09", "2024-09-15", "ws/abcdefg", false);
		diaryRequest = new DiaryRequest(1L, "2024-09", 1L, "2024-09-15");
		calendar = Calendar.builder().monthYear("2024-09").build();
	}
	@Test
	void 일기_조회에_실패하여_예외가_발생한다() {
		given(diaryRepository.findById(any())).willReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> diaryService.getDiaryDetialData(1L));
		verify(diaryRepository).findById(any());
	}
	@Test
	void 일기_조회에_성공한다() {
		given(diaryRepository.findById(any())).willReturn(Optional.of(foundDiary));

		DiaryResponse diaryResponse = diaryService.getDiaryDetialData(foundDiary.getId());
		assertNotNull(diaryResponse);
		assertEquals(foundDiary.getId(), diaryResponse.getDiaryId());
		assertEquals(foundDiary.getMonthYear(), diaryResponse.getMonthYear());
	}

	@Test
	void 캘린더아이디가_없어서_일기sns조회를_실패한다() {
		String day = "2024-09-15";
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(null);

		assertThrows(IllegalArgumentException.class, () -> diaryService.getDiarySnsLink(day, request));
		verify(diaryRepository, never()).findDiarySnsByCalendarIdAndDay(any(), any());
	}
	@Test
	void 일기가_만료되어_일기sns조회를_실패한다() {
		String day = "2024-09-15";
		Long calendarId1 = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId1);
		given(diaryRepository.findDiarySnsByCalendarIdAndDay(any(), any()))
			.willReturn(expiredDiarySnsDto);

		assertThrows(IllegalArgumentException.class, () -> diaryService.getDiarySnsLink(day, request));

	}
	@Test
	void 일기sns조회에_성공한다() {
		String day = "2024-09-15";
		Long calendarId1 = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId1);
		given(diaryRepository.findDiarySnsByCalendarIdAndDay(any(), any()))
			.willReturn(diarySnsDto);

		DiaryResponse diaryResponse = diaryService.getDiarySnsLink(day, request);

		assertNotNull(diaryResponse);
		assertEquals(diarySnsDto.getDiaryId(), diaryResponse.getDiaryId());
		assertEquals(diarySnsDto.getMonthYear(), diaryResponse.getMonthYear());
	}

	@Test
	void day에_일기가_있어_일기생성을_실패한다() {
		String day = "2024-09-15";
		Long calendarId1 = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId1);
		given(diaryRepository.existsByCalendarIdAndDay(any(), any())).willReturn(true);
		given(calendarRepository.findById(any())).willReturn(Optional.of(calendar));
		assertThrows(IllegalArgumentException.class, () -> diaryService.createDiary(diaryRequest, request));
	}

	@Test
	void 일기생성에_성공한다() {
		String day = "2024-09-15";
		Long calendarId1 = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId1);
		given(session.getAttribute("monthYear")).willReturn("2024-09");
		given(calendarRepository.findById(any())).willReturn(Optional.of(calendar));
		given(diaryRepository.existsByCalendarIdAndDay(any(), any())).willReturn(false);
		given(diaryRepository.save(any(Diary.class))).willReturn(foundDiary);

		DiaryResponse diaryResponse = diaryService.createDiary(diaryRequest, request);

		assertNotNull(diaryResponse);
		assertEquals(diaryResponse.getDay(), day);
	}
	@Test
	void saveFinallyDiary() {

	}
}
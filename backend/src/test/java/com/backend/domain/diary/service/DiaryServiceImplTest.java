package com.backend.domain.diary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
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
import com.backend.domain.diary.dto.DiaryCreateRequest;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.entity.Diary;
import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.repository.DiaryRepository;
import com.backend.global.common.exception.BadRequestException;
import com.backend.global.common.exception.ConflictException;
import com.backend.global.common.exception.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class DiaryServiceImplTest {

	/*@Mock
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
	private DiarySticker diarySticker;
	private DiarySticker diarySticker2;
	private DiaryTextBox diaryTextBox;
	private DiaryTextBox diaryTextBox2;
	private DiarySnsDto expiredDiarySnsDto;
	private DiarySnsDto diarySnsDto;
	private Calendar calendar;
	private  DiaryRequest diarySaveRequest;
	private DiaryCreateRequest createDiaryRequest;
	@BeforeEach
	void setUp() {
		foundDiary = Diary.builder()
			.monthYear("2024-09")
			.day("2024-09-15")
			.snsLink("ws/abcdefg")
			.diaryBgId(1L)
			.isExpiry(false)
			.build();
		diarySticker = DiarySticker.builder()
			.stickerImgUrl("diarySticker")
			.top(1)
			.leftPos(1)
			.width(1)
			.height(1)
			.rotate(1)
			.build();
		diarySticker2 = DiarySticker.builder()
			.stickerImgUrl("diarySticker2")
			.top(1)
			.leftPos(1)
			.width(1)
			.height(1)
			.rotate(1)
			.build();
		diaryTextBox = DiaryTextBox.builder()
			.writer("woo")
			.content("text")
			.xcoor(1)
			.ycoor(1)
			.width(1)
			.height(1)
			.build();
		diaryTextBox2 = DiaryTextBox.builder()
			.writer("woo2")
			.content("text2")
			.xcoor(1)
			.ycoor(1)
			.width(1)
			.height(1)
			.build();
		expiredDiarySnsDto = DiarySnsDto.builder()
			.diaryId(1L)
			.monthYear("2024-09")
			.day("2024-09-15")
			.snsLink("ws/abcdefg")
			.isExpiry(true).build();
		diarySnsDto = DiarySnsDto.builder()
			.diaryId(1L)
			.monthYear("2024-09")
			.day("2024-09-15")
			.snsLink("ws/abcdefg")
			.isExpiry(false).build();
		diarySticker = DiarySticker.builder()
			.stickerImgUrl("diarySticker")
			.top(1)
			.leftPos(1)
			.width(1)
			.height(1)
			.rotate(1)
			.build();
		diarySticker2 = DiarySticker.builder()
			.stickerImgUrl("diarySticker2")
			.top(1)
			.leftPos(1)
			.width(1)
			.height(1)
			.rotate(1)
			.build();
		diaryTextBox = DiaryTextBox.builder()
			.writer("woo")
			.content("text")
			.xcoor(1)
			.ycoor(1)
			.width(1)
			.height(1)
			.build();
		diaryTextBox2 = DiaryTextBox.builder()
			.writer("woo2")
			.content("text")
			.xcoor(1)
			.ycoor(1)
			.width(1)
			.height(1)
			.build();
*//*		diarySaveRequest = DiaryRequest.builder()
			.stickers(List.of(diarySticker, diarySticker2))
			.textBoxes(List.of(diaryTextBox, diaryTextBox2))
			.build();*//*
		createDiaryRequest = DiaryCreateRequest.builder().diaryBgId(1L).monthYear("2024-09").day("2024-09-15").build();
		calendar = Calendar.builder().monthYear("2024-09").build();
	}
	@Test
	void 일기_조회에_실패하여_예외가_발생한다() {
		//given(session.getAttribute("calendarId")).willReturn(1L);
		given(diaryRepository.findById(any())).willReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> diaryService.getDiaryDetialData(1L, request));
		verify(diaryRepository).findById(any());
	}
	@Test
	void 일기_조회에_성공한다() {
		given(diaryRepository.findById(any())).willReturn(Optional.of(foundDiary));
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("nickname")).willReturn(anyString());
		DiaryResponse diaryResponse = diaryService.getDiaryDetialData(foundDiary.getId(), request);
		assertNotNull(diaryResponse);
		assertEquals(foundDiary.getId(), diaryResponse.getDiaryId());
		assertEquals(foundDiary.getMonthYear(), diaryResponse.getMonthYear());
	}

	@Test
	void 캘린더아이디가_없어서_일기sns조회를_실패한다() {
		String day = "2024-09-15";
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(null);

		assertThrows(NotFoundException.class, () -> diaryService.getDiarySnsLink(day, request));
		verify(diaryRepository, never()).findDiarySnsByCalendarIdAndDay(any(), any());
	}
	@Test
	void 일기가_만료되어_일기sns조회를_실패한다() {
		String day = "2024-09-15";
		Long calendarId = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId);
		given(diaryRepository.findDiarySnsByCalendarIdAndDay(any(), any()))
			.willReturn(expiredDiarySnsDto);

		assertThrows(BadRequestException.class, () -> diaryService.getDiarySnsLink(day, request));

	}
	@Test
	void 일기sns조회에_성공한다() {
		String day = "2024-09-15";
		Long calendarId = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId);
		given(session.getAttribute("nickname")).willReturn("woo");
		given(diaryRepository.findDiarySnsByCalendarIdAndDay(any(), anyString()))
			.willReturn(diarySnsDto);

		DiaryResponse diaryResponse = diaryService.getDiarySnsLink(day, request);

		assertNotNull(diaryResponse);
		assertEquals(diarySnsDto.getDiaryId(), diaryResponse.getDiaryId());
		assertEquals(diarySnsDto.getMonthYear(), diaryResponse.getMonthYear());
	}

	@Test
	void day에_일기가_있어_일기생성을_실패한다() {
		Long calendarId1 = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId1);
		given(diaryRepository.existsByCalendarIdAndDay(any(), any())).willReturn(true);
		given(calendarRepository.findById(any())).willReturn(Optional.of(calendar));
		assertThrows(ConflictException.class, () -> diaryService.createDiary(createDiaryRequest, request));
	}

	@Test
	void 일기생성에_성공한다() {
		Long calendarId1 = 1L;
		given(request.getSession()).willReturn(session);
		given(session.getAttribute("calendarId")).willReturn(calendarId1);
		given(session.getAttribute("monthYear")).willReturn("2024-09");
		given(session.getAttribute("nickname")).willReturn("woo");
		given(calendarRepository.findById(any())).willReturn(Optional.of(calendar));
		given(diaryRepository.existsByCalendarIdAndDay(any(), any())).willReturn(false);
		given(diaryRepository.save(any(Diary.class))).willReturn(foundDiary);

		DiaryResponse diaryResponse = diaryService.createDiary(createDiaryRequest, request);

		assertNotNull(diaryResponse);
		assertEquals(diaryResponse.getDiaryId(), foundDiary.getId());
	}
	@Test
	void saveFinallyDiary() {
		given(diaryRepository.findById(any())).willReturn(Optional.of(foundDiary));
		given(diaryRepository.save(any(Diary.class))).willReturn(foundDiary);

		diaryService.saveFinallyDiary(1L, diarySaveRequest);

	}*/
}
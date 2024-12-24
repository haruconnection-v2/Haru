/*
package com.backend.domain.diary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.domain.diary.dto.DiaryStickerReqDto;
import com.backend.domain.diary.dto.DiaryTextBoxReqDto;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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
import com.backend.domain.diary.repository.DiaryRepository;
import com.backend.global.common.exception.BadRequestException;
import com.backend.global.common.exception.ConflictException;
import com.backend.global.common.exception.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class DiaryServiceImplTest {

    @Mock
    private DiaryRepository diaryRepository;
    @Mock
    private HaruRoomRepository haruRoomRepository;
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
    private DiarySnsDto expiredDiarySnsDto;
    private DiarySnsDto diarySnsDto;
    private Calendar calendar;
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
        createDiaryRequest = DiaryCreateRequest.builder().diaryBgId(1L).day("2024-09-15").build();
        calendar = Calendar.builder().monthYear("2024-09").build();
    }

    @Test
    void 일기_조회에_실패하여_예외가_발생한다() {
        given(diaryRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> diaryService.getDiaryDetailData(1L, request));
        verify(diaryRepository).findById(any());
    }

    @Test
    void 일기_조회에_성공한다() {
        given(diaryRepository.findById(any())).willReturn(Optional.of(foundDiary));
        given(request.getSession()).willReturn(session);
        given(session.getAttribute("nickname")).willReturn(anyString());
        DiaryResponse diaryResponse = diaryService.getDiaryDetailData(foundDiary.getId(), request);
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
        given(calendarService.getOrCreateCalendar(session)).willReturn(calendar);
        given(diaryRepository.existsByCalendarIdAndDay(any(), any())).willReturn(true);
        Assertions.assertThatThrownBy(() -> diaryService.createDiary(createDiaryRequest, request))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void 일기생성에_성공한다() {
        Long calendarId1 = 1L;
        Calendar mockCalendar = mock(Calendar.class);
        HaruRoom haruRoom = mock(HaruRoom.class);
        given(mockCalendar.getId()).willReturn(1L);
        given(request.getSession()).willReturn(session);
        given(session.getAttribute("monthYear")).willReturn("2024-09");
        given(session.getAttribute("nickname")).willReturn("woo");
        given(calendarService.getOrCreateCalendar(any())).willReturn(mockCalendar);
        given(diaryRepository.existsByCalendarIdAndDay(any(), any())).willReturn(false);
        given(haruRoomRepository.save(any())).willReturn(haruRoom);
        given(diaryRepository.save(any(Diary.class))).willReturn(foundDiary);

        DiaryResponse diaryResponse = diaryService.createDiary(createDiaryRequest, request);

        assertNotNull(diaryResponse);
        assertEquals(diaryResponse.getDiaryId(), foundDiary.getId());
    }

    @Test
    void saveFinallyDiary() {
        final Long id = 1L;
        DiaryStickerReqDto diaryStickerReqDto = DiaryStickerReqDto.builder()
                .stickerImageUrl("string")
                .top(1)
                .width(1)
                .height(1)
                .leftPos(1)
                .rotate(1)
                .build();
        DiaryTextBoxReqDto diaryTextBoxReqDto = DiaryTextBoxReqDto.builder()
                .writer("string").content("string")
                .xcoor(1).ycoor(1).width(1).height(1).build();
        given(diaryRepository.findById(any())).willReturn(Optional.of(foundDiary));
        given(diaryRepository.save(any(Diary.class))).willReturn(foundDiary);
        DiaryRequest diaryRequest = DiaryRequest.builder()
                .diaryStickers(List.of(diaryStickerReqDto))
                .diaryTextBoxes(List.of(diaryTextBoxReqDto)).build();
        diaryService.saveFinallyDiary(id, diaryRequest);

        verify(diaryRepository).save(any(Diary.class));
    }
}*/

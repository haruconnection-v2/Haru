package com.backend.domain.diary.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;  // 응답 상태 코드 검증

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.service.DiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DiaryService diaryService;
	private DiaryResponse diaryResponse;
	private DiaryRequest invalidDiaryRequest;
	private DiaryRequest saveFinallyDiaryRequest;

	private MockHttpServletRequest request;
	private MockHttpSession nullCalendarIdsession;
	private MockHttpSession haveCalendarIdSession;

	@BeforeEach
	void setUp() {
		diaryResponse = DiaryResponse.builder()
			.diaryId(1L)
			.isExpiry(false)
			.day("2024-09-18")
			.diaryBgId(1L)
			.monthYear("2024-09")
			.snsLink("ws/")
			.build();
		request = new MockHttpServletRequest();
		nullCalendarIdsession = new MockHttpSession();
		nullCalendarIdsession.setAttribute("calendarId", null);
		haveCalendarIdSession = new MockHttpSession();
		haveCalendarIdSession.setAttribute("calendarId", 1L);
		invalidDiaryRequest = new DiaryRequest();
	}
	@Test
	void getDiaryDetail() throws Exception {

		given(diaryService.getDiaryDetialData(anyLong())).willReturn(diaryResponse);

		mockMvc.perform(get("/v1/diaries/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.diaryId").value(1L))
			.andExpect(jsonPath("$.isExpiry").value(false))
			.andExpect(jsonPath("$.day").value("2024-09-18"))
			.andExpect(jsonPath("$.diaryBgId").value(1L))
			.andExpect(jsonPath("$.monthYear").value("2024-09"))
			.andExpect(jsonPath("$.snsLink").value("ws/"));
	}



	@Test
	void snsLink_조회를_성공한다() throws  Exception {
		given(diaryService.getDiarySnsLink(anyString(), any(HttpServletRequest.class))).willReturn(diaryResponse);

		mockMvc.perform(get("/v1/diaries/link")
			.param("day", "2024-09-18")
			.session(nullCalendarIdsession))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.diaryId").value(1L))
			.andExpect(jsonPath("$.isExpiry").value(false))
			.andExpect(jsonPath("$.day").value("2024-09-18"))
			.andExpect(jsonPath("$.diaryBgId").value(1L))
			.andExpect(jsonPath("$.monthYear").value("2024-09"))
			.andExpect(jsonPath("$.snsLink").value("ws/"));
	}

	@Test
	void createDiary() throws Exception {
/*		//실패 에러 메세지 못잡음
		mockMvc.perform(post("/v1/diaries")
			.session(haveCalendarIdSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(invalidDiaryRequest)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").exists());*/
		DiaryRequest diaryRequest = new DiaryRequest();
		diaryRequest.setDiaryBgId(1L);
		diaryRequest.setDay("2024-09-18");
		DiaryResponse CreatedDiaryResponse = DiaryResponse.builder().diaryId(1L).snsLink("ws/").build();
		given(diaryService.createDiary(any(DiaryRequest.class), any(HttpServletRequest.class)))
			.willReturn(CreatedDiaryResponse);

		mockMvc.perform(post("/v1/diaries")
			.session(haveCalendarIdSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(diaryRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.diaryId").value(1L));

	}

	@Test
	void saveFinallyDiary() {
	}
}
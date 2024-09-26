package com.backend.domain.diary.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;  // 응답 상태 코드 검증

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.backend.domain.diary.dto.DiaryCreateRequest;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.service.DiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

class DiaryControllerTest {


	@BeforeEach
	void setUp() {

	}

	@Test
	void getDiaryDetail() throws Exception {

	}

	@Test
	void snsLink_조회를_성공한다() throws  Exception {
	}

	@Test
	void createDiary() throws Exception {
	}

	@Test
	void saveFinallyDiary() {
	}
}
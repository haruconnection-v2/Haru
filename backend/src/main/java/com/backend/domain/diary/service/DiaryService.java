package com.backend.domain.diary.service;

import org.springframework.stereotype.Component;

import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;

import jakarta.servlet.http.HttpServletRequest;

@Component
public interface DiaryService {
	DiaryResponse getDiaryDetialData(Long diaryId);

	DiaryResponse getDiarySnsLink(String day, HttpServletRequest request);

    DiaryResponse createDiary(DiaryRequest diaryRequest, HttpServletRequest request);

	void saveFinallyDiary(DiaryRequest diaryRequest, HttpServletRequest request);

}

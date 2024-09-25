package com.backend.domain.diary.service;

import org.springframework.stereotype.Component;

import com.backend.domain.diary.dto.DiaryCreateRequest;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;

import jakarta.servlet.http.HttpServletRequest;

@Component
public interface DiaryService {
	DiaryResponse getDiaryDetialData(Long diaryId, HttpServletRequest request);

	DiaryResponse getDiarySnsLink(String day, HttpServletRequest request);

    DiaryResponse createDiary(DiaryCreateRequest diaryRequest, HttpServletRequest request);

	void saveFinallyDiary(Long diaryId ,DiaryRequest diaryRequest);

}

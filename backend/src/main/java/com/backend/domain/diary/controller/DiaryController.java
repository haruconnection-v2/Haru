package com.backend.domain.diary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.service.DiaryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class DiaryController {
	private final DiaryService diaryService;

	@GetMapping("/diaries/{diaryId}")
	public ResponseEntity<DiaryResponse> getDiaryDetail(@PathVariable Long diaryId) {
		// diaryService.getDiaryDetialData();
		DiaryResponse diaryResponse = diaryService.getDiaryDetialData(diaryId);
		return ResponseEntity.ok(diaryResponse);
	}
	@GetMapping("/diaries/link")
	public ResponseEntity<DiaryResponse> getDiarySnsLink(@RequestParam String day, HttpServletRequest request) {
		DiaryResponse diaryResponse = diaryService.getDiarySnsLink(day, request);
		// diaryService.getDiarySnsLink();
		return ResponseEntity.ok(diaryResponse);
	}
	@PostMapping("/diaries")
	public ResponseEntity<DiaryResponse> createDiary(@Valid @RequestBody DiaryRequest diaryRequest, HttpServletRequest request) {
		DiaryResponse diaryResponse = diaryService.createDiary(diaryRequest, request);
		return ResponseEntity.ok(diaryResponse);
		// diaryService.createDiary();
	}
	@PostMapping("/diaries/{diaryId}")
	public ResponseEntity<Void> saveFinallyDiary(@Valid @RequestBody DiaryRequest diaryRequest, HttpServletRequest request) {
		diaryService.saveFinallyDiary(diaryRequest, request);
		return ResponseEntity.ok().build();
		// diaryService.saveFinallyDiary();
	}

}

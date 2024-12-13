package com.backend.domain.diary.controller;

import static com.backend.global.common.response.ResultCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domain.diary.dto.DiaryCreateRequest;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.service.DiaryService;
import com.backend.global.common.response.ResultResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiaryController {
	private final DiaryService diaryService;

	@GetMapping("/diaries/{diaryId}")
	public ResponseEntity<ResultResponse<DiaryResponse>> getDiaryDetail(@PathVariable Long diaryId, HttpServletRequest request) {
		DiaryResponse diaryResponse = diaryService.getDiaryDetailData(diaryId, request);
		ResultResponse<DiaryResponse> resultResponse = ResultResponse.of(FIND_DIARY_SUCCESS, diaryResponse);
		return ResponseEntity.status(FIND_DIARY_SUCCESS.getHttpStatus()).body(resultResponse);
	}

	@GetMapping("/diaries/link")
	public ResponseEntity<ResultResponse<DiaryResponse>> getDiarySnsLink(@RequestParam String day, HttpServletRequest request) {
		DiaryResponse diaryResponse = diaryService.getDiarySnsLink(day, request);
		ResultResponse<DiaryResponse> resultResponse = ResultResponse.of(FIND_DIARY_SNS_SUCCESS, diaryResponse);
		return ResponseEntity.status(FIND_DIARY_SNS_SUCCESS.getHttpStatus()).body(resultResponse);
	}

	@PostMapping("/diaries")
	public ResponseEntity<ResultResponse<DiaryResponse>> createDiary(@Valid @RequestBody DiaryCreateRequest diaryRequest, HttpServletRequest request) {
		DiaryResponse diaryResponse = diaryService.createDiary(diaryRequest, request);
		ResultResponse<DiaryResponse> resultResponse = ResultResponse.of(CREATE_DIARY_SUCCESS, diaryResponse);
		return ResponseEntity.status(CREATE_DIARY_SUCCESS.getHttpStatus()).body(resultResponse);
	}

	@PutMapping("/diaries/{diaryId}")
	public ResponseEntity<ResultResponse<Void>> saveFinallyDiary(@PathVariable("diaryId") Long diaryId,
													@Valid @RequestBody DiaryRequest diaryRequest) {
		diaryService.saveFinallyDiary(diaryId, diaryRequest);
		ResultResponse<Void> resultResponse = ResultResponse.of(UPDATE_DIARY_SUCCESS);
		return ResponseEntity.status(UPDATE_DIARY_SUCCESS.getHttpStatus()).body(resultResponse);
	}

	@PostMapping("/diaries/stickers")
	public ResponseEntity<ResultResponse<String>> makeSticker(@Valid @RequestBody String content) {
		String sticker = "sticker";
		ResultResponse<String> resultResponse = ResultResponse.of(CREATE_STICKER_SUCCESS, sticker);
		return ResponseEntity.status(CREATE_STICKER_SUCCESS.getHttpStatus()).body(resultResponse);
	}
}

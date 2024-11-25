package com.backend.domain.chat.util;

import org.springframework.stereotype.Component;

import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.repository.DiaryTextBoxRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryTextBoxUtils {
	private final DiaryTextBoxRepository diaryTextBoxRepository;

	public DiaryTextBox fetchDiaryTextBox(String id) {
		return diaryTextBoxRepository.findById(Long.parseLong(id))
			.orElseThrow(() -> new NotFoundException(ErrorCode.TEXT_BOX_NOT_FOUND));
	}

}

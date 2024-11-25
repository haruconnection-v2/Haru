package com.backend.domain.chat.util;

import org.springframework.stereotype.Component;

import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.repository.DiaryStickerRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryStickerUtils {
	private final DiaryStickerRepository diaryStickerRepository;

	public DiarySticker fetchDiarySticker(String id) {
		return diaryStickerRepository.findById(Long.valueOf(id)).orElseThrow(
			() -> new NotFoundException(ErrorCode.STICKER_NOT_FOUND)
		);
	}
}

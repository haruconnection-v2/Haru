package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.dto.request.UpdateDiaryStickerReq;
import com.backend.domain.chat.dto.request.UpdateDiaryTextBoxReq;
import com.backend.domain.chat.util.DiaryStickerUtils;
import com.backend.domain.chat.util.DiaryTextBoxUtils;
import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.repository.DiaryStickerRepository;
import com.backend.domain.diary.repository.DiaryTextBoxRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StopObjectHandler implements MessageHandler {

	private final DiaryTextBoxRepository diaryTextBoxRepository;
	private final DiaryStickerRepository diaryStickerRepository;
	private final DiaryStickerUtils diaryStickerUtils;
	private final DiaryTextBoxUtils diaryTextBoxUtils;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		String objectType = payload.get("object_type").asText();
		// 중복 코드 메서드화 하기
		if (Objects.equals(objectType, "sticker")) {

			String stickerId = payload.get("id").asText();
			JsonNode stickerData = payload.get("position");
			String width = stickerData.get("width2").asText();
			String height = stickerData.get("height2").asText();
			String top = stickerData.get("top2").asText();
			String left = stickerData.get("left2").asText();
			String rotate = stickerData.get("rotate2").asText();

			DiarySticker diarySticker = diaryStickerUtils.fetchDiarySticker(stickerId);

			UpdateDiaryStickerReq req = UpdateDiaryStickerReq.builder()
				.top(Integer.parseInt(top))
				.height(Integer.parseInt(height))
				.leftPos(Integer.parseInt(left))
				.rotate(Integer.parseInt(rotate))
				.width(Integer.parseInt(width))
				.build();

			diarySticker.updateDiarySticker(req);
			diaryStickerRepository.save(diarySticker);

		} else if (Objects.equals(objectType, "text")) {

			String textId = payload.get("id").asText();
			String content = payload.get("content").asText();
			JsonNode textData = payload.get("position");
			String x = textData.get("xcoor").asText();
			String y = textData.get("ycoor").asText();
			String width = textData.get("width").asText();
			String height = textData.get("height").asText();

			DiaryTextBox diaryTextBox = diaryTextBoxUtils.fetchDiaryTextBox(textId);

			UpdateDiaryTextBoxReq req = UpdateDiaryTextBoxReq.builder()
				.content(content)
				.xcoor(Integer.parseInt(x))
				.ycoor(Integer.parseInt(y))
				.width(Integer.parseInt(width))
				.height(Integer.parseInt(height))
				.build();

			diaryTextBox.updateDiaryTextBox(req);
			diaryTextBoxRepository.save(diaryTextBox);

		}

		return CompletableFuture.completedFuture(null);
	}
}

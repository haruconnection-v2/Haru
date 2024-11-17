package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.repository.DiaryStickerRepository;
import com.backend.domain.diary.repository.DiaryTextBoxRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteObjectHandler implements MessageHandler {

	private final DiaryStickerRepository diaryStickerRepository;
	private final DiaryTextBoxRepository diaryTextBoxRepository;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {

		String objectType = payload.get("object_type").asText();
		String objectId = payload.get("object_id").asText();

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "delete_object");
		response.put("object_type", objectType);
		response.put("object_id", objectId);

		log.info("Response created: {}", response);

		if (Objects.equals(objectType, "sticker") || Objects.equals(objectType, "dalle")) {

			DiarySticker diarySticker = diaryStickerRepository.findById(Long.valueOf(objectId)).orElseThrow(
				() -> new NotFoundException(ErrorCode.STICKER_NOT_FOUND)
			);
			diarySticker.changeToDelete();
			diaryStickerRepository.save(diarySticker);

		} else if (Objects.equals(objectType, "textbox")) {

			DiaryTextBox diaryTextBox = diaryTextBoxRepository.findById(Long.valueOf(objectId)).orElseThrow(
				() -> new NotFoundException(ErrorCode.TEXT_BOX_NOT_FOUND)
			);
			diaryTextBox.changeToDelete();
			diaryTextBoxRepository.save(diaryTextBox);
		}

		return CompletableFuture.completedFuture(response);
	}
}

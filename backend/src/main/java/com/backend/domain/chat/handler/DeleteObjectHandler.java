package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.util.DiaryStickerUtils;
import com.backend.domain.chat.util.DiaryTextBoxUtils;
import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.repository.DiaryStickerRepository;
import com.backend.domain.diary.repository.DiaryTextBoxRepository;
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
	private final DiaryStickerUtils diaryStickerUtils;
	private final DiaryTextBoxUtils diaryTextBoxUtils;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {

		String objectType = payload.get("objectType").asText();
		String objectId = payload.get("objectId").asText();

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "deleteObject");
		response.put("objectType", objectType);
		response.put("objectId", objectId);

		log.info("Response created: {}", response);

		if (Objects.equals(objectType, "sticker") || Objects.equals(objectType, "dalle")) {

			DiarySticker diarySticker = diaryStickerUtils.fetchDiarySticker(objectId);
			diarySticker.changeToDelete();
			diaryStickerRepository.save(diarySticker);

		} else if (Objects.equals(objectType, "textbox")) {

			DiaryTextBox diaryTextBox = diaryTextBoxUtils.fetchDiaryTextBox(objectId);

			diaryTextBox.changeToDelete();
			diaryTextBoxRepository.save(diaryTextBox);
		}

		return CompletableFuture.completedFuture(response);
	}
}

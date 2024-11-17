package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.dto.request.UpdateDiaryStickerReq;
import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.repository.DiaryStickerRepository;
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
public class SaveStickerHandler implements MessageHandler {

	private final DiaryStickerRepository diaryStickerRepository;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {

		String stickerId = payload.get("id").asText();
		String stickerUrl = payload.get("image").asText();
		JsonNode stickerData = payload.get("position");
		String width = stickerData.get("width2").asText();
		String height = stickerData.get("height2").asText();
		String top = stickerData.get("top2").asText();
		String left = stickerData.get("left2").asText();
		String rotate = stickerData.get("rotate2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("top2", top);
		positionNode.put("left2", left);
		positionNode.put("width2", width);
		positionNode.put("height2", height);
		positionNode.put("rotate2", rotate);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "save_sticker");
		response.put("sticker_id", stickerId);
		response.put("image", stickerUrl);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		DiarySticker diarySticker = diaryStickerRepository.findById(Long.valueOf(stickerId)).orElseThrow(
			() -> new NotFoundException(ErrorCode.STICKER_NOT_FOUND)
		);

		UpdateDiaryStickerReq req = UpdateDiaryStickerReq.builder()
			.top(Integer.parseInt(top))
			.height(Integer.parseInt(height))
			.leftPos(Integer.parseInt(left))
			.rotate(Integer.parseInt(rotate))
			.width(Integer.parseInt(width))
			.build();

		diarySticker.updateDiarySticker(req);
		diaryStickerRepository.save(diarySticker);

		return CompletableFuture.completedFuture(response);
	}
}

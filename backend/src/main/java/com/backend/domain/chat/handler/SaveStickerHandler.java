package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.dto.request.UpdateDiaryStickerReq;
import com.backend.domain.chat.util.DiaryStickerUtils;
import com.backend.domain.chat.util.PositionUtils;
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
	private final DiaryStickerUtils diaryStickerUtils;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {

		Map<String, Object> resultMap = PositionUtils.extractPositionData(payload, "id");
		String stickerId = (String) resultMap.get("id");
		String stickerUrl = (String) resultMap.get("url");
		ObjectNode positionNode = (ObjectNode) resultMap.get("positionNode");

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "save_sticker");
		response.put("sticker_id", stickerId);
		response.put("image", stickerUrl);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		DiarySticker diarySticker = diaryStickerUtils.fetchDiarySticker(stickerId);

		UpdateDiaryStickerReq req = UpdateDiaryStickerReq.builder()
			.top(positionNode.get("top2").asInt())
			.height(positionNode.get("height2").asInt())
			.leftPos(positionNode.get("left2").asInt())
			.rotate(positionNode.get("rotate2").asInt())
			.width(positionNode.get("width2").asInt())
			.build();

		diarySticker.updateDiarySticker(req);
		diaryStickerRepository.save(diarySticker);

		return CompletableFuture.completedFuture(response);
	}
}

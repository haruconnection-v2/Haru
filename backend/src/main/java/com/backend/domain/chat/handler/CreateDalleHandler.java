package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.domain.chat.util.HaruRoomUtils;
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
public class CreateDalleHandler implements RoomMessageHandler {

	private final DiaryStickerRepository diaryStickerRepository;
	private final HaruRoomUtils haruRoomUtils;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Long roomId, Map<String, JsonNode> payload) {

		Map<String, Object> resultMap = PositionUtils.extractPositionData(payload, "dalle_id");
		String dalleId = (String) resultMap.get("id");
		String dalleUrl = (String) resultMap.get("url");
		ObjectNode positionNode = (ObjectNode) resultMap.get("positionNode");

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "create_dalle");
		response.put("dalle_id", dalleId);
		response.put("image", dalleUrl);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		HaruRoom haruRoom = haruRoomUtils.fetchHaruRoom(roomId);

		DiarySticker diarySticker = DiarySticker.builder()
			.stickerImageUrl(dalleUrl)
			.top(positionNode.get("top2").asInt())
			.height(positionNode.get("height2").asInt())
			.leftPos(positionNode.get("left2").asInt())
			.rotate(positionNode.get("rotate2").asInt())
			.width(positionNode.get("width2").asInt())
			.diary(haruRoom.getDiary())
			.build();

		diaryStickerRepository.save(diarySticker);

		return CompletableFuture.completedFuture(response);
	}
}

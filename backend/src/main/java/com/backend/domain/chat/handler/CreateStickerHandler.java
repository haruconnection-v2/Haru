package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.repository.DiaryStickerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateStickerHandler implements RoomMessageHandler {

	private final DiaryStickerRepository diaryStickerRepository;
	private final HaruRoomRepository haruRoomRepository;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Long roomId, Map<String, JsonNode> payload) {

		String stickerId = payload.get("sticker_id").asText();
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
		response.put("type", "create_sticker");
		response.put("sticker_id", stickerId);
		response.put("image", stickerUrl);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		Optional<HaruRoom> haruRoomOptional = haruRoomRepository.findById(roomId);
		// TODO make exception
		HaruRoom haruRoom = haruRoomOptional.orElseThrow();

		DiarySticker diarySticker = DiarySticker.builder()
			.stickerImageUrl(stickerUrl)
			.top(Integer.parseInt(top))
			.leftPos(Integer.parseInt(left))
			.width(Integer.parseInt(width))
			.height(Integer.parseInt(height))
			.rotate(Integer.parseInt(rotate))
			.diary(haruRoom.getDiary())
			.build();

		diaryStickerRepository.save(diarySticker);

		return CompletableFuture.completedFuture(response);
	}
}

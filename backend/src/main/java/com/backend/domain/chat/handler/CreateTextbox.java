package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.domain.diary.entity.Diary;
import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.repository.DiaryTextBoxRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTextbox implements RoomMessageHandler {

	private final DiaryTextBoxRepository diaryTextBoxRepository;
	private final HaruRoomRepository haruRoomRepository;

	@Async
	@Override
	public CompletableFuture<JsonNode> handle(Long roomId, Map<String, JsonNode> payload) {
		JsonNode textData = payload.get("position");
		String x = textData.get("x").asText();
		String y = textData.get("y").asText();
		String width = textData.get("width").asText();
		String height = textData.get("height").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("x", x);
		positionNode.put("y", y);
		positionNode.put("width", width);
		positionNode.put("height", height);

		HaruRoom haruRoom = haruRoomRepository.findById(roomId).orElseThrow(
			() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND)
		);

		DiaryTextBox newDiaryTextBox = DiaryTextBox.builder()
			.content(null)
			.xcoor(Integer.parseInt(x))
			.ycoor(Integer.parseInt(y))
			.width(Integer.parseInt(width))
			.height(Integer.parseInt(height))
			.diary(haruRoom.getDiary())
			.build();

		diaryTextBoxRepository.save(newDiaryTextBox);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "create_textbox");
		response.put("text_id", newDiaryTextBox.getId().toString());
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return CompletableFuture.completedFuture(response);
	}
}

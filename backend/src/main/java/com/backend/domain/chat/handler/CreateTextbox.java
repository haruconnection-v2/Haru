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
	private final HaruRoomUtils haruRoomUtils;

	@Async
	@Override
	public JsonNode handle(Long roomId, Map<String, JsonNode> payload) {

		ObjectNode positionNode = PositionUtils.extractPositionData(payload);

		HaruRoom haruRoom = haruRoomUtils.fetchHaruRoom(roomId);

		DiaryTextBox newDiaryTextBox = DiaryTextBox.builder()
			.content(null)
			.xcoor(positionNode.get("x").asInt())
			.ycoor(positionNode.get("y").asInt())
			.width(positionNode.get("width").asInt())
			.height(positionNode.get("height").asInt())
			.diary(haruRoom.getDiary())
			.build();

		diaryTextBoxRepository.save(newDiaryTextBox);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "create_textbox");
		response.put("text_id", newDiaryTextBox.getId().toString());
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return response;
	}
}

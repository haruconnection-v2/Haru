package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.repository.DiaryTextBoxRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTextbox implements RoomMessageHandler {

	private final DiaryTextBoxRepository diaryTextBoxRepository;
	private final HaruRoomRepository haruRoomRepository;

	@Override
	public JsonNode handle(Long roomId, Map<String, JsonNode> payload) {

		String textId = payload.get("id").asText();
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

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "create_textbox");
		response.put("text_id", textId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		Optional<HaruRoom> haruRoomOptional = haruRoomRepository.findById(roomId);
		// TODO make exception
		HaruRoom haruRoom = haruRoomOptional.orElseThrow();

		// 사용자를 식별할 수 있는 토큰이라던지 관련 로직이 필요할 듯
		// 생성 초기 content는 null로 들어가야 할 듯.
		DiaryTextBox newDiaryTextBox = DiaryTextBox.builder()
			.writer("1")
			.content(null)
			.xcoor(Integer.parseInt(x))
			.ycoor(Integer.parseInt(y))
			.width(Integer.parseInt(width))
			.height(Integer.parseInt(height))
			.diary(haruRoom.getDiary())
			.build();

		diaryTextBoxRepository.save(newDiaryTextBox);

		return response;
	}
}

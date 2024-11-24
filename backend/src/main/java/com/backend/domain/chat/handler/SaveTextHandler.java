package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.dto.request.UpdateDiaryTextBoxReq;
import com.backend.domain.chat.util.DiaryTextBoxUtils;
import com.backend.domain.diary.entity.DiaryTextBox;
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
public class SaveTextHandler implements MessageHandler {

	private final DiaryTextBoxRepository diaryTextBoxRepository;
	private final DiaryTextBoxUtils diaryTextBoxUtils;

	@Async
	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {

		String textId = payload.get("id").asText();
		String content = payload.get("content").asText();
		String nickname = payload.get("nickname").asText();
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
		response.put("type", "save_text");
		response.put("text_id", textId);
		response.put("content", content);
		response.put("nickname", nickname);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

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

		return response;
	}
}

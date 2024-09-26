package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SaveTextHandler implements MessageHandler {

	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		// 저장 로직

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

		return response;
	}
}

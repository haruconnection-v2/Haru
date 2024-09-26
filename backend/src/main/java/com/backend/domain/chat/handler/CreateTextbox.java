package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CreateTextbox implements MessageHandler {

	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		// 이해가 되지 않음

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

		return response;
	}
}

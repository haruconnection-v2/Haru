package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextInputHandler implements MessageHandler {

	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		String textId = payload.get("id").asText();
		String content = payload.get("content").asText();

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "text_input");
		response.put("text_id", textId);
		response.put("content", content);

		log.info("Response created: {}", response);

		return response;
	}
}
package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextInputHandler implements MessageHandler {

	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		String textId = payload.get("textId").asText();
		String content = payload.get("content").asText();

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "textInput");
		response.put("textId", textId);
		response.put("content", content);

		log.info("Response created: {}", response);

		return CompletableFuture.completedFuture(response);
	}
}

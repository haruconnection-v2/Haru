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
public class TextResizeHandler implements MessageHandler {
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		String textId = payload.get("id").asText();
		JsonNode textData = payload.get("position");
		String width = textData.get("width").asText();
		String height = textData.get("height").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("width", width);
		positionNode.put("height", height);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "text_resize");
		response.put("text_id", textId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return CompletableFuture.completedFuture(response);
	}
}

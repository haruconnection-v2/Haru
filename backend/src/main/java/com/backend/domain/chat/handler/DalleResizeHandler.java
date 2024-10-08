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
public class DalleResizeHandler implements MessageHandler {
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		String dalleId = payload.get("dalle_id").asText();
		JsonNode dalleData = payload.get("position");
		String width = dalleData.get("width2").asText();
		String height = dalleData.get("height2").asText();
		String top = dalleData.get("top2").asText();
		String left = dalleData.get("left2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("top2", top);
		positionNode.put("left2", left);
		positionNode.put("width2", width);
		positionNode.put("height2", height);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "dalle_resize");
		response.put("dalle_id", dalleId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return CompletableFuture.completedFuture(response);
	}
}

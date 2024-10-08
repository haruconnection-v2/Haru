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
public class DalleRotateHandler implements MessageHandler {
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		String dalleId = payload.get("dalle_id").asText();
		JsonNode dalleData = payload.get("position");
		String rotate = dalleData.get("rotate2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("rotate2", rotate);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "dalle_rotate");
		response.put("dalle_id", dalleId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return CompletableFuture.completedFuture(response);
	}
}

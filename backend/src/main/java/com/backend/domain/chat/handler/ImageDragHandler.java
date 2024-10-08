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
public class ImageDragHandler implements MessageHandler {
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		String stickerId = payload.get("stickerId").asText();
		JsonNode stickerData = payload.get("sticker_id");
		String top = stickerData.get("top2").asText();
		String left = stickerData.get("left2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("top2", top);
		positionNode.put("left2", left);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "image_drag");
		response.put("sticker_id", stickerId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return CompletableFuture.completedFuture(response);
	}
}

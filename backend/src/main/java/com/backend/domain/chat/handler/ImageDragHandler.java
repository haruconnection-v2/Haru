package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.backend.domain.chat.util.PositionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImageDragHandler implements MessageHandler {
	@Override
	public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		Map<String, ObjectNode> resultMap = PositionUtils.extractTopAndLeftData(payload, "stickerId");

		String stickerId = resultMap.keySet().iterator().next();
		ObjectNode positionNode = resultMap.get(stickerId);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "image_drag");
		response.put("sticker_id", stickerId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return CompletableFuture.completedFuture(response);
	}
}

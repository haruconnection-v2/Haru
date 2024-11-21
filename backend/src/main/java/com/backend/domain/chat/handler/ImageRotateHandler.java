package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImageRotateHandler implements MessageHandler {
	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		String stickerId = payload.get("sticker_id").asText();
		JsonNode stickerData = payload.get("position");
		String rotate = stickerData.get("rotate2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("rotate2", rotate);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "image_rotate");
		response.put("sticker_id", stickerId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return response;
	}
}

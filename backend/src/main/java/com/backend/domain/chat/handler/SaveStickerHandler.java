package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SaveStickerHandler implements MessageHandler {
	// 저장 로직

	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		String stickerId = payload.get("id").asText();
		String stickerUrl = payload.get("image").asText();
		JsonNode stickerData = payload.get("position");
		String width = stickerData.get("width2").asText();
		String height = stickerData.get("height2").asText();
		String top = stickerData.get("top2").asText();
		String left = stickerData.get("left2").asText();
		String rotate = stickerData.get("rotate2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("top2", top);
		positionNode.put("left2", left);
		positionNode.put("width2", width);
		positionNode.put("height2", height);
		positionNode.put("rotate2", rotate);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "save_sticker");
		response.put("sticker_id", stickerId);
		response.put("image", stickerUrl);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return response;
	}
}

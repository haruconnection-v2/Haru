package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CreateDalleHandler implements MessageHandler {
	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		// dalleId 없는 경우 create
		String dalleId = payload.get("id").asText();
		String dalleUrl = payload.get("image").asText();
		JsonNode dalleData = payload.get("position");
		String width = dalleData.get("width2").asText();
		String height = dalleData.get("height2").asText();
		String top = dalleData.get("top2").asText();
		String left = dalleData.get("left2").asText();
		String rotate = dalleData.get("rotate2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("top2", top);
		positionNode.put("left2", left);
		positionNode.put("width2", width);
		positionNode.put("height2", height);
		positionNode.put("rotate2", rotate);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "create_dalle");
		response.put("dalle_id", dalleId);
		response.put("image", dalleUrl);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return response;
	}
}

package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DalleDragHandler implements MessageHandler {
	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		String dalleId = payload.get("dalle_id").asText();
		JsonNode dalleData = payload.get("position");
		String top = dalleData.get("top2").asText();
		String left = dalleData.get("left2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("top2", top);
		positionNode.put("left2", left);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "dalle_drag");
		response.put("dalle_id", dalleId);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		return response;
	}
}
package com.backend.domain.chat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeleteObjectHandler implements MessageHandler {
	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		// 삭제로직

		String objectType = payload.get("object_type").asText();
		String objectId = payload.get("object_id").asText();

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "delete_object");
		response.put("object_type", objectType);
		response.put("object_id", objectId);

		log.info("Response created: {}", response);

		return response;
	}
}

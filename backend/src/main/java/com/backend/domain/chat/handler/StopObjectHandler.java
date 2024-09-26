package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class StopObjectHandler implements MessageHandler {
	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {
		String objectType = payload.get("object_type").asText();

		if (Objects.equals(objectType, "sticker")) {
			String stickerId = payload.get("id").asText();
			String stickerData = payload.get("position").asText();

			// 저장
		} else if (Objects.equals(objectType, "text")) {
			String textboxId = payload.get("id").asText();
			String textboxData = payload.get("position").asText();

			// 저장
		}

		return null;
	}
}

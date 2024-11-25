package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;

public interface RoomMessageHandler extends MessageHandler {

	CompletableFuture<JsonNode> handle(Long roomId, Map<String, JsonNode> payload);

	@Override
	default CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {
		return CompletableFuture.completedFuture(null);
	}
}

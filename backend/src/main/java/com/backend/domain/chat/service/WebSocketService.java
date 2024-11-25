package com.backend.domain.chat.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public interface WebSocketService {

	public void initializeHandlers();

	public CompletableFuture<JsonNode> registerHandler(Long roomId, String type, Map<String, JsonNode> payload);

	public CompletableFuture<JsonNode> positionProcess(String type, Long objectId, Map<String, JsonNode> payload);

}

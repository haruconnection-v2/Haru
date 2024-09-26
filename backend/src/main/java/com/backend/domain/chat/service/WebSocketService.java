package com.backend.domain.chat.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public interface WebSocketService {

	public void initializeHandlers();

	public JsonNode registerHandler(String type, Map<String, JsonNode> payload);
}

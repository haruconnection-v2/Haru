package com.backend.domain.chat.handler;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public interface MessageHandler {

	JsonNode handle(Map<String, JsonNode> payload);

}

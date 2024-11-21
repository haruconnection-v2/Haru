package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;

public interface MessageHandler {

	CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload);

}

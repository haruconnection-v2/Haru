package com.backend.domain.chat.handler.event.eventProcessor;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface PositionEventListener {
    void onEventCompleted(String objectId, JsonNode result);
}

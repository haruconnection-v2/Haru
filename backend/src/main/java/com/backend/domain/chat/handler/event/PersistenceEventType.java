package com.backend.domain.chat.handler.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;

public enum PersistenceEventType {
    SAVE_TEXT(TextNode.valueOf("saveTextBox")),
    CREATE_TEXT(TextNode.valueOf("createTextBox"));

    private final TextNode eventType;

    PersistenceEventType(TextNode eventType) {
        this.eventType = eventType;
    }

    public static PersistenceEventType setType(String type) {
        return Arrays.stream(values()).filter(t -> t.eventType.asText().equals(type)).findFirst()
                .orElseThrow();
    }

    public boolean isEqual(JsonNode type) {
        return this.eventType.equals(type);
    }

    public String getType() {
        return this.eventType.asText();
    }
}

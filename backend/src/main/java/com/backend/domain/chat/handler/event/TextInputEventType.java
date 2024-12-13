package com.backend.domain.chat.handler.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum TextInputEventType {
    CONTENT(TextNode.valueOf("textInput")),
    NICKNAME(TextNode.valueOf("nicknameInput"));

    private final TextNode eventType;

    TextInputEventType(TextNode eventType) {
        this.eventType = eventType;
    }

    public static boolean hasType(final JsonNode type) {
        for (TextInputEventType t : values()) {
            if (t.getEventType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static TextInputEventType setType(final JsonNode type) {
        return Arrays.stream(values()).filter(t -> t.eventType.equals(type)).findFirst()
                .orElseThrow();
    }
}

package com.backend.domain.chat.handler.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PositionEventType {
    IMAGE_DRAG(TextNode.valueOf("imageDrag")),
    IMAGE_RESIZE(TextNode.valueOf("imageResize")),
    IMAGE_ROTATE(TextNode.valueOf("imageRotate")),
    TEXT_DRAG(TextNode.valueOf("textDrag")),
    TEXT_DRAG_STOP(TextNode.valueOf("textDragStop")),
    TEXT_RESIZE(TextNode.valueOf("textResize"));

    private final TextNode eventType;

    PositionEventType(TextNode eventType) {
        this.eventType = eventType;
    }

    public static PositionEventType setType(final JsonNode type) {
        return Arrays.stream(values()).filter(t -> t.eventType.equals(type)).findFirst()
                .orElseThrow();
    }
}

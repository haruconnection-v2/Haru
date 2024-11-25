package com.backend.domain.chat.handler.event;

import com.fasterxml.jackson.databind.node.TextNode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PositionEventType {
    IMAGE_DRAG("position", TextNode.valueOf("imageDrag"), "id"),
    IMAGE_RESIZE("position", TextNode.valueOf("imageResize"), "id"),
    IMAGE_ROTATE("position", TextNode.valueOf("imageRotate"), "id"),
    TEXT_DRAG("position", TextNode.valueOf("textDrag"), "id"),
    TEXT_RESIZE("position", TextNode.valueOf("textResize"), "id");

    private final String handlerType;
    private final TextNode eventType;
    private final String idType;

    PositionEventType(String handlerType, TextNode eventType, String idType) {
        this.handlerType = handlerType;
        this.eventType = eventType;
        this.idType = idType;
    }

    public static PositionEventType setType(String type) {
        return Arrays.stream(values()).filter(t -> t.eventType.asText().equals(type)).findFirst()
                .orElseThrow();
    }
}

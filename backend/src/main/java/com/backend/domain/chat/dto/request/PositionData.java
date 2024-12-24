package com.backend.domain.chat.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

public record PositionData(
        JsonNode node
) {

    public int getX() {
        return node.get("x").asInt();
    }

    public int getY() {
        return node.get("y").asInt();
    }

    public int getWidth() {
        return node.get("width").asInt();
    }

    public int getHeight() {
        return node.get("height").asInt();
    }
}

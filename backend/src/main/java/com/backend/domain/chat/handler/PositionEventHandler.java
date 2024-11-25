package com.backend.domain.chat.handler;

import com.backend.domain.chat.handler.event.PositionEventType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PositionEventHandler {

    public CompletableFuture<JsonNode> handle(Long objectId, PositionEventType positionEventType,
            Map<String, JsonNode> payload) {
        //JsonNode objectId = payload.get(positionEventType.getIdType()); //objectId
        JsonNode positionData = payload.get(positionEventType.getHandlerType());
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.set("type", positionEventType.getEventType()); //type
        response.set(positionEventType.getIdType(), TextNode.valueOf(objectId.toString()));
        response.set(positionEventType.getHandlerType(), positionData);

        log.info("Response created: {}", response);

        return CompletableFuture.completedFuture(response);
    }
}

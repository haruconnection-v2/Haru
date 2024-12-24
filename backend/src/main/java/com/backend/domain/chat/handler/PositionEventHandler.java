package com.backend.domain.chat.handler;

import com.backend.domain.chat.handler.event.PositionEventType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PositionEventHandler {

    public JsonNode handle(final Long objectId, final JsonNode eventType,
            final JsonNode positionData, final JsonNode nickname) {
        //JsonNode objectId = payload.get(positionEventType.getIdType()); //objectId
        final ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.set("type", eventType); //type
        response.set("id", TextNode.valueOf(objectId.toString()));
        response.set("position", positionData);
        response.set("nickname", nickname);

        log.info("Response created: {}", response);

        return response;
    }
}

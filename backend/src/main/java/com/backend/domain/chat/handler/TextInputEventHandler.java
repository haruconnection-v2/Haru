package com.backend.domain.chat.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TextInputEventHandler {

    public JsonNode handle(final Long objectId,
            final JsonNode type, final JsonNode textData) {
        //JsonNode objectId = payload.get(positionEventType.getIdType()); //objectId
        final ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.set("type", type); //type
        response.set("id", TextNode.valueOf(objectId.toString()));
        response.set(type.getNodeType().name(), textData);

        log.info("Response created: {}", response);

        return response;
    }
}

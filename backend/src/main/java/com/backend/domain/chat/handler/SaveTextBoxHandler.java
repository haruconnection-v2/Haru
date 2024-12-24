package com.backend.domain.chat.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaveTextBoxHandler {

    public JsonNode handle(final Long objectId, final JsonNode type, final String content,
            final String nickname, final JsonNode positionData) {

        final ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.set("type", type);
        response.put("textId", objectId);
        response.put("content", content);
        response.put("nickname", nickname);
        response.set("position", positionData);
        log.info("Response created: {}", response);
        return response;
    }

}

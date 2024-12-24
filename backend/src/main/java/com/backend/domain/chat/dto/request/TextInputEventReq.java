package com.backend.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class TextInputEventReq {

    private final JsonNode type;
    private final Long id;
    private final JsonNode textData;
    private final JsonNode nickname;

    @JsonCreator
    public TextInputEventReq(final JsonNode type, final Long id, final JsonNode content,
            final JsonNode nickname) {
        this.type = type;
        this.id = id;
        this.textData = determineTextData(content, nickname);
        this.nickname = nickname;
    }

    private JsonNode determineTextData(final JsonNode content, final JsonNode nickname) {
        validateTextData(content, nickname);
        if (content == null) {
            return nickname;
        }
        return content;
    }

    private void validateTextData(final JsonNode content, final JsonNode nickname) {
        if (content == null && nickname == null) {
            throw new NullPointerException("content and nickname null");
        }
    }
}
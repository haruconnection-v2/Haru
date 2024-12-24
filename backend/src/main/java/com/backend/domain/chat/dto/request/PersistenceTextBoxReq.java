package com.backend.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class PersistenceTextBoxReq {

    private final JsonNode type;
    private final Long id;
    private final JsonNode nickname;
    private final String content;
    private final PositionData positionData;

    public PersistenceTextBoxReq(
            final JsonNode type, final Long id, final String content, final JsonNode nickname,
            @JsonProperty("position") JsonNode position) {
        this.type = type;
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.positionData = new PositionData(position);
    }
}

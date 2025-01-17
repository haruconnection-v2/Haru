package com.backend.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PositionEventReq {

    private final JsonNode type;
    private final Long id;
    @NotNull
    private final PositionData positionData;
    private final JsonNode nickname;

    public PositionEventReq(
            final JsonNode type, final Long id, final JsonNode nickname,
            @JsonProperty("position") final JsonNode position) {
        this.type = type;
        this.id = id;
        this.nickname = nickname;
        this.positionData = new PositionData(position);
    }
}

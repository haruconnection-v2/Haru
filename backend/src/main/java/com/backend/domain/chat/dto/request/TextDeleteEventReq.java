package com.backend.domain.chat.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

public record TextDeleteEventReq(@NotNull Long id, @NotNull JsonNode type) {

}

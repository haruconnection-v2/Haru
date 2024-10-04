package com.backend.domain.chat.dto.request;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebSocketMessageReq {
	private String type;
	private Map<String, JsonNode> payload = new HashMap<>();

	// @JsonAnySetter -> 나머지 필드 저장
	@JsonAnySetter
	public void setPayload(String key, JsonNode value) {
		payload.put(key, value);
	}
}

package com.backend.domain.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.backend.domain.chat.dto.request.WebSocketMessageReq;
import com.backend.domain.chat.service.WebSocketServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	private final ObjectMapper objectMapper;
	private final WebSocketServiceImpl webSocketService;
	private final SimpMessagingTemplate messagingTemplate; //특정 Broker로 메세지를 전달

	@MessageMapping("/{roomId}") // 클라이언트가 /send/{roomId}로 메시지를 전송하면 호출됨
	@SendTo("/harurooms/{roomId}")   // /room/{roomId}로 구독하고 있는 클라이언트에게 메시지 전송
	public void chat(@DestinationVariable Long roomId, @Payload String payload) throws JsonProcessingException {

		WebSocketMessageReq webSocketMessage = objectMapper.readValue(payload, WebSocketMessageReq.class);

		JsonNode response = webSocketService.registerHandler(roomId, webSocketMessage.getType(), webSocketMessage.getPayload());

		if (response == null) {
			throw new IllegalArgumentException("Handler did not return any response");
		}

		// 구독하고 있는 장소로 메시지 전송 (방 ID를 포함하여 전송)
		messagingTemplate.convertAndSend("/harurooms/" + roomId, response);
	}
}

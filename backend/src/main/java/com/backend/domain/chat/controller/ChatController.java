package com.backend.domain.chat.controller;

import java.util.concurrent.CompletableFuture;

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
		log.error("Received message from room {}: {}", roomId, payload);
		WebSocketMessageReq webSocketMessage = objectMapper.readValue(payload, WebSocketMessageReq.class);
		JsonNode response = webSocketService.registerHandler(roomId, webSocketMessage.getType(), webSocketMessage.getPayload());

		if (response == null) {
			throw new IllegalArgumentException("Handler did not return any response");
		}

		// // 비동기 작업이 완료된 후 메시지를 전송
		// response.thenAccept(res -> {
		// 	messagingTemplate.convertAndSend("/harurooms/" + roomId, res);
		// 	log.info("Message sent to room {}: {}", roomId, res);
		// }).exceptionally(ex -> {
		// 	log.error("Failed to process message for room {}: {}", roomId, ex.getMessage());
		// 	return null;
		// });
		messagingTemplate.convertAndSend("/harurooms/" + roomId, response);

	}
}

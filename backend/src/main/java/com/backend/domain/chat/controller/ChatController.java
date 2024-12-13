package com.backend.domain.chat.controller;

import com.backend.domain.chat.dto.request.PositionEventReq;
import com.backend.domain.chat.dto.request.PersistenceTextBoxReq;
import com.backend.domain.chat.dto.request.TextDeleteEventReq;
import com.backend.domain.chat.dto.request.TextInputEventReq;
import java.util.concurrent.CompletableFuture;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
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
    public void chat(@DestinationVariable Long roomId, @Payload String payload)
            throws JsonProcessingException {
        log.info("chatController payload: {}", payload);
        WebSocketMessageReq webSocketMessage = objectMapper.readValue(payload,
                WebSocketMessageReq.class);
        log.info("webSocketMessage: {}", webSocketMessage.getPayload());
        CompletableFuture<JsonNode> response = webSocketService.registerHandler(roomId,
                webSocketMessage.getType(), webSocketMessage.getPayload());

        if (response == null) {
            throw new IllegalArgumentException("Handler did not return any response");
        }

        // 비동기 작업이 완료된 후 메시지를 전송
        response.thenAccept(res -> {
            messagingTemplate.convertAndSend("/harurooms/" + roomId, res);
            log.info("Message sent to room {}: {}", roomId, res);
        }).exceptionally(ex -> {
            log.error("Failed to process message for room {}: {}", roomId, ex.getMessage());
            return null;
        });
    }

    @MessageMapping("/position/{roomId}")
    @SendTo("/harurooms/{roomId}")
    public void positionChat(@DestinationVariable Long roomId, @Payload String payload)
            throws JsonProcessingException {
        PositionEventReq positionEventReq = objectMapper.readValue(payload,
                PositionEventReq.class);
        log.info("chatController.positionChat: {}", payload);
        webSocketService.positionProcess(roomId, positionEventReq);
    }

    @MessageMapping("/input/{roomId}")
    @SendTo("/harurooms/{roomId}")
    public void textInputChat(@DestinationVariable Long roomId, @Payload String payload)
            throws JsonProcessingException {
        TextInputEventReq textInputEventReq = objectMapper.readValue(payload,
                TextInputEventReq.class);
        log.info("chatController.textInputChat: {}", payload);
        webSocketService.textInputProcess(roomId, textInputEventReq);
    }

    @MessageMapping("/create-text/{roomId}")
    @SendTo("/harurooms/{roomId}")
    public void textCreateChat(@DestinationVariable Long roomId, @Payload String payload)
            throws JsonProcessingException {
        PersistenceTextBoxReq persistenceTextBoxReq = objectMapper.readValue(payload,
                PersistenceTextBoxReq.class);
        log.info("chatController.textCreateChat: {}", payload);
        webSocketService.textCreateProcess(roomId, persistenceTextBoxReq);
    }

    @MessageMapping("/save-text/{roomId}")
    @SendTo("/harurooms/{roomId}")
    public void textSaveChat(@DestinationVariable Long roomId, @Payload String payload)
            throws JsonProcessingException {
        PersistenceTextBoxReq persistenceTextBoxReq = objectMapper.readValue(payload,
                PersistenceTextBoxReq.class);
        log.info("chatController.textSaveChat: {}", payload);
        webSocketService.textSaveProcess(roomId, persistenceTextBoxReq);
    }

    @MessageMapping("/delete-text/{roomId}")
    @SendTo("/harurooms/{roomId}")
    @SendToUser
    public void textDeleteChat(@DestinationVariable Long roomId, @Payload String payload)
            throws JsonProcessingException {
        TextDeleteEventReq deleteEventReq = objectMapper.readValue(payload, TextDeleteEventReq.class);
        log.info("chatController.textDeleteChat: {}", payload);
        webSocketService.textDeleteProcess(roomId, deleteEventReq);
    }
}

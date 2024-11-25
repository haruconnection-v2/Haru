package com.backend.domain.chat.service;

import com.backend.domain.chat.handler.PositionEventHandler;
import com.backend.domain.chat.handler.event.PositionEventType;
import com.backend.domain.chat.handler.event.eventProcessor.PositionEventProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.backend.domain.chat.handler.CreateDalleHandler;
import com.backend.domain.chat.handler.CreateStickerHandler;
import com.backend.domain.chat.handler.CreateTextbox;
import com.backend.domain.chat.handler.DalleDragHandler;
import com.backend.domain.chat.handler.DalleResizeHandler;
import com.backend.domain.chat.handler.DalleRotateHandler;
import com.backend.domain.chat.handler.DeleteObjectHandler;
import com.backend.domain.chat.handler.ImageDragHandler;
import com.backend.domain.chat.handler.ImageResizeHandler;
import com.backend.domain.chat.handler.ImageRotateHandler;
import com.backend.domain.chat.handler.MessageHandler;
import com.backend.domain.chat.handler.NicknameInputHandler;
import com.backend.domain.chat.handler.RoomMessageHandler;
import com.backend.domain.chat.handler.SaveDalleHandler;
import com.backend.domain.chat.handler.SaveStickerHandler;
import com.backend.domain.chat.handler.SaveTextHandler;
import com.backend.domain.chat.handler.StopObjectHandler;
import com.backend.domain.chat.handler.TextDragHandler;
import com.backend.domain.chat.handler.TextInputHandler;
import com.backend.domain.chat.handler.TextResizeHandler;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final PositionEventProcessor positionEventProcessor;
    private final PositionEventHandler positionEventHandler;

    private final TextInputHandler textInputHandler;
    private final NicknameInputHandler nicknameInputHandler;
    private final TextDragHandler textDragHandler;
    private final TextResizeHandler textResizeHandler;
    private final SaveTextHandler saveTextHandler;
    private final CreateTextbox createTextbox;
    private final ImageDragHandler imageDragHandler;
    private final ImageResizeHandler imageResizeHandler;
    private final ImageRotateHandler imageRotateHandler;
    private final SaveStickerHandler saveStickerHandler;
    private final CreateStickerHandler createStickerHandler;
    private final DalleDragHandler dalleDragHandler;
    private final DalleResizeHandler dalleResizeHandler;
    private final DalleRotateHandler dalleRotateHandler;
    private final SaveDalleHandler saveDalleHandler;
    private final CreateDalleHandler createDalleHandler;
    private final DeleteObjectHandler deleteObjectHandler;
    private final StopObjectHandler stopObjectHandler;

    private final Map<String, MessageHandler> handlerMap = new HashMap<>();
    private final Map<String, RoomMessageHandler> roomHandlerMap = new HashMap<>();

    @Override
    @PostConstruct
    public void initializeHandlers() {
        handlerMap.put("textInput", textInputHandler);
        handlerMap.put("nicknameInput", nicknameInputHandler);
        handlerMap.put("textDrag", textDragHandler);
        handlerMap.put("textResize", textResizeHandler);
        handlerMap.put("saveText", saveTextHandler);
        handlerMap.put("imageDrag", imageDragHandler);
        handlerMap.put("imageResize", imageResizeHandler);
        handlerMap.put("imageRotate", imageRotateHandler);
        handlerMap.put("saveSticker", saveStickerHandler);
        handlerMap.put("dalleDrag", dalleDragHandler);
        handlerMap.put("dalleResize", dalleResizeHandler);
        handlerMap.put("dalleRotate", dalleRotateHandler);
        handlerMap.put("saveDalle", saveDalleHandler);
        handlerMap.put("deleteObject", deleteObjectHandler);
        handlerMap.put("dragStop", stopObjectHandler);
        handlerMap.put("rotateStop", stopObjectHandler);
        handlerMap.put("resizeStop", stopObjectHandler);

        roomHandlerMap.put("createTextbox", createTextbox);
        roomHandlerMap.put("createSticker", createStickerHandler);
        roomHandlerMap.put("createDalle", createDalleHandler);
    }

    @Override
    public CompletableFuture<JsonNode> registerHandler(Long roomId, String type,
            Map<String, JsonNode> payload) {
        MessageHandler handler = handlerMap.get(type);

        //TODO need refactor
        if (handler == null) {
            RoomMessageHandler roomHandler = roomHandlerMap.get(type);

            if (roomHandler == null) {
                log.error("No handler found for type: {} in roomId: {}", type, roomId);
                throw new IllegalArgumentException(
                        "No handler found for type: " + type + " in roomId: " + roomId);
            }

            return roomHandler.handle(roomId, payload);
        }
        return handler.handle(payload);
    }

    @Override
    public CompletableFuture<JsonNode> positionProcess(String type, Long objectId,
            Map<String, JsonNode> payload) {
        log.info("websocketService.positionProcess: {}", payload);
        PositionEventType eventType = PositionEventType.setType(type);
        //String objectId = payload.get(eventType.getIdType()).asText();
        return positionEventProcessor.submitEvent(objectId ,
                () -> positionEventHandler.handle(objectId, eventType, payload)
        );
    }
}
package com.backend.domain.chat.service;

import com.backend.domain.chat.dto.request.PersistenceTextBoxReq;
import com.backend.domain.chat.dto.request.PositionData;
import com.backend.domain.chat.dto.request.PositionEventReq;
import com.backend.domain.chat.dto.request.TextDeleteEventReq;
import com.backend.domain.chat.dto.request.TextInputEventReq;
import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.handler.PositionEventHandler;
import com.backend.domain.chat.handler.SaveTextBoxHandler;
import com.backend.domain.chat.handler.TextInputEventHandler;
import com.backend.domain.chat.handler.event.PersistenceEventType;
import com.backend.domain.chat.handler.event.PositionEventType;
import com.backend.domain.chat.handler.event.TextInputEventType;
import com.backend.domain.chat.handler.event.eventProcessor.EventQueueManager;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.domain.diary.entity.DiaryTextBox;
import com.backend.domain.diary.mapper.TextBoxMapper;
import com.backend.domain.diary.repository.DiaryTextBoxRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final DiaryTextBoxRepository diaryTextBoxRepository;
    private final HaruRoomRepository haruRoomRepository;
    private final SaveTextBoxHandler saveTextBoxHandler;
    private final PositionEventHandler positionEventHandler;
    private final EventQueueManager<JsonNode> eventQueueManager;
    private final TextInputEventHandler textInputEventHandler;
    private final SimpMessagingTemplate messagingTemplate; //특정 Broker로 메세지를 전달


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
    public void positionProcess(final Long roomId, final PositionEventReq positionEventReq) {
        final Long objectId = positionEventReq.getId();
        final PositionData positionData = positionEventReq.getPositionData();
        final JsonNode nickname = positionEventReq.getNickname();
        log.info("websocketService.positionProcess: x: {}, y: {}",
                positionData.getX(), positionData.getY());
        PositionEventType eventType = PositionEventType.setType(positionEventReq.getType());
        eventQueueManager.submitEvent(objectId,
                () -> positionEventHandler.handle(objectId, eventType.getEventType(),
                        positionData.node(), nickname)
        ).thenAccept(res -> {
            messagingTemplate.convertAndSend("/harurooms/" + roomId, res);
            log.info("Message sent to room {}: {}", roomId, res);
        }).exceptionally(ex -> {
            throw new IllegalArgumentException(ex);
        });
    }

    @Override
    public void textInputProcess(final Long roomId, final TextInputEventReq textInputEventReq) {
        final TextInputEventType eventType = TextInputEventType.setType(
                textInputEventReq.getType());
        final Long objectId = textInputEventReq.getId();
        final JsonNode textData = textInputEventReq.getTextData();
        final JsonNode nickname = textInputEventReq.getNickname();
        eventQueueManager.submitEvent(objectId,
                        () -> textInputEventHandler.handle(objectId, eventType.getEventType(), textData,
                                nickname))
                .thenAccept(res -> messagingTemplate.convertAndSend("/harurooms/" + roomId, res))
                .exceptionally(ex -> {
                    throw new IllegalArgumentException(ex);
                });
    }

    @Override
    @Transactional
    public void textCreateProcess(final Long roomId,
            final PersistenceTextBoxReq persistenceTextBoxReq) {
        final JsonNode type = persistenceTextBoxReq.getType();
        validateType(type, PersistenceEventType.CREATE_TEXT);
        final PositionData positionData = persistenceTextBoxReq.getPositionData();
        HaruRoom haruRoom = haruRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
        final DiaryTextBox textBox = diaryTextBoxRepository.save(
                TextBoxMapper.toCreateEntity(positionData, haruRoom));
        final JsonNode nickname = persistenceTextBoxReq.getNickname();
        eventQueueManager.submitEvent(textBox.getId(),
                () -> positionEventHandler.handle(textBox.getId(),
                        persistenceTextBoxReq.getType(), positionData.node(), nickname)
        ).thenAccept(res -> {
            messagingTemplate.convertAndSend("/harurooms/" + roomId, res);
            log.info("Message sent to room {}: {}", roomId, res);
        }).exceptionally(ex -> {
            throw new IllegalArgumentException(ex);
        });

    }

    @Override
    @Transactional
    public void textSaveProcess(final Long roomId,
            final PersistenceTextBoxReq persistenceTextBoxReq) {
        final Long objectId = persistenceTextBoxReq.getId();
        final JsonNode type = persistenceTextBoxReq.getType();
        validateType(type, PersistenceEventType.SAVE_TEXT);
        final String content = persistenceTextBoxReq.getContent();
        final JsonNode nickname = persistenceTextBoxReq.getNickname();
        final PositionData positionData = persistenceTextBoxReq.getPositionData();
        final DiaryTextBox diaryTextBox = diaryTextBoxRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException(ErrorCode.TEXT_BOX_NOT_FOUND)
        );
        diaryTextBox.updateDiaryTextBox(content, nickname.asText(), positionData);
        diaryTextBoxRepository.save(diaryTextBox);

        eventQueueManager.submitEvent(objectId,
                        () -> saveTextBoxHandler.handle(objectId, type, content, nickname,
                                positionData.node()))
                .thenAccept(res -> messagingTemplate.convertAndSend("/harurooms/" + roomId, res))
                .exceptionally(ex -> {
                    throw new IllegalArgumentException(ex);
                });
    }

    private void validateType(final JsonNode type,
            final PersistenceEventType persistenceEventType) {
        if (!persistenceEventType.isEqual(type)) {
            throw new IllegalArgumentException(type.asText());
        }
    }

    public void textDeleteProcess(final Long roomId, final TextDeleteEventReq deleteEventReq) {
        diaryTextBoxRepository.deleteById(deleteEventReq.id());
        final ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.set("type", deleteEventReq.type());
        response.put("id", deleteEventReq.id().toString());
        if (response.isNull()) {
            throw new NullPointerException("");
        }
        messagingTemplate.convertAndSend("/harurooms/" + roomId, response);
    }
}

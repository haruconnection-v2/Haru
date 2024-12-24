package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.util.HaruRoomUtils;
import com.backend.domain.chat.util.PositionUtils;
import com.backend.domain.diary.entity.DiarySticker;
import com.backend.domain.diary.repository.DiaryStickerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateStickerHandler implements RoomMessageHandler {

    private final DiaryStickerRepository diaryStickerRepository;
    private final HaruRoomUtils haruRoomUtils;

    @Override
    public CompletableFuture<JsonNode> handle(Long roomId, Map<String, JsonNode> payload) {
        Map<String, Object> resultMap = PositionUtils.extractPositionData(payload, "stickerId");
        String stickerId = (String) resultMap.get("id");
        String stickerUrl = (String) resultMap.get("url");
        ObjectNode positionNode = (ObjectNode) resultMap.get("positionNode");

        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("type", "createSticker");
        response.put("stickerId", stickerId);
        response.put("image", stickerUrl);
        response.set("position", positionNode);

        log.info("Response created: {}", response);

        HaruRoom haruRoom = haruRoomUtils.fetchHaruRoom(roomId);

        DiarySticker diarySticker = DiarySticker.builder()
                .stickerImageUrl(stickerUrl)
                .topPos(positionNode.get("topPos").asInt())
                .leftPos(positionNode.get("leftPos").asInt())
                .height(positionNode.get("height").asInt())
                .rotate(positionNode.get("rotate").asInt())
                .width(positionNode.get("width").asInt())
                .diary(haruRoom.getDiary())
                .build();

        diaryStickerRepository.save(diarySticker);

        return CompletableFuture.completedFuture(response);
    }
}

package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.domain.chat.dto.request.UpdateDiaryStickerReq;
import com.backend.domain.chat.util.DiaryStickerUtils;
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
public class SaveDalleHandler implements MessageHandler {

    private final DiaryStickerRepository diaryStickerRepository;
    private final DiaryStickerUtils diaryStickerUtils;

    @Async
    @Override
    public CompletableFuture<JsonNode> handle(Map<String, JsonNode> payload) {

        Map<String, Object> resultMap = PositionUtils.extractPositionData(payload, "id");
        String dalleId = (String) resultMap.get("id");
        String dalleUrl = (String) resultMap.get("url");
        ObjectNode positionNode = (ObjectNode) resultMap.get("positionNode");

        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("type", "saveDalle");
        response.put("dalleId", dalleId);
        response.put("image", dalleUrl);
        response.set("position", (ObjectNode) resultMap.get("positionNode"));

        log.info("Response created: {}", response);

        DiarySticker diarySticker = diaryStickerUtils.fetchDiarySticker(dalleId);

        UpdateDiaryStickerReq req = UpdateDiaryStickerReq.builder()
                .topPos(positionNode.get("topPos").asInt())
                .leftPos(positionNode.get("leftPos").asInt())
                .height(positionNode.get("height").asInt())
                .rotate(positionNode.get("rotate").asInt())
                .width(positionNode.get("width").asInt())
                .build();

        diarySticker.updateDiarySticker(req);
        diaryStickerRepository.save(diarySticker);

        return CompletableFuture.completedFuture(response);
    }
}

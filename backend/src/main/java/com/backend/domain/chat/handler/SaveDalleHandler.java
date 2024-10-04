package com.backend.domain.chat.handler;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.backend.domain.chat.dto.request.UpdateDiaryStickerReq;
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

	@Override
	public JsonNode handle(Map<String, JsonNode> payload) {

		String dalleId = payload.get("id").asText();
		String dalleUrl = payload.get("image").asText();
		JsonNode dalleData = payload.get("position");
		String width = dalleData.get("width2").asText();
		String height = dalleData.get("height2").asText();
		String top = dalleData.get("top2").asText();
		String left = dalleData.get("left2").asText();
		String rotate = dalleData.get("rotate2").asText();

		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("top2", top);
		positionNode.put("left2", left);
		positionNode.put("width2", width);
		positionNode.put("height2", height);
		positionNode.put("rotate2", rotate);

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("type", "save_dalle");
		response.put("dalle_id", dalleId);
		response.put("image", dalleUrl);
		response.set("position", positionNode);

		log.info("Response created: {}", response);

		Optional<DiarySticker> diaryStickerOptional = diaryStickerRepository.findById(Long.valueOf(dalleId));
		// TODO Exception
		DiarySticker diarySticker = diaryStickerOptional.orElseThrow();

		UpdateDiaryStickerReq req = UpdateDiaryStickerReq.builder()
			.top(Integer.parseInt(top))
			.height(Integer.parseInt(height))
			.leftPos(Integer.parseInt(left))
			.rotate(Integer.parseInt(rotate))
			.width(Integer.parseInt(width))
			.build();

		diarySticker.updateDiarySticker(req);
		diaryStickerRepository.save(diarySticker);

		return response;
	}
}

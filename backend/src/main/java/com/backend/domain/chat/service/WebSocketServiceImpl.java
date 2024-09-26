package com.backend.domain.chat.service;

import java.util.HashMap;
import java.util.Map;

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

@Service
@Transactional
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

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

	@Override
	@PostConstruct
	public void initializeHandlers() {
		handlerMap.put("text_input", textInputHandler);
		handlerMap.put("nickname_input", nicknameInputHandler);
		handlerMap.put("text_drag", textDragHandler);
		handlerMap.put("text_resize", textResizeHandler);
		handlerMap.put("save_text", saveTextHandler);
		handlerMap.put("create_textbox", createTextbox);
		handlerMap.put("image_drag", imageDragHandler);
		handlerMap.put("image_resize", imageResizeHandler);
		handlerMap.put("image_rotate", imageRotateHandler);
		handlerMap.put("save_sticker", saveStickerHandler);
		handlerMap.put("create_sticker", createStickerHandler);
		handlerMap.put("dalle_drag", dalleDragHandler);
		handlerMap.put("dalle_resize", dalleResizeHandler);
		handlerMap.put("dalle_rotate", dalleRotateHandler);
		handlerMap.put("save_dalle", dalleRotateHandler);
		handlerMap.put("save_dalle", saveDalleHandler);
		handlerMap.put("create_dalle", createDalleHandler);
		handlerMap.put("delete_object", deleteObjectHandler);
		handlerMap.put("drag_stop", stopObjectHandler);
		handlerMap.put("rotate_stop", stopObjectHandler);
		handlerMap.put("resize_stop", stopObjectHandler);
	}

	@Override
	public JsonNode registerHandler(String type, Map<String, JsonNode> payload) {
		MessageHandler handler = handlerMap.get(type);

		if (handler == null) {
			throw new IllegalArgumentException("No handler found for type: " + type);
		}

		return handler.handle(payload);
	}
}

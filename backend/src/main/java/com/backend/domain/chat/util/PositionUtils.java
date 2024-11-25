package com.backend.domain.chat.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.backend.domain.chat.dto.common.CreatePositionNodeDto;
import com.backend.domain.chat.dto.common.CreateTextBoxNodeDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class PositionUtils {

	public static Map<String, Object> extractPositionData(Map<String, JsonNode> payload, String idKey) {
		String id = payload.get(idKey).asText();
		String url = payload.get("image").asText();
		JsonNode stickerData = payload.get("position");
		String width = stickerData.get("width").asText();
		String height = stickerData.get("height").asText();
		String top = stickerData.get("topPos").asText();
		String left = stickerData.get("leftPos").asText();
		String rotate = stickerData.get("rotate").asText();

		CreatePositionNodeDto createPositionNodeDto = CreatePositionNodeDto.builder()
			.top(top)
			.left(left)
			.width(width)
			.height(height)
			.rotate(rotate)
			.build();
		ObjectNode objectNode = createPosition(createPositionNodeDto);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("stickerId", id);
		resultMap.put("url", url);
		resultMap.put("positionNode", objectNode);

		return resultMap;
	}

	public static Map<String, ObjectNode> extractTopAndLeftData(Map<String, JsonNode> payload, String idKey) {
		String id = payload.get(idKey).asText();
		JsonNode data = payload.get("position");
		String top = data.get("topPos").asText();
		String left = data.get("leftPos").asText();

		CreatePositionNodeDto createPositionNodeDto = CreatePositionNodeDto.builder()
			.top(top)
			.left(left)
			.build();
		ObjectNode objectNode = updateTopLeftPosition(createPositionNodeDto);

		Map<String, ObjectNode> resultMap = new HashMap<>();
		resultMap.put(id, objectNode);

		return resultMap;
	}

	public static Map<String, ObjectNode> extractPositionDataWithoutRotate(Map<String, JsonNode> payload, String idKey) {
		String id = payload.get(idKey).asText();
		JsonNode data = payload.get("position");

		String width = data.get("width").asText();
		String height = data.get("height").asText();
		String top = data.get("topPos").asText();
		String left = data.get("leftPos").asText();

		CreatePositionNodeDto createPositionNodeDto = CreatePositionNodeDto.builder()
			.width(width)
			.height(height)
			.top(top)
			.left(left)
			.build();
		ObjectNode objectNode = updatePositionWithoutRotate(createPositionNodeDto);

		Map<String, ObjectNode> resultMap = new HashMap<>();
		resultMap.put(id, objectNode);

		return resultMap;
	}

	public static Map<String, ObjectNode> extractRotateData(Map<String, JsonNode> payload, String idKey) {
		String id = payload.get(idKey).asText();
		JsonNode data = payload.get("position");
		String rotate = data.get("rotate").asText();

		CreatePositionNodeDto createPositionNodeDto = CreatePositionNodeDto.builder()
			.rotate(rotate)
			.build();
		ObjectNode objectNode = updateRotatePosition(createPositionNodeDto);

		Map<String, ObjectNode> resultMap = new HashMap<>();
		resultMap.put(id, objectNode);

		return resultMap;
	}

	private static ObjectNode createPosition(CreatePositionNodeDto dto) {
		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("topPos", dto.getTop());
		positionNode.put("leftPos", dto.getLeft());
		positionNode.put("width", dto.getWidth());
		positionNode.put("height", dto.getHeight());
		positionNode.put("rotate", dto.getRotate());

		return positionNode;
	}

	private static ObjectNode updateTopLeftPosition(CreatePositionNodeDto dto) {
		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("topPos", dto.getTop());
		positionNode.put("leftPos", dto.getLeft());

		return positionNode;
	}

	private static ObjectNode updatePositionWithoutRotate(CreatePositionNodeDto dto) {
		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("topPos", dto.getTop());
		positionNode.put("leftPos", dto.getLeft());
		positionNode.put("width", dto.getWidth());
		positionNode.put("height", dto.getHeight());

		return positionNode;
	}

	private static ObjectNode updateRotatePosition(CreatePositionNodeDto dto) {
		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("rotate", dto.getRotate());

		return positionNode;
	}

	public static ObjectNode extractPositionData(Map<String, JsonNode> payload) {
		JsonNode data = payload.get("position");
		String x = data.get("x").asText();
		String y = data.get("y").asText();
		String width = data.get("width").asText();
		String height = data.get("height").asText();

		CreateTextBoxNodeDto createTextBoxNodeDto = CreateTextBoxNodeDto.builder()
			.x(x)
			.y(y)
			.width(width)
			.height(height)
			.build();

		return createBoxPosition(createTextBoxNodeDto);
	}

	public static Map<String, ObjectNode> extractTopAndLeftData(Map<String, JsonNode> payload) {
		String id = payload.get("id").asText();
		JsonNode textData = payload.get("position");
		String x = textData.get("x").asText();
		String y = textData.get("y").asText();

		CreateTextBoxNodeDto createTextBoxNodeDto = CreateTextBoxNodeDto.builder()
			.x(x)
            .y(y)
            .build();

		ObjectNode objectNode = updateBoxTopLeftPosition(createTextBoxNodeDto);

		Map<String, ObjectNode> resultMap = new HashMap<>();
		resultMap.put(id, objectNode);

		return resultMap;
	}

	public static Map<String, ObjectNode> extractWidthAndHeightData(Map<String, JsonNode> payload) {
		String id = payload.get("id").asText();
		JsonNode textData = payload.get("position");
		String width = textData.get("width").asText();
		String height = textData.get("height").asText();

		CreateTextBoxNodeDto createTextBoxNodeDto = CreateTextBoxNodeDto.builder()
			.width(width)
			.height(height)
			.build();

		ObjectNode objectNode = updateBoxWidthHeightPosition(createTextBoxNodeDto);

		Map<String, ObjectNode> resultMap = new HashMap<>();
		resultMap.put(id, objectNode);

		return resultMap;
	}


	private static ObjectNode createBoxPosition(CreateTextBoxNodeDto dto) {
		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("x", dto.getX());
		positionNode.put("y", dto.getY());
		positionNode.put("width", dto.getWidth());
		positionNode.put("height", dto.getHeight());

		return positionNode;
	}

	private static ObjectNode updateBoxTopLeftPosition(CreateTextBoxNodeDto dto) {
		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("x", dto.getX());
		positionNode.put("y", dto.getY());

		return positionNode;
	}

	private static ObjectNode updateBoxWidthHeightPosition(CreateTextBoxNodeDto dto) {
		ObjectNode positionNode = JsonNodeFactory.instance.objectNode();
		positionNode.put("width", dto.getWidth());
		positionNode.put("height", dto.getHeight());

		return positionNode;
	}

}

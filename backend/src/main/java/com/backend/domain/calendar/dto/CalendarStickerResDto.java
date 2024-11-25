package com.backend.domain.calendar.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CalendarStickerResDto {
	private final Long id;
	private final String stickerImageUrl;
	private final int topPos;
	private final int leftPos;
	private final int width;
	private final int height;
	private final int rotate;
	@Builder
	public CalendarStickerResDto(Long id, String stickerImageUrl, int topPos, int leftPos, int width, int height, int rotate) {
		this.id = id;
		this.stickerImageUrl = stickerImageUrl;
		this.topPos = topPos;
		this.leftPos = leftPos;
		this.width = width;
		this.height = height;
		this.rotate = rotate;
	}
}

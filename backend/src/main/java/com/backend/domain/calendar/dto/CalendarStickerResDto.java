package com.backend.domain.calendar.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CalendarStickerResDto {
	private final Long id;
	private final String stickerImgUrl;
	private final int top;
	private final int leftPos;
	private final int width;
	private final int height;
	private final int rotate;
	@Builder
	public CalendarStickerResDto(Long id, String stickerImgUrl, int top, int leftPos, int width, int height, int rotate) {
		this.id = id;
		this.stickerImgUrl = stickerImgUrl;
		this.top = top;
		this.leftPos = leftPos;
		this.width = width;
		this.height = height;
		this.rotate = rotate;
	}
}

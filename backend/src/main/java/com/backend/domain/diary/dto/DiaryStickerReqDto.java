package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryStickerReqDto {

	private final String stickerImageUrl;
	private final int top;
	private final int leftPos;
	private final int width;
	private final int height;
	private final int rotate;
	@Builder
	public DiaryStickerReqDto(String stickerImageUrl, int top, int leftPos, int width, int height, int rotate) {
		this.stickerImageUrl = stickerImageUrl;
		this.top = top;
		this.leftPos = leftPos;
		this.width = width;
		this.height = height;
		this.rotate = rotate;
	}
}

package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryTextBoxReqDto {
	private final String writer;
	private final String content;
	private final int xcoor;
	private final int ycoor;
	private final int width;
	private final int height;
	@Builder
	public DiaryTextBoxReqDto(String writer, String content, int xcoor, int ycoor, int width, int height) {
		this.writer = writer;
		this.content = content;
		this.xcoor = xcoor;
		this.ycoor = ycoor;
		this.width = width;
		this.height = height;
	}
}

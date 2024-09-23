package com.backend.domain.diary.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryTextBoxResDto {
	private final Long id;
	private final String writer;
	private final String content;
	private final int xcoor;
	private final int ycoor;
	private final int width;
	private final int height;
	@Builder
	public DiaryTextBoxResDto(Long id, String writer, String content, int xcoor, int ycoor, int width, int height) {
		this.id = id;
		this.writer = writer;
		this.content = content;
		this.xcoor = xcoor;
		this.ycoor = ycoor;
		this.width = width;
		this.height = height;
	}
}

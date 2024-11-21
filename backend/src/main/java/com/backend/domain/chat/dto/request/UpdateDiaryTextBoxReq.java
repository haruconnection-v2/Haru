package com.backend.domain.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateDiaryTextBoxReq {

	private final String content;
	private final int xcoor;
	private final int ycoor;
	private final int width;
	private final int height;
}

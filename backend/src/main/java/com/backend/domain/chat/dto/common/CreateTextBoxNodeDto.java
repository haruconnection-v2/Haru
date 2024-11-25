package com.backend.domain.chat.dto.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateTextBoxNodeDto {
	// x, y, width, height
	private final String x;
	private final String y;
	private final String width;
	private final String height;
}

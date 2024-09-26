package com.backend.domain.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateDiaryTextBoxReq {

	private String content;
	private int xcoor;
	private int ycoor;
	private int width;
	private int height;
}

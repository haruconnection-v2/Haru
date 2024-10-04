package com.backend.domain.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateDiaryStickerReq {

	private int top;
	private int leftPos;
	private int width;
	private int height;
	private int rotate;
}

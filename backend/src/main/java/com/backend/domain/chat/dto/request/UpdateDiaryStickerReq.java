package com.backend.domain.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateDiaryStickerReq {
	private final int topPos;
	private final int leftPos;
	private final int width;
	private final int height;
	private final int rotate;
}

package com.backend.domain.chat.dto.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreatePositionNodeDto {
 private final String top;
 private final String left;
 private final String width;
 private final String height;
 private final String rotate;
}

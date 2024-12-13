package com.backend.domain.diary.mapper;

import com.backend.domain.chat.dto.request.PositionData;
import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.diary.entity.DiaryTextBox;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextBoxMapper {

    public static DiaryTextBox toCreateEntity(final PositionData positionData, final HaruRoom haruRoom) {
        return DiaryTextBox.builder()
                .xcoor(positionData.getX())
                .ycoor(positionData.getY())
                .width(positionData.getWidth())
                .height(positionData.getHeight())
                .diary(haruRoom.getDiary())
                .build();
    }

}

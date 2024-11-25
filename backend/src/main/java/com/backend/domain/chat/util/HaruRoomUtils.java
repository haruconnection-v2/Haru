package com.backend.domain.chat.util;

import org.springframework.stereotype.Component;

import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HaruRoomUtils {
	private final HaruRoomRepository haruRoomRepository;
	public HaruRoom fetchHaruRoom(Long id) {
		return haruRoomRepository.findById(id).orElseThrow(
			() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND)
		);
	}
}

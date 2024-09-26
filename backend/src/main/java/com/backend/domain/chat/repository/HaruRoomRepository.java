package com.backend.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domain.chat.entity.HaruRoom;

@Repository
public interface HaruRoomRepository extends JpaRepository<HaruRoom, Long> {
}

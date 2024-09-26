package com.backend.domain.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domain.diary.entity.DiarySticker;

@Repository
public interface DiaryStickerRepository extends JpaRepository<DiarySticker, Long> {
}

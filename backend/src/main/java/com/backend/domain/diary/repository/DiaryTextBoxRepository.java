package com.backend.domain.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domain.diary.entity.Diary;
import com.backend.domain.diary.entity.DiaryTextBox;

@Repository
public interface DiaryTextBoxRepository extends JpaRepository<DiaryTextBox, Long> {

}

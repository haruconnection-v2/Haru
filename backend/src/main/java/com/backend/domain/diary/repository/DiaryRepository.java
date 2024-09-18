package com.backend.domain.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
	boolean existsByCalendarIdAndDay(Long calendarId, String day);

	@Query("SELECT new com.backend.domain.diary.dto.DiarySnsDto(d.id, d.monthYear, d.day, d.snsLink, d.isExpiry) "
		+ "FROM Diary d "
		+ "WHERE d.calendar.id = :calendarId AND d.day = :day")
	DiarySnsDto findDiarySnsByCalendarIdAndDay(Long calendarId, String day);
}


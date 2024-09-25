package com.backend.domain.calendar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.domain.calendar.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
	@Query("select c from Calendar c where c.member.id = :memberId and c.monthYear = :monthYear")
	Optional<Calendar> findByMemberIdAndMonthYear(Long memberId, String monthYear);
}

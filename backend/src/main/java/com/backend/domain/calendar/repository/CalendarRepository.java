package com.backend.domain.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.domain.calendar.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}

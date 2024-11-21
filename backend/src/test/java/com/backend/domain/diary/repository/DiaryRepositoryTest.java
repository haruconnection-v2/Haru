package com.backend.domain.diary.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.calendar.repository.CalendarRepository;
import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.entity.Diary;
import com.backend.helper.RepositoryTest;

@RepositoryTest
class DiaryRepositoryTest {

	@Autowired
	private DiaryRepository diaryRepository;
	@Autowired
	private CalendarRepository calendarRepository;
	private Calendar calendar;
	@BeforeEach
	void setUp() {
		calendar = calendarRepository.save(
			Calendar.builder().monthYear("2024-09").build());
	}
	@Test
	void 캘린더_아이디와_day로_일기를_조회한다() {
		// given
		Diary diary = Diary.builder()
			.monthYear("2024-09")
			.day("2024-09-15")
			.snsLink("ws/abcdefg")
			.diaryBgId(1L)
			.isExpiry(false)
			.calendar(calendar)
			.build();
		diaryRepository.save(diary);

		// when
		DiarySnsDto foundDiaryDto = diaryRepository.findDiarySnsByCalendarIdAndDay(calendar.getId(), "2024-09-15");

		// then
		assertThat(foundDiaryDto).isNotNull();
		assertThat(foundDiaryDto.getDiaryId()).isEqualTo(diary.getId());
		assertThat(foundDiaryDto.getMonthYear()).isEqualTo(diary.getMonthYear());
	}
	@Test
	void day가_일치하지_않는_일기는_조회되지_않는다() {
		// given
		Diary diary = Diary.builder()
			.monthYear("2024-09")
			.day("2024-09-14")
			.snsLink("ws/abcdefg")
			.calendar(calendar)
			.isExpiry(true)
			.build();
		diaryRepository.save(diary);

		// when
		DiarySnsDto foundDiaryDto = diaryRepository.findDiarySnsByCalendarIdAndDay(calendar.getId(), "2024-09-15");

		// then
		assertThat(foundDiaryDto).isNull();
	}
}
package com.backend.domain.calendar.entity;

import java.util.List;
import com.backend.domain.diary.entity.Diary;
import com.backend.global.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
@Getter
@Entity
public class Calendar extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String monthYear;

	@OneToMany(mappedBy = "calendar", orphanRemoval = true)
	private List<Diary> diary;
	protected Calendar() {
	}
	@Builder
	private Calendar(String monthYear, List<Diary> diary) {
		this.monthYear = monthYear;
		this.diary = diary;
	}
}

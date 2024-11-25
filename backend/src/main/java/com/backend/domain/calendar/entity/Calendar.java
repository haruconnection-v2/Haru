package com.backend.domain.calendar.entity;

import java.util.List;

import com.backend.domain.diary.entity.Diary;
import com.backend.domain.member.entity.Member;
import com.backend.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member member;

	@OneToMany(mappedBy = "calendar", orphanRemoval = true)
	private List<Diary> diary;
	@OneToMany(mappedBy = "calendar", orphanRemoval = true, cascade = CascadeType.MERGE)
	private List<CalendarSticker> calendarSticker;
	protected Calendar() {
	}
	@Builder
	private Calendar(String monthYear, Member member) {
		this.monthYear = monthYear;
		this.member = member;
	}
}

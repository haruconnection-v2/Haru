package com.backend.domain.diary.entity;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Diary extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	@Pattern(regexp = "^\\d{4}-(0\\d|1[0-2])$", message = "YYYY-MM 형식이 아닙니다.")
	private String monthYear;
	@Column(nullable = false)
	@Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "YYYY-MM-DD 형식이 아닙니다.")
	private String day;
	private String snsLink;
	@Column(nullable = false)
	private Long diaryBgId;
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isExpiry;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calendar_id", nullable = false)
	private Calendar calendar;

	protected Diary() {
	}
	@Builder
	private Diary(String monthYear, String day, String snsLink, Long diaryBgId, boolean isExpiry, Calendar calendar) {
		this.monthYear = monthYear;
		this.day = day;
		this.snsLink = snsLink;
		this.diaryBgId = diaryBgId;
		this.isExpiry = isExpiry;
		this.calendar = calendar;
	}

	public void expireDiary() {
		if (this.isExpiry) {
			throw new IllegalStateException("이미 만료된 다이어리입니다.");
		}
		this.isExpiry = true;
	}
}

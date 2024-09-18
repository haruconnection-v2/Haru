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
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Diary extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String monthYear;
	private String day;
	private String snsLink;
	private Long diaryBgId;
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isExpiry;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calendar_id", nullable = false)
	private Calendar calendar;

	protected Diary() {
	}
	@Builder
	private Diary(String monthYear, String day, String snsLink, Long diaryBgId, Boolean isExpiry, Calendar calendar) {
		this.monthYear = monthYear;
		this.day = day;
		this.snsLink = snsLink;
		this.diaryBgId = diaryBgId;
		this.isExpiry = isExpiry;
		this.calendar = calendar;
	}
	public void setIsExpiry(Boolean isExpiry) {
		this.isExpiry = isExpiry;
	}
}

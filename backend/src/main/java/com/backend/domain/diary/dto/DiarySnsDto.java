package com.backend.domain.diary.dto;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiarySnsDto {
	private Long diaryId;
	private String monthYear;
	private String day;
	private String snsLink;
	private boolean isExpiry;

	// 생성자
	public DiarySnsDto(Long diaryId, String monthYear, String day, String snsLink, boolean isExpiry) {
		this.diaryId = diaryId;
		this.monthYear = monthYear;
		this.day = day;
		this.snsLink = snsLink;
		this.isExpiry = isExpiry;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DiarySnsDto that = (DiarySnsDto) o;
		return diaryId.equals(that.diaryId)
			&& monthYear.equals(that.monthYear)
			&& day.equals(that.day)
			&& snsLink.equals(that.snsLink);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			diaryId,
			monthYear,
			day,
			snsLink
		);
	}
}

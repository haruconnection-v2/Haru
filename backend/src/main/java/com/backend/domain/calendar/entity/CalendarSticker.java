package com.backend.domain.calendar.entity;

import com.backend.global.common.BaseEntity;

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
public class CalendarSticker extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String stickerImgUrl;
	private int top;
	private int leftPos;
	private int width;
	private int height;
	private int rotate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Calendar calendar;

	protected CalendarSticker() {
	}

	@Builder
	public CalendarSticker(String stickerImgUrl, int top, int leftPos, int width, int height, int rotate, Calendar calendar) {
		this.stickerImgUrl = stickerImgUrl;
		this.top = top;
		this.leftPos = leftPos;
		this.width = width;
		this.height = height;
		this.rotate = rotate;
		this.calendar = calendar;
	}
}

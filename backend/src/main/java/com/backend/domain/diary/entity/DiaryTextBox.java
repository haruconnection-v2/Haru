package com.backend.domain.diary.entity;

import com.backend.domain.chat.dto.request.PositionData;
import com.backend.global.common.BaseEntity;

import jakarta.annotation.Nullable;
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
public class DiaryTextBox extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50)
	@Nullable
	private String writer;
	@Column(length = 500)
	@Nullable
	private String content;
	private int xcoor;
	private int ycoor;
	private int width;
	private int height;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Diary diary;

	protected DiaryTextBox() {
	}

	@Builder
	public DiaryTextBox(String writer, String content, int xcoor, int ycoor, int width, int height, Diary diary) {
		this.writer = writer;
		this.content = content;
		this.xcoor = xcoor;
		this.ycoor = ycoor;
		this.width = width;
		this.height = height;
		this.diary = diary;
	}

	public void updateDiaryTextBox(String content, String nickname, PositionData positionData) {
		this.content = content;
		this.writer = nickname;
		this.xcoor = positionData.getX();
		this.ycoor = positionData.getY();
		this.width = positionData.getWidth();
		this.height = positionData.getHeight();
	}
}

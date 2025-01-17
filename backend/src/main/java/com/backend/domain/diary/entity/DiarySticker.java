package com.backend.domain.diary.entity;

import com.backend.domain.chat.dto.request.UpdateDiaryStickerReq;
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
public class DiarySticker extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String stickerImageUrl;
	private int topPos;
	private int leftPos;
	private int width;
	private int height;
	private int rotate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Diary diary;

	protected DiarySticker() {
	}

	@Builder
	public DiarySticker(String stickerImageUrl, int topPos, int leftPos, int width, int height, int rotate, Diary diary) {
		this.stickerImageUrl = stickerImageUrl;
		this.topPos = topPos;
		this.leftPos = leftPos;
		this.width = width;
		this.height = height;
		this.rotate = rotate;
		this.diary = diary;
	}

	public void updateDiarySticker(UpdateDiaryStickerReq updateDiaryStickerReq) {
		this.topPos = updateDiaryStickerReq.getTopPos();
		this.leftPos = updateDiaryStickerReq.getLeftPos();
		this.width = updateDiaryStickerReq.getWidth();
		this.height = updateDiaryStickerReq.getHeight();
		this.rotate = updateDiaryStickerReq.getRotate();
	}
}

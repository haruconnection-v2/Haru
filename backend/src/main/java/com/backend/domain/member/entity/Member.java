package com.backend.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 50, unique = true)
	private String loginId;
	@Column(nullable = false, length = 50)
	private String nickname;
	@Column(nullable = false, length = 250)
	private String password;

	@OneToMany(mappedBy = "member", orphanRemoval = true)
	private final List<Calendar> calendars = new ArrayList<>();

	protected Member() {
	}

	@Builder
	private Member(String loginId, String nickname, String password) {
		this.loginId = loginId;
		this.nickname = nickname;
		this.password = password;
	}
}

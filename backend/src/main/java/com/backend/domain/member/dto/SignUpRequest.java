package com.backend.domain.member.dto;

import lombok.Getter;

@Getter
public class SignUpRequest {
	private final String loginId;
	private final String nickname;
	private final String password;

	public SignUpRequest(String loginId, String nickname, String password) {
		this.loginId = loginId;
		this.nickname = nickname;
		this.password = password;
	}
}

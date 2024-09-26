package com.backend.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequest {
	private final String loginId;
	private final String password;

	@Builder
	public LoginRequest(String loginId, String password) {
		this.loginId = loginId;
		this.password = password;
	}
}

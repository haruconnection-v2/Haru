package com.backend.domain.member.service;

import org.springframework.stereotype.Component;

import com.backend.domain.member.dto.LoginRequest;
import com.backend.domain.member.dto.SignUpRequest;

import jakarta.servlet.http.HttpServletRequest;

@Component
public interface MemberService {
	void singUp(SignUpRequest signUpRequest, HttpServletRequest request);

	String login(LoginRequest loginRequest, HttpServletRequest request);

	void logout(HttpServletRequest request);
}

package com.backend.domain.member.controller;

import static com.backend.global.common.response.ResultCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domain.member.dto.LoginRequest;
import com.backend.domain.member.dto.SignUpRequest;
import com.backend.domain.member.service.MemberService;
import com.backend.global.common.response.ResultResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/members/signup")
	public ResponseEntity<ResultResponse<Void>> signUp(@RequestBody SignUpRequest signUpRequest,
														HttpServletRequest request) {
		memberService.singUp(signUpRequest, request);
		ResultResponse<Void> resultResponse = ResultResponse.of(SIGN_UP_SUCCESS);
		return ResponseEntity.status(SIGN_UP_SUCCESS.getHttpStatus()).body(resultResponse);
	}

	@PostMapping("/members/login")
	public ResponseEntity<ResultResponse<String>> login(@RequestBody LoginRequest loginRequest,
														HttpServletRequest request) {
		String nickname = memberService.login(loginRequest, request);
		ResultResponse<String> resultResponse = ResultResponse.of(LOGIN_SUCCESS, nickname);
		return ResponseEntity.status(LOGIN_SUCCESS.getHttpStatus()).body(resultResponse);
	}

	@GetMapping("/members/logout")
	public ResponseEntity<ResultResponse<Void>> logout(HttpServletRequest request) {
		memberService.logout(request);
		ResultResponse<Void> resultResponse = ResultResponse.of(LOGOUT_SUCCESS);
		return ResponseEntity.status(LOGOUT_SUCCESS.getHttpStatus()).body(resultResponse);
	}
}


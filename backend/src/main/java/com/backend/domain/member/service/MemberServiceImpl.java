package com.backend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domain.member.dto.LoginRequest;
import com.backend.domain.member.dto.SingUpRequest;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.global.common.exception.BadRequestException;
import com.backend.global.common.response.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public void singUp(SingUpRequest singUpRequest, HttpServletRequest request) {
		//loginId, nickname, password 중복, 형식 확인은 따로 api처리 필요
		if (memberRepository.existsByLoginId(singUpRequest.getLoginId())) {
			throw new BadRequestException(ErrorCode.MEMBER_LOGIN_ID_CONFLICT);
		}
		if (memberRepository.existsByNickname(singUpRequest.getNickname())) {
			throw new BadRequestException(ErrorCode.MEMBER_NICKNAME_CONFLICT);
		}
		Member member = Member.builder()
			.loginId(singUpRequest.getLoginId())
			.nickname(singUpRequest.getNickname())
			.password(singUpRequest.getPassword())
			.build();
		memberRepository.save(member);
	}

	@Override
	public String login(LoginRequest loginRequest, HttpServletRequest request) {
		if (request.getSession().getAttribute("memberId") != null) {
			throw new BadRequestException(ErrorCode.MEMBER_LOGIN_CONFLICT);
		}
		String loginId = loginRequest.getLoginId();
		String password = loginRequest.getPassword();
		Member member = memberRepository.findByLoginIdAndPassword(loginId, password)
			.orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다."));
		request.getSession().setAttribute("memberId", member.getId());
		request.getSession().setAttribute("nickname", member.getNickname());
		return member.getNickname();
	}

	@Override
	public void logout(HttpServletRequest request) {
		if (request.getSession().getAttribute("memberId") == null) {
			throw new BadRequestException(ErrorCode.MEMBER_LOGOUT_CONFLICT);
		}
		request.getSession().invalidate();
	}
}

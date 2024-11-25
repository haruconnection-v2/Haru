package com.backend.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	@Query("select m from Member m where m.loginId = :loginId and m.password = :password")
	Optional<Member> findByLoginIdAndPassword(String loginId, String password);

	boolean existsByLoginId(String loginId);

	boolean existsByNickname(String nickname);
}
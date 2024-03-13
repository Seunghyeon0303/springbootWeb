package com.kh.join6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kh.join6.entity.JoinMember;

@Repository
public interface JoinMemberRepository extends JpaRepository<JoinMember, Long> {

	// email의 검색 값을 가져와서 데이터가 있으면 객체 타입으로 반환 받고 없으면 null을 넘기는 jpa를 만든다.
	JoinMember findByEmail(String userId);
}

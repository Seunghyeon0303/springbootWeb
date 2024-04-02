package com.kh.Final_Project.ask.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kh.Final_Project.ask.entity.Ask;

@Repository
public interface AskRepository extends JpaRepository<Ask, Long>{
	
	// 회원의 ocid로 게시글 목록출력
	Page<Ask> findByMemberId(Long memberId, Pageable pageable);
	
	Page<Ask> findByCustmerRealIdContaining(String id, Pageable pageable);
	
}
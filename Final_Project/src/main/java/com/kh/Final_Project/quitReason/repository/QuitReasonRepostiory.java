package com.kh.Final_Project.quitReason.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.Final_Project.quitReason.entity.QuitReason;

public interface QuitReasonRepostiory extends JpaRepository<QuitReason, Long>{
	
	 long countByReasonType(String reasonType); // reasonType 반환메서드
	 List<QuitReason> findByReasonType(String reasonType); // reason 목록 반환 메서드
	 Page<QuitReason> findByReasonType(String reasonType, Pageable pageable); // page로 생성
	 
}

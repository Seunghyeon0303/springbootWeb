package com.kh.Final_Project.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.kh.Final_Project.notice.entity.NoticeEntity;


public interface NoticeRepository extends JpaRepository<NoticeEntity, Long>{
	
	// 공지사항 전체 불러오기
	@Query(value = "SELECT * FROM notice_entity ORDER BY ocid DESC", nativeQuery = true)
	Page<NoticeEntity> findAll(Pageable pageable);
	

}
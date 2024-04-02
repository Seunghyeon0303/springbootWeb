package com.kh.Final_Project.faq.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kh.Final_Project.faq.entity.FAQEntity;

public interface FAQRepository
	extends JpaRepository<FAQEntity, Long>{

	// faq 전체 불러오기
		@Query(value = "SELECT * FROM faqentity ORDER BY ocid DESC", nativeQuery = true)
		Page<FAQEntity> findAll(Pageable pageable);
		
		 List<FAQEntity> findByCategory(String category);
}